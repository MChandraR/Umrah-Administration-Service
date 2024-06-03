package com.example.administrationapp.apimodel;

import com.google.gson.annotations.SerializedName;

public class LoginAPIModel {
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    private String nama;
    private String status,message;
    private String token;
    private String user_id;

    public LoginAPIModel(String usernam,String pw){
        username = usernam;
        password = pw;
    }

    public String getNama() {
        return nama;
    }

    public String getUser_id() { return user_id; }

    public String getUsername() {
        return username;
    }

    public String getPassword() { return password; }

    public String getStatus() { return status; }

    public String getMessage() { return message; }

    public String getToken() {
        return token;
    }
}
