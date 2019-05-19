package chatek.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

public class Companions {
    public ArrayList<Companion> companions = new ArrayList<>();
    private ConcurrentHashMap<ArrayList<Integer>, Conversation> conversations = new ConcurrentHashMap<>();
    synchronized public void Add(Companion companion){
        companions.add(companion);
    }
    public int getSize(){return companions.size();}
    synchronized public void setConversations(ConcurrentHashMap<ArrayList<Integer>, Conversation> conversations){
        this.conversations = conversations;
    }

    synchronized public boolean isNotUnique(String nickName) {
        for (Companion companion: companions) {
            if (companion.name.equals(nickName))
                return true;
        }
        return false;
    }

    synchronized public Companion getCompanion(String nickName) {
        for (Companion companion: companions) {
            if (companion.name.equals(nickName))
                return companion;
        }
        return null;
    }

    synchronized public void setNumbersOfMessagesInCompanions(Integer Id){
        ArrayList<Integer> key = new ArrayList<>();
        for (Companion i: companions) {
            key.clear();
            if(i.id == 0){
                key.add(0);
                key.add(0);
            } else {
                key.add(i.id);
                key.add(Id);
            }
            Collections.sort(key);
            i.setNumberOfMessages((conversations.get(key)).getNumberOfMessages());
        }
    }
}
