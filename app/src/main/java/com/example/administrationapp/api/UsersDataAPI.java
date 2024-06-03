package com.example.administrationapp.api;

import com.example.administrationapp.apimodel.users.UsersDataAPIModel;
import com.example.administrationapp.apimodel.users.UsersUpdateAPIModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface UsersDataAPI {
    @POST
    Call<UsersDataAPIModel> getUsersData(@Url String url,@Body UsersDataAPIModel body);

    @PUT
    Call<UsersUpdateAPIModel> updateUsersData(@Url String url , @Body UsersUpdateAPIModel body);

    @Multipart
    @POST("user")
    Call<UsersUpdateAPIModel> updateProfilePicture(@Part MultipartBody.Part image,@Part("user_id") RequestBody body,@Part("token") RequestBody token);

}
