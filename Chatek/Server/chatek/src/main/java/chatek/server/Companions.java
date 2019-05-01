package chatek.server;

import java.util.ArrayList;

public class Companions {
    public ArrayList<Companion> companions = new ArrayList<>();
    synchronized public void Add(Companion companion){
        companions.add(companion);
    }
    public int getSize(){return companions.size();}
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
}
