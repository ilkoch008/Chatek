package com.example.chatek;

public class Message {
    public   String message = " ";
    public   String owner = " ";
    public   String time = " ";
    public   Integer id;
    public static class Builder{
        private  String message = " ";
        private  String owner = " ";
        private  String time = " ";
        private  Integer id;
        public Builder messageIs(String message){this.message = message; return this;}
        public Builder ownerIs(String owner){this.owner = owner;return this;}
        public Builder timeIs(String time){this.time = time;return this;}
        public Builder idIs(Integer id){this.id = id;return this;}
        public Message build(){return new Message(this);}

    }

    private Message(Builder builder){
        message = builder.message;
        owner = builder.owner;
        time = builder.time;
        id = builder.id;
    }
}