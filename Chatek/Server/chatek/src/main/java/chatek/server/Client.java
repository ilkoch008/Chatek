package chatek.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.google.gson.*;

public class Client extends Thread {
    ArrayList<Integer> key = new ArrayList<>();
    int clientId;
    int companionId;
    String nickName = " ";
    Socket fromClient = null;
    //ArrayList<Message> messages = new ArrayList<>();
    Companions companions = null;
    ConcurrentHashMap<ArrayList<Integer>, Conversation> conversations = null;
    Conversation conversation = null;

    public void SetClient(int clientId, Socket fromClient){
        this.clientId = clientId;
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
                    case 1:
                        nickName = jInput.get("data").getAsString();
                        System.out.println(nickName);
                        jOutput.addProperty("data", clientId);
                        out.println(jOutput.toString());
                        companions.Add(new Companion(nickName, clientId, true));
                        Thread.yield();
                        break;
                    case 2:
                        output = gson.toJson(companions);
                        out.println(output);
                        Thread.yield();
                        break;
                    case 3:
                        Message message = new Message.Builder()
                                .messageIs(jInput.get("message").getAsString())
                                .idIs(jInput.get("id").getAsInt())
                                .ownerIs(jInput.get("owner").getAsString())
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
                        break;
                    case 4:
                        try {
                            output = gson.toJson(conversation.getConversation());
                        } catch (ConcurrentModificationException e){
                            e.printStackTrace();
                            companions.companions.get(clientId).isNotAvailable();
                            Thread.currentThread().interrupt();
                        }

                        out.println(output);
                        Thread.yield();
                        break;
                    case 5:
                        out.println();
                        Thread.yield();
                        break;
                    case 6:

                        break;
                    case 9:
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
        companions.companions.get(clientId).isNotAvailable();
        System.out.println("Client disconnected");

    }
}
