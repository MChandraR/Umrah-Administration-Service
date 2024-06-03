package com.example.administrationapp.api;

import com.example.administrationapp.apimodel.session.SessionAPIModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface SessionAPI {
    @POST("session")
    Call<SessionAPIModel> checkSession(@Body SessionAPIModel body);

    @PUT("session")
    Call<SessionAPIModel> deleteSession(@Body SessionAPIModel body);
}
