package chatek.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.*;

public class Client extends Thread {

    final static int EVERYTHING_OK = 0;
    final static int CONNECT_TO_SERVER = 1;
    final static int LOG_IN = 11;
    final static int REGISTER = 12;
    final static int THIS_NAME_ALREADY_EXISTS = 13;
    final static int ACCOUNT_IS_NOT_FOUND = 14;
    final static int WRONG_PASSWORD = 15;
    final static int GET_COMPANIONS = 2;
    final static int SEND_MESSAGE = 3;
    final static int RENEW_DIALOG = 4;
    final static int WAIT_GET_COMPANIONS = 5;
    final static int WAIT_RENEW_DIALOG = 6;
    final static int CHECK_NEW_MESSAGES = 7;
    final static int CHECK_NEW_COMPANIONS = 8;
    final static int CHOOSE_COMPANION = 9;

    ArrayList<Integer> key = new ArrayList<>();
    Integer clientId = null;
    int companionId;
    String nickName = " ";
    String password = "";
    Socket fromClient = null;
    //ArrayList<Message> messages = new ArrayList<>();
    Companions companions = null;
    ConcurrentHashMap<ArrayList<Integer>, Conversation> conversations = null;
    Conversation conversation = null;
    public boolean LOGGED_IN = false;

    public void SetClient( Socket fromClient){
        this.fromClient = fromClient;
    }

    public void setCompanions(Companions companions){
        this.companions = companions;

    }

    public void setConversations(ConcurrentHashMap<ArrayList<Integer>, Conversation> conversations){
        this.conversations = conversations;
    }

    @Override
    public void run(){
        int command;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(fromClient.getInputStream()));
            out = new PrintWriter(fromClient.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("Problems with Input or Output streams");
            e.printStackTrace();
            System.exit(-1);
        }

        String input, output;
        output = null;
        JsonParser parser = new JsonParser();
        Gson gson = new Gson();
        System.out.println("Wait for messages");
        try {
            while ((input = in.readLine()) != null) {
                JsonObject jOutput = new JsonObject();
                JsonObject jInput = null;
                jInput = (JsonObject) parser.parse(input);
                command = jInput.get("command").getAsInt();
                switch (command){
                    case CONNECT_TO_SERVER:
//                        nickName = jInput.get("data").getAsString();
//                        System.out.println(nickName);
//                        jOutput.addProperty("data", clientId);
//                        out.println(jOutput.toString());
//                        //companions.Add(new Companion(nickName, clientId, true));
//                        Thread.yield();
                        break;
                    case LOG_IN:
                        nickName = jInput.get("nickName").getAsString();
                        password = jInput.get("password").getAsString();
                        Companion companion = companions.getCompanion(nickName);
                        if(companion == null){
                            jOutput.addProperty("command", ACCOUNT_IS_NOT_FOUND);
                            out.println(jOutput.toString());
                        } else if(!companion.password.equals(password)){
                            jOutput.addProperty("command", WRONG_PASSWORD);
                            out.println(jOutput.toString());
                        } else {
                            LOGGED_IN = true;
                            clientId = companion.id;
                            jOutput.addProperty("clientId", clientId);
                            jOutput.addProperty("command", EVERYTHING_OK);
                            out.println(jOutput.toString());
                            companions.getCompanion(nickName).isAvailable();
                        }
                        break;
                    case REGISTER:
                        nickName = jInput.get("nickName").getAsString();
                        password = jInput.get("password").getAsString();
                        if(companions.isNotUnique(nickName)){
                            jOutput.addProperty("command", THIS_NAME_ALREADY_EXISTS);
                            out.println(jOutput.toString());
                        } else {
                            synchronized (this) {
                                clientId = companions.companions.size();
                                companions.Add(new Companion(nickName, clientId, true, password));
                            }
                            jOutput.addProperty("clientId", clientId);
                            jOutput.addProperty("command", EVERYTHING_OK);
                            out.println(jOutput.toString());
                            companions.getCompanion(nickName).isAvailable();
                            LOGGED_IN = true;
                        }
                        break;
                    case GET_COMPANIONS:
                        if(LOGGED_IN) {
                            output = gson.toJson(companions);
                            out.println(output);
                            Thread.yield();
                        }
                        break;
                    case SEND_MESSAGE:
                        if(LOGGED_IN) {
                            Message message = new Message.Builder()
                                    .messageIs(jInput.get("message").getAsString())
                                    .idIs(jInput.get("id").getAsInt())
                                    .ownerIs(jInput.get("owner").getAsString())
                                    .timeIs(getCurrentTime())
                                    .build();
                            conversation.Add(message);
//                        if(key.contains(0)) {
//                        Message response = new Message.Builder()
//                                .messageIs("etggwrry")
//                                .idIs(0)
//                                .ownerIs("Server")
//                                .build();
//                        conversation.Add(response);
//                        }
                            output = gson.toJson(conversation.getConversation());
                            out.println(output);
                            Thread.yield();
                        }
                        break;
                    case RENEW_DIALOG:
                        if(LOGGED_IN) {
                            try {
                                output = gson.toJson(conversation.getConversation());
                            } catch (ConcurrentModificationException e) {
                                e.printStackTrace();
                                companions.companions.get(clientId).isNotAvailable();
                                Thread.currentThread().interrupt();
                            }

                            out.println(output);
                        }
                            Thread.yield();

                        break;
                    case WAIT_GET_COMPANIONS:
                        if(LOGGED_IN) {
                            out.println();
                        }
                        Thread.yield();
                        break;
                    case WAIT_RENEW_DIALOG:

                        break;
                    case CHOOSE_COMPANION:
                        if(LOGGED_IN) {
                            key.clear();
                            companionId = jInput.get("companionId").getAsInt();
                            if (companionId == 0) {
                                key.add(0);
                                key.add(0);
                            } else {
                                key.add(companionId);
                                key.add(clientId);
                                Collections.sort(key);
                            }
                            conversation = conversations.get(key);
                            out.println("ok");
                        }
                        Thread.yield();
                        break;
                }
                //out.println("S ::: " + input);
                System.out.println(input);
            }
            out.close();
            in.close();
            fromClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (clientId != null) {
            companions.companions.get(clientId).isNotAvailable();
            LOGGED_IN = false;
        }
        System.out.println("Client disconnected");

    }


    public static String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        cal.roll(Calendar.HOUR_OF_DAY, 1);
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate=dateFormat.format(date);
        System.out.println("Current time of the day using Calendar - 24 hour format: "+ formattedDate);
        return formattedDate;
    }
}
