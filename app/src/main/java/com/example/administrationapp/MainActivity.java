package com.example.administrationapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrationapp.api.SessionAPI;
import com.example.administrationapp.apimodel.session.SessionAPIModel;
import com.example.administrationapp.app.ClientAPI;
import com.example.administrationapp.app.Core;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.security.Permission;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences SP;
    private SharedPreferences.Editor SPEditor;
    private ClientAPI clientAPI ;
    private Retrofit sessionRetro;
    private String user_id,token,ip;
    private View parentView;
    private String url,urls;
    private Button OkeBtn;
    private TextInputEditText IpField;
    private LinearLayout MessageArea;
    private Core appCore;
    private Button Logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //View Declaration
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Deklarasi objek
        appCore = new Core();
        SP = appCore.getSharedPreference(MainActivity.this);
        SPEditor = appCore.getSharedPreferenceEditor(MainActivity.this);
        clientAPI = new ClientAPI();
        ip = "";


        //Deklarasi variabel
        user_id = SP.getString("user_id","");
        token = SP.getString("token","");
        ip = SP.getString("ip","");
        parentView = findViewById(R.id.MainActivityParentView);
        OkeBtn = findViewById(R.id.MainActivityOkeBtn);
        IpField = findViewById(R.id.MainActivityIpField);
        MessageArea = findViewById(R.id.MainActivityMessageArea);


//        Handler handler = new Handler(Looper.getMainLooper());
//        Runnable run = new Runnable() {
//            @Override
//            public void run() {
//                Intent act = new Intent(MainActivity.this,MainForm.class);
//                startActivity(act);
//            }
//        };
//        handler.postDelayed(run,3000);
        checkSession();

    }


    private void addEvent(){
        if(Logo != null){
            Logo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MessageArea.setClickable(true);
                    MessageArea.setVisibility(View.VISIBLE);
                }
            });

        }

        if(OkeBtn != null){
            OkeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SPEditor.putString("ip",IpField.getText().toString()).apply();
                    MessageArea.setClickable(false);
                    MessageArea.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public String getUrl(){
        if(ip==null){
            url = "http://192.168.1.7:8000/API/";
        }else{
            url = "http://"+ip+"/API/";
        }

        return url;
    }

    public String getUrls(){
        if(ip==null){
            urls = "http://192.168.1.7:8000";
        }else{
            urls = "http://"+ip;
        }

        return  urls;
    }


    public void checkSession(){
        sessionRetro = clientAPI.getClientAPI();
        SessionAPIModel postData = new SessionAPIModel(user_id,token);
        SessionAPI sAPI = sessionRetro.create(SessionAPI.class);
        Call<SessionAPIModel> resdata = sAPI.checkSession(postData);
        resdata.enqueue(new Callback<SessionAPIModel>() {
            @Override
            public void onResponse(Call<SessionAPIModel> call, Response<SessionAPIModel> response) {
                SessionAPIModel data = response.body();
                if(response.isSuccessful()){
                    if(data.getStatus().equals("success")){
                        Intent act = new Intent(MainActivity.this,MainForm.class);
                        startActivity(act);
                    }else{
                        Intent act = new Intent(MainActivity.this,LoginForm.class);
                        startActivity(act);
                        showMessage("Sesi telah berakhir silahkan login !");
                    }
                }else{
                    Intent act = new Intent(MainActivity.this,MainForm.class);
                    startActivity(act);
                    showMessage("Internal server error !");
                }
            }

            @Override
            public void onFailure(Call<SessionAPIModel> call, Throwable t) {
                Intent act = new Intent(MainActivity.this,MainForm.class);
                startActivity(act);
                showMessage("Tidak dapat terhubung ke server !");
            }
        });
    }

    public void showMessage(String message){
        if(MainActivity.this != null){
            try {
                Snackbar.make(MainActivity.this,parentView,message,Snackbar.LENGTH_SHORT).
                        setBackgroundTint(getResources().getColor(R.color.white)).
                        setActionTextColor(getResources().getColor(R.color.black)).
                        setTextColor(getResources().getColor(R.color.black)).
                        setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).
                        show();
            }catch (Exception e){
                Toast.makeText(MainActivity.this,  message, Toast.LENGTH_SHORT).show();
            }
        }
    }
}