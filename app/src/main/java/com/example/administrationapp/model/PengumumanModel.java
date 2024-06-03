package com.example.administrationapp.model;

public class PengumumanModel {
    private String title,content,datetime;

    public PengumumanModel(String titles,String contents,String datetimes){
        title = titles;
        content = contents;
        datetime = datetimes;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDatetime() {
        return datetime;
    }
}
