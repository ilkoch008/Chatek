package com.example.chatek;

public class Companion {
    public String name = " ";
    public Integer id;
    private boolean availability = true;

    private Integer oldNumberOfMessages = 0;
    private Integer newNumberOfMessages = 0;

    public Companion(String name, Integer id, Boolean availability){
        this.name = name;
        this.id = id;
        this.availability = availability;
    }

    public Companion(String name, Integer id, Boolean availability, Integer numberOfMessages){
        this.name = name;
        this.id = id;
        this.availability = availability;
        this.newNumberOfMessages = numberOfMessages;
    }

    public Companion(String name, Integer id, Boolean availability, Integer oldNumberOfMessages, Integer newNumberOfMessages){
        this.name = name;
        this.id = id;
        this.availability = availability;
        this.newNumberOfMessages = newNumberOfMessages;
        this.oldNumberOfMessages = oldNumberOfMessages;
    }

    public static class Builder{
        private String name = " ";
        private Integer id;
        private boolean availability = true;
        private Integer oldNumberOfMessages = 0;
        private Integer newNumberOfMessages = 0;

        public Builder nameIs(String name) {this.name = name; return this;}
        public Builder idIs(Integer id) {this.id = id; return this;}
        public Builder availabilityIs(Boolean availability){this.availability = availability; return this;}
        public Builder oldNumOfMessagesIs(Integer oldNumberOfMessages){this.oldNumberOfMessages = oldNumberOfMessages; return this;}
        public Builder newNumOfMessagesIs(Integer newNumberOfMessages){this.newNumberOfMessages = newNumberOfMessages; return this;}

        public Companion build(){return new Companion(
                this.name,
                this.id,
                this.availability,
                this.oldNumberOfMessages,
                this.newNumberOfMessages);}
    }

    public void isAvailable(){ availability = true; }
    public void isNotAvailable(){ availability = false; }
    public boolean getAvailability(){ return availability; }


    public Integer getDiff(){
        return (newNumberOfMessages - oldNumberOfMessages);
    }

    public void setNewNumberOfMessages(Integer newNumberOfMessages){
        this.newNumberOfMessages = newNumberOfMessages;
    }

    public void setOldNumberOfMessages(Integer oldNumberOfMessages){
        this.oldNumberOfMessages = oldNumberOfMessages;
    }

    public Integer getOldNumberOfMessages(){
        return oldNumberOfMessages;
    }

    public void overlook(){
        oldNumberOfMessages = newNumberOfMessages;
    }

}
