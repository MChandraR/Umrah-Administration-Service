package com.example.administrationapp.app;


import android.content.SharedPreferences;

import com.example.administrationapp.MainActivity;

public class connection {
    String url ,urls;

    public connection(){
        /*if(MainActivity.class != null){
            MainActivity MA = new MainActivity();
            urls = MA.getUrls();
            url = MA.getUrl();
        }else{

        }*/
        urls = "http://192.168.42.113:8000";
        url = "http://192.168.42.113:8000/API/";
    }

    public String getUrl() {
        return url;
    }
    public String getRawUrl() {return urls;}
}
