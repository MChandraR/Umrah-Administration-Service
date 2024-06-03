package com.example.administrationapp.api;

import com.example.administrationapp.apimodel.MessageListAPIModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface MessageListAPI {
    @GET()
    Call<List<MessageListAPIModel>> getMessageList(@Url String url);

}
