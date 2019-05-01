package chatek.server;

public class Companion {
    public String name = " ";
    public Integer id;
    private boolean availability = true;
    public String password = "";
    public Companion(String name, Integer id, Boolean availability, String password){
        this.name = name;
        this.id = id;
        this.availability = availability;
        this.password = password;
    }

    public void isAvailable(){ availability = true; }
    public void isNotAvailable(){ availability = false; }
    public boolean getAvailability(){ return availability; }
}
