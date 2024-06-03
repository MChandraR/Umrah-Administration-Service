package com.example.administrationapp.api;

import com.example.administrationapp.apimodel.BeritaModel;
import com.example.administrationapp.apimodel.PengumumanAPIModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PengumumanAPI {
    @GET("getannouncement")
    Call<List<PengumumanAPIModel>> getResData();

    @GET("getberita")
    Call<ArrayList<BeritaModel>> getBerita();
}
