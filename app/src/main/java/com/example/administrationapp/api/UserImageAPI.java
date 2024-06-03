package com.example.administrationapp.api;

import com.example.administrationapp.apimodel.UserImageAPIModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface UserImageAPI {
    @GET
    Call<UserImageAPIModel> getUserImage(@Url String url);
}
