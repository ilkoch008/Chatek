package chatek.server;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class Conversation {
    private CopyOnWriteArrayList<Message> conversation = new CopyOnWriteArrayList<>();
    synchronized public void Add(Message message){
        conversation.add(message);
    }
    synchronized public CopyOnWriteArrayList<Message> getConversation(){
        return conversation;
    }
    synchronized public Integer getNumberOfMessages(){return conversation.size();}
}
