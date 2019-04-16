package chatek.server;

import java.util.ArrayList;

public class Companions {
    public ArrayList<Companion> companions = new ArrayList<>();
    synchronized public void Add(Companion companion){
        companions.add(companion);
    }
}
