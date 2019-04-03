package chatek.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Hello world!
 *
 */
public class App 
{
    ArrayList<Client> clients = new ArrayList<>();

    public static void main( String[] args )

    {
        ServerSocket server = null;
        try {
            server = new ServerSocket(4444, 1000);
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 4444");
            System.exit(-1);
        }


        for(int i = 0; i < 1000; i++){
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
            client.SetClient(i+1, fromclient);
            client.start();
        }
    }
}
