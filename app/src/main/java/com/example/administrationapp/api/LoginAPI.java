package com.example.administrationapp.api;

import com.example.administrationapp.apimodel.LoginAPIModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginAPI {
    @POST("login")
    Call<LoginAPIModel> getData(@Body LoginAPIModel model );
}
