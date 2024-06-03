package com.example.administrationapp.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.administrationapp.adapter.BeritaAdapter;
import com.example.administrationapp.api.PengumumanAPI;
import com.example.administrationapp.apimodel.BeritaModel;
import com.example.administrationapp.app.ClientAPI;
import com.example.administrationapp.app.Feature;
import com.example.administrationapp.databinding.FragmentNewsBinding;
import com.example.administrationapp.databinding.FragmentNewsBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsFragment extends Fragment {

    private NewsViewModel newsViewModel;
    private FragmentNewsBinding binding;
    RecyclerView RV;
    RelativeLayout ParentView;
    Retrofit beritaAPI;
    ClientAPI clientAPI;
    Feature appFeature;
    RecyclerView.Adapter RvAdapter;
    RecyclerView.LayoutManager RvManager;
    SwipeRefreshLayout refreshArea;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        newsViewModel =
                new ViewModelProvider(this).get(NewsViewModel.class);

        binding = FragmentNewsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        clientAPI = new ClientAPI();
        appFeature = new Feature();
        RV = binding.BeritaRV;
        ParentView = binding.BeritaParentView;
        refreshArea = binding.BeritaRefreshArea;
        RvManager = new LinearLayoutManager(getContext());

        getBeritaData();
        addEvent();
        return root;
    }

    private void getBeritaData(){
        beritaAPI = clientAPI.getClientAPI();
        PengumumanAPI beritaInterface = beritaAPI.create(PengumumanAPI.class);
        Call<ArrayList<BeritaModel>> resData = beritaInterface.getBerita();

        resData.enqueue(new Callback<ArrayList<BeritaModel>>() {
            @Override
            public void onResponse(Call<ArrayList<BeritaModel>> call, Response<ArrayList<BeritaModel>> response) {
                if(response.isSuccessful()){
                    RvAdapter = new BeritaAdapter(response.body());
                    RV.setAdapter(RvAdapter);
                    RV.setLayoutManager(RvManager);
                }else{
                    appFeature.showSnackbar(getContext(),ParentView,"Internal Server Error : \n" +
                            response.message().toString());
                }
                refreshArea.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ArrayList<BeritaModel>> call, Throwable t) {
                appFeature.showSnackbar(getContext(),ParentView,"Gagal Mendapatkan Data Berita !");
                refreshArea.setRefreshing(false);
            }
        });
    }

    private void addEvent(){
        refreshArea.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getBeritaData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}