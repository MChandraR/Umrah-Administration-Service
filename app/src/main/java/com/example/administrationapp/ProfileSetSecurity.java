package com.example.administrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.administrationapp.api.UserImageAPI;
import com.example.administrationapp.api.UsersDataAPI;
import com.example.administrationapp.apimodel.UserImageAPIModel;
import com.example.administrationapp.apimodel.users.UsersDataAPIModel;
import com.example.administrationapp.apimodel.users.UsersUpdateAPIModel;
import com.example.administrationapp.app.ClientAPI;
import com.example.administrationapp.app.Core;
import com.example.administrationapp.app.connection;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ProfileSetSecurity extends AppCompatActivity {
    private Button saveBtn,cancelBtn;
    private ImageButton lockBtn;
    private TextInputEditText passwordField,emailField;
    private Retrofit securityRetro,imageRetro,usersRetro;
    private ClientAPI clientAPI;
    private connection conn;
    private String token,user_id,savedpassword;
    private SharedPreferences SP;
    private CircleImageView usersImage;
    private View parentView;
    private Core appCore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_set_security);

        SP = this.getSharedPreferences("mcr",MODE_PRIVATE);
        clientAPI = new ClientAPI();
        conn = new connection();
        appCore = new Core();

        saveBtn = findViewById(R.id.ProfileSecuritySave);
        cancelBtn = findViewById(R.id.ProfileSecurityCancel);
        lockBtn = findViewById(R.id.ProfileSecurityLockBtn);
        passwordField = findViewById(R.id.ProfileSecurityPassword);
        emailField = findViewById(R.id.ProfileSecurityEmail);
        usersImage = findViewById(R.id.ProfileSecurityUserImage);
        parentView = findViewById(R.id.ProfileSecurityMainView);

        user_id = SP.getString("user_id","");
        token = SP.getString("token","");
        savedpassword = "";

        getUsersImage();
        getUsersData();
        addEvent();
    }

    private void getUsersImage(){
        String url = "res/image/profile/" + user_id;
        imageRetro = clientAPI.getClientAPI();
        UserImageAPI imageAPI = imageRetro.create(UserImageAPI.class);
        Call<UserImageAPIModel> resdata = imageAPI.getUserImage(url);

        resdata.enqueue(new Callback<UserImageAPIModel>() {
            @Override
            public void onResponse(Call<UserImageAPIModel> call, Response<UserImageAPIModel> response) {
                UserImageAPIModel data = response.body();
                if(response.isSuccessful()){
                    if(data.getStatus().equals("success")){
                        if(usersImage != null){
                            Picasso.get().load(conn.getRawUrl() + data.getPath()).into(usersImage);
                        }
                    }else{
                        showMessage(data.getMessage());
                    }
                }else{
                    showMessage("Internal server error !");
                }
            }

            @Override
            public void onFailure(Call<UserImageAPIModel> call, Throwable t) {

            }
        });
    }

    private void getUsersData(){
        String url = "user/" + user_id;
        UsersDataAPIModel udata = new UsersDataAPIModel(token);
        usersRetro = clientAPI.getClientAPI();
        UsersDataAPI usersAPI = usersRetro.create(UsersDataAPI.class);
        Call<UsersDataAPIModel> resdata = usersAPI.getUsersData(url,udata);

        resdata.enqueue(new Callback<UsersDataAPIModel>() {
            @Override
            public void onResponse(Call<UsersDataAPIModel> call, Response<UsersDataAPIModel> response) {
                UsersDataAPIModel data = response.body();
                if(response.isSuccessful()){
                    if(data.getStatus().equals("success")){
                        ArrayList<UsersDataAPIModel.UsersDetail> detaildata = data.getDetail();
                        passwordField.setText(detaildata.get(0).getPassword());
                        emailField.setText(detaildata.get(0).getEmail());
                        savedpassword = detaildata.get(0).getPassword();
                        appCore.UpdateUserData(ProfileSetSecurity.this,
                                detaildata.get(0).getUser_id(),
                                detaildata.get(0).getNama(),
                                detaildata.get(0).getEmail());
                    }else{
                        showMessage(data.getMessage());
                    }
                }else{
                    showMessage("Internal server error !");
                }
            }

            @Override
            public void onFailure(Call<UsersDataAPIModel> call, Throwable t) {
                showMessage("Gagal terhubung ke server !");
            }
        });
    }

    private void updateSecurity(){
        String url = "user/" + user_id;
        String password = passwordField.getText().toString();
        String email = emailField.getText().toString();
        UsersUpdateAPIModel postData = new UsersUpdateAPIModel(token,"security","","",password,email);
        securityRetro = clientAPI.getClientAPI();
        UsersDataAPI updateAPI = securityRetro.create(UsersDataAPI.class);
        Call<UsersUpdateAPIModel> resdata = updateAPI.updateUsersData(url,postData);

        resdata.enqueue(new Callback<UsersUpdateAPIModel>() {
            @Override
            public void onResponse(Call<UsersUpdateAPIModel> call, Response<UsersUpdateAPIModel> response) {
                UsersUpdateAPIModel data = response.body();
                if(response.isSuccessful()){
                    if(data.getStatus().equals("success")){
                        showMessage(data.getMessage());
                        getUsersData();
                    }else{
                        showMessage(data.getMessage());
                    }
                }else{
                    showMessage("Internal server error !");
                }
                saveBtn.setEnabled(true);
            }

            @Override
            public void onFailure(Call<UsersUpdateAPIModel> call, Throwable t) {
                showMessage("Gagal terhubung ke server !");
                saveBtn.setEnabled(true);
            }
        });
    }

    private void addEvent(){
        if(saveBtn != null){
            saveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String pass = passwordField.getText().toString();
                    String email = emailField.getText().toString();
                    if(validateInput(pass,email) == true){
                        updateSecurity();
                        saveBtn.setEnabled(false);
                    }else{
                        showMessage("Input tidak boleh kosong !");
                    }
                }
            });
        }

        if(cancelBtn != null){
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ProfileSetSecurity.this.finish();
                }
            });
        }

        if(lockBtn != null){
            lockBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(passwordField.isEnabled()){
                        passwordField.setEnabled(false);
                        passwordField.setText(savedpassword);
                        lockBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_lock_24));
                    }else{
                        passwordField.setEnabled(true);
                        lockBtn.setImageDrawable(getDrawable(R.drawable.ic_baseline_lock_open_24));
                    }
                }
            });
        }
    }

    private void showMessage(String message){
        if(ProfileSetSecurity.this != null){
            try {
                Snackbar.make(ProfileSetSecurity.this,parentView,message,Snackbar.LENGTH_SHORT).
                        setBackgroundTint(getResources().getColor(R.color.white)).
                        setActionTextColor(getResources().getColor(R.color.black)).
                        setTextColor(getResources().getColor(R.color.black)).
                        setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).
                        show();
            }catch (Exception e){
                Toast.makeText(ProfileSetSecurity.this,  message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInput(String pass,String email){
        if(pass.equals("") || email.equals("")){
            return false;
        }else{
            return true;
        }
    }
}