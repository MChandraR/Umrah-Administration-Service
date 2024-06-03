package com.example.administrationapp.model;

public class MessageListModel {
    String sender,last_message,time;
    String image;

    public MessageListModel(String send,String last, String tim,String img){
        sender = send;
        last_message = last;
        time = tim;
        image = img;
    }

    public String getSender() {
        return sender;
    }

    public String getLast_message() {
        return last_message;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return image;
    }
}
