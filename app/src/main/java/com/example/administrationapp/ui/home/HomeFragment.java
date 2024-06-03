package com.example.administrationapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrationapp.FormPelayananAkademik;
import com.example.administrationapp.LoginForm;
import com.example.administrationapp.ProfileSet;
import com.example.administrationapp.ProfileSetSecurity;
import com.example.administrationapp.adapter.PengumumanAdapter;
import com.example.administrationapp.api.PengumumanAPI;
import com.example.administrationapp.api.SessionAPI;
import com.example.administrationapp.apimodel.PengumumanAPIModel;
import com.example.administrationapp.apimodel.session.SessionAPIModel;
import com.example.administrationapp.app.ClientAPI;
import com.example.administrationapp.app.Feature;
import com.example.administrationapp.databinding.FragmentHomeBinding;
import com.example.administrationapp.model.PengumumanModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private TextView MarqueeArea,UsernameLabel;
    private RecyclerView RV;
    private ClientAPI ClientAPIs;
    private RecyclerView.Adapter RV_Adapter;
    private RecyclerView.LayoutManager RV_Manager;
    private ArrayList<PengumumanModel> pdata ;
    private SharedPreferences SP;
    private String username,user_id;
    private View parentView;
    private Feature appfeature;
    private CircleImageView formSc,profileSc,logoutSc,secureSc;
    CircleImageView ProfilePicture;
    Retrofit logoutRetro;
    ClientAPI API ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        appfeature = new Feature();
        API = new ClientAPI();

        //Deklarasi
        MarqueeArea = binding.HomeMarqueArea;
        parentView = binding.FragmentHomeParentView;
        RV = binding.HomePengumumanArea;
        ProfilePicture = binding.HomeProfilePicture;
        formSc = binding.shortcutForm;
        profileSc = binding.shortcutProfile;
        logoutSc = binding.shortcutLogout;
        secureSc = binding.shortcutSecurity;
        ClientAPIs = new ClientAPI();
        pdata = new ArrayList<>();
        RV_Manager = new LinearLayoutManager(getContext());
        UsernameLabel = binding.HomeMenuUsernameLabel;
        SP = this.getContext().getSharedPreferences("mcr", Context.MODE_PRIVATE);

        //Code
        MarqueeArea.setSelected(true);

        if(!SP.getString("nama","").equals("")){
            username = SP.getString("nama","");
        }else{
            username = "user";
        }
        UsernameLabel.setText(username);

        if(!SP.getString("user_id","").isEmpty()){
            user_id = SP.getString("user_id","");
        }else{
            user_id = "";
        }

        appfeature.loadProfilePicture(user_id,ProfilePicture);
        getAPIData();
        addEvent();
        return root;
    }

    public void getAPIData(){
        Retrofit retrofit = ClientAPIs.getClientAPI();
        PengumumanAPI API = retrofit.create(PengumumanAPI.class);
        Call<List<PengumumanAPIModel>> resdata = API.getResData();

        resdata.enqueue(new Callback<List<PengumumanAPIModel>>() {
            @Override
            public void onResponse(Call<List<PengumumanAPIModel>> call, Response<List<PengumumanAPIModel>> response) {
                if(!response.isSuccessful()){
                    appfeature.showSnackbar(getContext(),parentView,"Gagal mendapatkan data : " + response.message().toString());
                }else{
                    List<PengumumanAPIModel> responsedata = response.body();
                    for(PengumumanAPIModel data : responsedata){
                        pdata.add(new PengumumanModel(data.getTitle(),"\t\t"+data.getContent(),data.getDatetime()));
                    }

                    RV_Adapter = new PengumumanAdapter(pdata);
                    if(RV_Manager != null){
                        RV_Manager = new LinearLayoutManager(getContext());
                    }

                    if(RV != null){
                        RV.setAdapter(RV_Adapter);
                        RV.setLayoutManager(RV_Manager);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PengumumanAPIModel>> call, Throwable t) {
                if(getContext() != null){
                    appfeature.showSnackbar(getContext(),parentView,"Tidak dapat terhubung ke server !");
                }
            }
        });
    }

    private void addEvent(){
        profileSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getContext() != null){
                    Intent PS = new Intent(getContext(), ProfileSet.class);
                    getContext().startActivity(PS);
                }
            }
        });
        formSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getContext() != null){
                    startActivity(new Intent(getContext(), FormPelayananAkademik.class));
                }
            }
        });
        logoutSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getContext() != null){
                    SessionAPIModel postData = new SessionAPIModel(user_id,SP.getString("token",""));
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
                        }

                        @Override
                        public void onFailure(Call<SessionAPIModel> call, Throwable t) {
                            appfeature.showSnackbar(getContext(),parentView,"Tidak dapat terhubung ke server !");
                        }
                    });
                }

            }
        });
        secureSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getContext() != null){
                    startActivity(new Intent(getContext(), ProfileSetSecurity.class));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}