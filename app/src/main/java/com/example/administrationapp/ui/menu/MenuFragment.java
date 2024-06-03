package com.example.administrationapp.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.administrationapp.FormPelayananAkademik;
import com.example.administrationapp.R;
import com.example.administrationapp.adapter.BeritaAdapter;
import com.example.administrationapp.adapter.SubmissionListAdapter;
import com.example.administrationapp.api.FormAPI;
import com.example.administrationapp.apimodel.FormPelayananModel;
import com.example.administrationapp.apimodel.SubmissionListModel;
import com.example.administrationapp.apimodel.session.SessionAPIModel;
import com.example.administrationapp.app.ClientAPI;
import com.example.administrationapp.app.Feature;
import com.example.administrationapp.databinding.FragmentMenuBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuFragment extends Fragment {
    private FragmentMenuBinding binding;
    private CardView ContantPerson,IsiForm;
    SwipeRefreshLayout refreshArea;
    SharedPreferences SP;
    Retrofit SubmissionAPI;
    ClientAPI clientAPI;
    RecyclerView Rv;
    RecyclerView.LayoutManager RvManager;
    RecyclerView.Adapter RvAdapter;
    Feature appFeature ;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMenuBinding.inflate(inflater,container,false);
        View root = binding.getRoot();

        appFeature = new Feature();
        clientAPI = new ClientAPI();
        SP = getContext().getSharedPreferences("mcr", Context.MODE_PRIVATE);

        ContantPerson = binding.MenuContactPerson;
        IsiForm = binding.MenuIsiForm;
        Rv = binding.MenuSubmissionList;
        refreshArea = binding.MenuRefreshArea;
        RvManager = new LinearLayoutManager(getContext());

        System.gc();
        addEvent();
        fetchSubmissionList();
        return root;
    }

    private void addEvent(){
        if(ContantPerson != null){
            ContantPerson.setOnClickListener(view -> {
                ContantPerson.setBackgroundTintList(getResources().getColorStateList(R.color.dark_oppacity_25));
            });
        }

        if(IsiForm != null){
            IsiForm.setOnClickListener(view ->
                    startActivity(new Intent(getContext(), FormPelayananAkademik.class))
            );
        }

        refreshArea.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchSubmissionList();
            }
        });
    }

    private void fetchSubmissionList(){
        SubmissionAPI = clientAPI.getClientAPI();
        FormAPI Interface = SubmissionAPI.create(FormAPI.class);
        Call<ArrayList<SubmissionListModel>> resData = Interface.getSubmissionList(
                new SessionAPIModel(SP.getString("user_id",""),SP.getString("token",""))
        );

        resData.enqueue(new Callback<ArrayList<SubmissionListModel>>() {
            @Override
            public void onResponse(Call<ArrayList<SubmissionListModel>> call, Response<ArrayList<SubmissionListModel>> response) {
                if(response.isSuccessful()){
                    RvAdapter = new SubmissionListAdapter(response.body());
                    Rv.setLayoutManager(RvManager);
                    Rv.setAdapter(RvAdapter);
                }else{
                    Toast.makeText(getContext(), response.message(), Toast.LENGTH_SHORT).show();
                }

                refreshArea.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<SubmissionListModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                refreshArea.setRefreshing(false);

            }
        });
    }
}
