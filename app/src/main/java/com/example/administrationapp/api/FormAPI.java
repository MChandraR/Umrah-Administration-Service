package com.example.administrationapp.api;

import com.example.administrationapp.apimodel.FormPelayananModel;
import com.example.administrationapp.apimodel.SubmissionListModel;
import com.example.administrationapp.apimodel.session.SessionAPIModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface FormAPI {
    @POST("form/submit")
    Call<FormPelayananModel> sendRequest(@Body FormPelayananModel body);

    @POST("form/list")
    Call<ArrayList<SubmissionListModel>> getSubmissionList(@Body SessionAPIModel Body);
}
