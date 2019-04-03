package chatek.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.*;

public class Client extends Thread {
    int clientId;
    String nickName = " ";
    Socket fromClient = null;
    ArrayList<Message> messages = new ArrayList<>();

    public void SetClient(int clientId, Socket fromClient){
        this.clientId = clientId;
        this.fromClient = fromClient;
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
                        break;
                    case 2:
                        break;
                    case 3:
                        Message message = new Message.Builder()
                                .messageIs(jInput.get("message").getAsString())
                                .idIs(jInput.get("id").getAsInt())
                                .ownerIs(jInput.get("owner").getAsString())
                                .build();
                        messages.add(message);
                        Message response = new Message.Builder()
                                .messageIs("nea))0)0)")
                                .idIs(0)
                                .ownerIs("Server")
                                .build();
                        messages.add(response);
                        output = gson.toJson(messages);
                        out.println(output);
                        break;
                    case 4:
                        output = gson.toJson(messages);
                        out.println(output);
                        break;
                    case 5:
                        out.println();
                        break;
                    case 6:
                        nickName = jInput.get("data").getAsString();
                        System.out.println(nickName);
                        jOutput.addProperty("data", clientId);
                        out.println(jOutput.toString());
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
        System.out.println("Client disconnected");

    }
}
