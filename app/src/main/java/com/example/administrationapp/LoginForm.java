package com.example.administrationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrationapp.api.LoginAPI;
import com.example.administrationapp.apimodel.LoginAPIModel;
import com.example.administrationapp.app.connection;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginForm extends AppCompatActivity {
    private TextView Username,RegisterButton;
    private connection con;
    private LoginAPIModel logindata ;
    private TextInputEditText UsernameInput,PasswordInput;
    private Button LoginButton;
    private SharedPreferences.Editor SPed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);
        con = new connection();
        Username = findViewById(R.id.User);
        LoginButton = findViewById(R.id.LoginFormLoginButton);
        RegisterButton = findViewById(R.id.LoginFormRegisterButton);
        UsernameInput = findViewById(R.id.LoginFormUsernameInput);
        PasswordInput = findViewById(R.id.LoginFormPasswordInput);
        SPed = this.getSharedPreferences("mcr",MODE_PRIVATE).edit();
        addEvent();
    }

    private void addEvent(){
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ValidateInput()==true){
                    LoginButton.setEnabled(false);
                    logindata = new LoginAPIModel(UsernameInput.getText().toString(),PasswordInput.getText().toString());

                    Retrofit retrofit = new Retrofit.Builder().baseUrl(con.getUrl()).addConverterFactory(GsonConverterFactory.create()).build();
                    LoginAPI API = retrofit.create(LoginAPI.class);
                    Call<LoginAPIModel> resdata = API.getData(logindata);

                    resdata.enqueue(new Callback<LoginAPIModel>() {
                        @Override
                        public void onResponse(Call<LoginAPIModel> call, Response<LoginAPIModel> response) {
                            if(!response.isSuccessful()){
                                Toast.makeText(LoginForm.this,"not succes : "+response.message().toString(),Toast.LENGTH_SHORT).show();
                            }else{
                                LoginAPIModel datas = response.body();
                                if(datas.getStatus().equals("success")){
                                    Toast.makeText(LoginForm.this,datas.getMessage(),Toast.LENGTH_SHORT).show();
                                    //Username.setText(datas.getUsername());
                                    startActivity(new Intent(LoginForm.this,MainForm.class));
                                    SPed.putString("token", datas.getToken());
                                    SPed.putString("username",datas.getUsername());
                                    SPed.putString("user_id",datas.getUser_id());
                                    SPed.putString("nama",datas.getNama());
                                    SPed.apply();
                                }
                                else{Toast.makeText(LoginForm.this,datas.getMessage(),Toast.LENGTH_SHORT).show();}
                            }
                            LoginButton.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<LoginAPIModel> call, Throwable t) {
                            Toast.makeText(LoginForm.this,t.toString() , Toast.LENGTH_SHORT).show();
                            LoginButton.setEnabled(true);
                        }
                    });
                }

            }
        });

        RegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginForm.this,MainForm.class));
            }
        });
    }

    private boolean ValidateInput(){
        if(UsernameInput.getText().toString().equals("")){
            Toast.makeText(LoginForm.this,"Username tidak boleh kosong !", Toast.LENGTH_SHORT).show();
            UsernameInput.requestFocus();
            return false;
        }else if(PasswordInput.getText().toString().equals("")){
            Toast.makeText(LoginForm.this,"Password tidak boleh kosong !", Toast.LENGTH_SHORT).show();
            PasswordInput.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onBackPressed() {
//        int pid = android.os.Process.myPid();
//        android.os.Process.killProcess(pid);
        finishAffinity();
    }
}