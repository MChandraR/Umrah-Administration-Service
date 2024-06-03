package com.example.administrationapp.apimodel.users;

import java.io.File;

public class UsersUpdateAPIModel {
    private String token,option,username,nama,password,email;
    private String status,message;
    private File image;

    public UsersUpdateAPIModel(String tkn, String opt, String usern, String name, String pass, String mail){
        token = tkn;
        option = opt;
        username = usern;
        nama = name;
        password = pass;
        email = mail;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
