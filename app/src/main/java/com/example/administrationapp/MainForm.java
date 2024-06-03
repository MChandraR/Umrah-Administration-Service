package com.example.administrationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.administrationapp.api.SessionAPI;
import com.example.administrationapp.apimodel.session.SessionAPIModel;
import com.example.administrationapp.app.ClientAPI;
import com.example.administrationapp.ui.news.NewsFragment;
import com.example.administrationapp.ui.home.HomeFragment;
import com.example.administrationapp.ui.menu.MenuFragment;
import com.example.administrationapp.ui.messagearea.MessageAreaFragment;
import com.example.administrationapp.ui.setting.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainForm extends AppCompatActivity {
    private BottomNavigationView BottomNavigation;
    private FrameLayout FragmentArea;
    private ClientAPI clientAPI;
    private Retrofit sessionRetro;
    private String user_id,token;
    private SharedPreferences SP;
    private boolean isActive;
    private View parentView;
    private Fragment homeFragment;
    private MessageAreaFragment messageAreaFragment;
    private NewsFragment newsFragment;
    private SettingsFragment settingsFragment;
    private MenuFragment menuFragment ;
    private Fragment oldfragment;
    private FrameLayout FLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_form);
        //Deklarasi variabel

        FLayout = findViewById(R.id.MainFormFragmentArea);
        messageAreaFragment = new MessageAreaFragment();
        BottomNavigation = findViewById(R.id.MainFormBottomNavigation);
        FragmentArea = findViewById(R.id.MainFormFragmentArea);

        SP = this.getSharedPreferences("mcr",MODE_PRIVATE);
        user_id = SP.getString("user_id","");
        token = SP.getString("token","");
        isActive = true;
        clientAPI = new ClientAPI();
        parentView = findViewById(R.id.MainFormParentView);

        Handler handler = new Handler(Looper.getMainLooper());
        Runnable run = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,5000);
                if(isActive){
                    checkSession();
                }
            }
        };

        handler.postDelayed(run,5000);

        CheckPermission();

        homeFragment = new HomeFragment();
        newsFragment = new NewsFragment();
        settingsFragment = new SettingsFragment();
        menuFragment = new MenuFragment();

        //Setup Form pertama dibuka
        getSupportFragmentManager().beginTransaction().replace(R.id.MainFormFragmentArea,homeFragment).commit();
        oldfragment = homeFragment;
        BottomNavigation.setOnItemSelectedListener(listener);
    }


    private void CheckPermission(){
        String permission1 = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        String permission2 = Manifest.permission.READ_EXTERNAL_STORAGE;
        int isAllowed1 = this.checkCallingPermission(permission1);
        int isAllowed2 = this.checkCallingPermission(permission2);
        if(isAllowed1 != 1){
            ActivityCompat.requestPermissions(MainForm.this,new String[]{permission1},1);
        }
        if(isAllowed1 != 1){
            ActivityCompat.requestPermissions(MainForm.this,new String[]{permission2},2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    private void checkSession(){
        SessionAPIModel postdata = new SessionAPIModel(user_id,token);
        sessionRetro = clientAPI.getClientAPI();
        SessionAPI sAPI = sessionRetro.create(SessionAPI.class);
        Call<SessionAPIModel> res = sAPI.checkSession(postdata);

        res.enqueue(new Callback<SessionAPIModel>() {
            @Override
            public void onResponse(Call<SessionAPIModel> call, Response<SessionAPIModel> response) {
                 SessionAPIModel data = response.body();
                 if(response.isSuccessful()){
                     if(!data.getStatus().equals("success")){
                         showMessage("Session berakhir harap login ulang !");
                         if(MainForm.this != null){
                             MainForm.this.startActivity(new Intent(MainForm.this,LoginForm.class));
                         }
                     }else{
                         //showMessage("Sesi valid !");
                     }
                 }else{
                     showMessage("Internal server error !");
                 }
            }

            @Override
            public void onFailure(Call<SessionAPIModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    private NavigationBarView.OnItemSelectedListener listener = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFrag = null;
            switch (item.getItemId()){
                case R.id.nav_home:
                    selectedFrag = homeFragment;
                    break;
                case R.id.nav_news:
                    selectedFrag = newsFragment;
                    break;
                case R.id.nav_menu:
                    selectedFrag =  menuFragment;
                    break;
                case R.id.nav_message:
                    selectedFrag = messageAreaFragment;
                    break;
                case R.id.nav_settings:
                    selectedFrag = settingsFragment;
                    break;
                default:
                    selectedFrag = homeFragment;
                    break;
            }

            if(selectedFrag != oldfragment){
                oldfragment = selectedFrag;
                getSupportFragmentManager().beginTransaction().replace(R.id.MainFormFragmentArea,selectedFrag).commit();
            }
            return true;
        }
    };

    public void showMessage(String message){
        if(MainForm.this != null){
/*            try {
                Snackbar.make(MainForm.this,parentView,message,Snackbar.LENGTH_SHORT).
                        setBackgroundTint(getResources().getColor(R.color.white)).
                        setActionTextColor(getResources().getColor(R.color.black)).
                        setTextColor(getResources().getColor(R.color.black)).
                        setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE).
                        show();
            }catch (Exception e){

            }*/
            Toast.makeText(MainForm.this,  message, Toast.LENGTH_SHORT).show();
        }
    }
}