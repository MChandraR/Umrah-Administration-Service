package com.example.administrationapp.apimodel.session;

public class SessionAPIModel {
    private String status,message,user_id,token;

    public SessionAPIModel(String uid, String tkn){
        user_id = uid;
        token = tkn;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getToken() {
        return token;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
