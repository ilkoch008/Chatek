package chatek.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * Hello world!
 *
 */
public class App 
{
    public static Companions companions = new Companions();
    private static ConcurrentHashMap<ArrayList<Integer>, Conversation> conversations = new ConcurrentHashMap<>();

    public static void main( String[] args ) {
        companions.Add(new Companion("Server", 0, true, "ritnbritniwrnbowrtnpwirunbwriptu"));
        //companions.add(new Companion("Server1", 1));
        for (int i = 0; i < 100; i++){
            for (int j = i; j < 100; j++) {
                ArrayList<Integer> key = new ArrayList<>();
                key.add(i);
                key.add(j);
                Collections.sort(key);
                Conversation conversation = new Conversation();
                conversations.put(key, conversation);
            }
        }

        ServerSocket server = null;
        try {
            server = new ServerSocket(4444, 1000);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 4444");
            System.exit(-1);
        }

        while(companions.companions.size() < 100){
//        for(int i = 1; i < 100; i++){
            Client client = new Client();
            Socket fromclient = null;
            try {
                //System.out.print("Waiting for a client...");
                fromclient = server.accept();
                System.out.println("Client connected");
            } catch (IOException e) {
                System.out.println("Can't accept");
                System.exit(-1);
            }
            client.SetClient(fromclient);
            client.setCompanions(companions);
            client.setConversations(conversations);
            client.start();
        }
    }
}
