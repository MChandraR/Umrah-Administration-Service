package com.example.administrationapp.app;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.administrationapp.R;
import com.example.administrationapp.api.UserImageAPI;
import com.example.administrationapp.apimodel.UserImageAPIModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Feature {
    Retrofit retrofit;
    ClientAPI API = new ClientAPI();
    connection conn = new connection();

    public void showSnackbar(Context context , View view , String message){
        if(context != null){
            try{
                Snackbar.make(context,view,message,Snackbar.LENGTH_SHORT).
                        setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).
                        setTextColor(context.getResources().getColor(R.color.black)).
                        setBackgroundTint(context.getResources().getColor(R.color.white)).show();
            }catch (Exception e){
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void loadProfilePicture(String user_id, CircleImageView ProfilePicture ){
        retrofit = API.getClientAPI();
        UserImageAPI ImageAPI = retrofit.create(UserImageAPI.class);
        String url = "res/image/profile/" + user_id;

        Call<UserImageAPIModel> resdata = ImageAPI.getUserImage(url);
        resdata.enqueue(new Callback<UserImageAPIModel>() {
            @Override
            public void onResponse(Call<UserImageAPIModel> call, Response<UserImageAPIModel> response) {
                if(response.isSuccessful()){
                    UserImageAPIModel data = response.body();
                    if(data.getStatus().equals("success")){
                        //showMessage(data.getMessage());
                        if(ProfilePicture != null){
                            Picasso.get().load(conn.getRawUrl()+data.getPath()).into(ProfilePicture);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserImageAPIModel> call, Throwable t) {
            }
        });
    }

}
