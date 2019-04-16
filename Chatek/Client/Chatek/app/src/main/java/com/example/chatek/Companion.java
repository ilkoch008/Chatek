package com.example.chatek;

public class Companion {
    public String name = " ";
    public Integer id;
    private boolean availability = true;
    public Companion(String name, Integer id, Boolean availability){
        this.name = name;
        this.id = id;
        this.availability = availability;
    }
    public void isAvailable(){ availability = true; }
    public void isNotAvailable(){ availability = false; }
    public boolean getAvailability(){ return availability; }
}
