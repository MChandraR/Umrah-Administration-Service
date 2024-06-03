package com.example.administrationapp.app;

import com.example.administrationapp.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientAPI {
    private connection conn;
    private String url;

    public ClientAPI(){
        conn = new connection();
        url = conn.getUrl();
    }

    public Retrofit getClientAPI(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }




}
