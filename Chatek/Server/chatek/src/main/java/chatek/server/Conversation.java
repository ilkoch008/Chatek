package chatek.server;

import java.util.ArrayList;

public class Conversation {
    private ArrayList<Message> conversation = new ArrayList<>();
    synchronized public void Add(Message message){
        conversation.add(message);
    }
    synchronized public ArrayList<Message> getConversation(){
        return conversation;
    }
}
