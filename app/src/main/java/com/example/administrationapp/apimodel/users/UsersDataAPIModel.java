package com.example.administrationapp.apimodel.users;

import java.util.ArrayList;

public class UsersDataAPIModel  {
    private String status,message;
    private ArrayList<UsersDetail> data;
    public String token;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<UsersDetail> getDetail() {
        return data;
    }

    public UsersDataAPIModel(String tkn){
        token = tkn;
    }

    public class UsersDetail{
        private String user_id,username,level,nama,email,password;

        public UsersDetail(){

        }

        public String getPassword() {
            return password;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getUsername() {
            return username;
        }

        public String getLevel() {
            return level;
        }

        public String getNama() {
            return nama;
        }

        public String getEmail() {
            return email;
        }
    }
}
