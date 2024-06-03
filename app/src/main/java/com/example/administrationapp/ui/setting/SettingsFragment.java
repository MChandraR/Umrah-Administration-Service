package com.example.administrationapp.ui.setting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.administrationapp.LoginForm;
import com.example.administrationapp.ProfileSet;
import com.example.administrationapp.ProfileSetSecurity;
import com.example.administrationapp.api.SessionAPI;
import com.example.administrationapp.api.UserImageAPI;
import com.example.administrationapp.apimodel.UserImageAPIModel;
import com.example.administrationapp.apimodel.session.SessionAPIModel;
import com.example.administrationapp.app.ClientAPI;
import com.example.administrationapp.app.Feature;
import com.example.administrationapp.app.connection;
import com.example.administrationapp.databinding.FragmentSettingsBinding;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private SharedPreferences SP;
    private TextView namaUser;
    private String token,user_id,nama;
    private LinearLayout ProfileSetMenu,SecurityMenu;
    private Retrofit retrofit,logoutRetro;
    private ClientAPI API;
    private connection conn;
    private CircleImageView ProfilePicture;
    private Button logoutBtn;
    private View parentView;
    private Feature appfeature;
    SwipeRefreshLayout refreshArea;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        SP = this.getContext().getSharedPreferences("mcr", Context.MODE_PRIVATE);
        token = SP.getString("token","");
        user_id = SP.getString("user_id","");
        nama = SP.getString("nama","");

        appfeature = new Feature();

        //Toast.makeText(this.getContext(),token , Toast.LENGTH_SHORT).show();
        API = new ClientAPI();
        conn = new connection();

        ProfileSetMenu = binding.SettingsProfileSet;
        SecurityMenu = binding.SettingsSecurityMenu;
        ProfilePicture = binding.SettingsProfilePictures;
        logoutBtn = binding.SettingsLogout;
        namaUser = binding.SettingsNamaUser;
        refreshArea = binding.SettingsProfilePicture;
        parentView = binding.SettingsFragmentParentView;
        namaUser.setText(nama);

        addEvent();
        getUserImage();

        View root = binding.getRoot();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void getUserImage(){
        retrofit = API.getClientAPI();
        UserImageAPI ImageAPI = retrofit.create(UserImageAPI.class);
        String url = "res/image/profile/" + user_id;

        //Picasso.get().load("http://192.168.1.7:8000///storage//img//users//profile//U000000001.png").into(ProfilePicture);

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
                    }else{
                        showMessage(data.getMessage());
                    }
                }
                refreshArea.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<UserImageAPIModel> call, Throwable t) {
//                if(getContext() != null){
//                    Toast.makeText(getContext(),"Gagal mengambil gambar profile !",Toast.LENGTH_SHORT).show();
//                }
                refreshArea.setRefreshing(false);
            }
        });
    }

    private void addEvent(){
        ProfileSetMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getContext() != null){
                    Intent PS = new Intent(getContext(), ProfileSet.class);
                    getContext().startActivity(PS);
                }
            }
        });

        SecurityMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PS = new Intent(getContext(), ProfileSetSecurity.class);
                getContext().startActivity(PS);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutBtn.setEnabled(false);
                logOut();
            }
        });

        refreshArea.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserImage();
            }
        });
    }

    public void logOut(){
        SessionAPIModel postData = new SessionAPIModel(user_id,token);
        logoutRetro = API.getClientAPI();
        SessionAPI sAPI = logoutRetro.create(SessionAPI.class);
        Call<SessionAPIModel> resdata = sAPI.deleteSession(postData);

        resdata.enqueue(new Callback<SessionAPIModel>() {
            @Override
            public void onResponse(Call<SessionAPIModel> call, Response<SessionAPIModel> response) {
                SessionAPIModel data = response.body();
                if(response.isSuccessful()){
                    if(data.getStatus().equals("success")){
                        if(getContext() != null){
                            getContext().startActivity(new Intent(getContext(), LoginForm.class));
                        }
                    }else{
                        appfeature.showSnackbar(getContext(),parentView,data.getMessage());
                    }
                }
                logoutBtn.setEnabled(true);
            }

            @Override
            public void onFailure(Call<SessionAPIModel> call, Throwable t) {
                logoutBtn.setEnabled(true);
                appfeature.showSnackbar(getContext(),parentView,"Tidak dapat terhubung ke server !");
            }
        });
    }

    private void showMessage(String message){
        if(getContext() != null){
            Toast.makeText(getContext(),message, Toast.LENGTH_SHORT).show();
        }
    }
}