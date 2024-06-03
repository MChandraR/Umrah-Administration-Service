package com.example.administrationapp.ui.messagearea;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrationapp.adapter.MessageListAdapter;
import com.example.administrationapp.api.MessageListAPI;
import com.example.administrationapp.apimodel.MessageListAPIModel;
import com.example.administrationapp.app.ClientAPI;
import com.example.administrationapp.app.Core;
import com.example.administrationapp.app.Feature;
import com.example.administrationapp.databinding.MessageAreaFragmentBinding;
import com.example.administrationapp.model.MessageListModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MessageAreaFragment extends Fragment {
    public MessageAreaFragmentBinding binding;
    private RecyclerView Rv;
    private RecyclerView.Adapter Rv_Adapter;
    private RecyclerView.LayoutManager Rv_Manager;
    private Core appCore;
    private ClientAPI clientAPI;
    private String user_id;
    private ArrayList<MessageListModel> data;
    private SharedPreferences SP;
    private Feature appFeature;
    private RelativeLayout parentView;
    TextInputEditText searchInput;
    Button searchBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MessageAreaFragmentBinding.inflate(inflater,container,false);
        View root = binding.getRoot();


        appCore = new Core();
        appFeature = new Feature();
        clientAPI = new ClientAPI();
        data = new ArrayList<>();
        Rv = binding.MessageAreaRecycleView;
        SP = appCore.getSharedPreference(getContext());

        user_id = SP.getString("user_id","");
        getMessageList();

        parentView = binding.MessageAreaParentView;
        searchBtn = binding.MessageAreaSearchBtn;
        searchInput = binding.MessageAreaInputSearch;

        addEvent();

        return root;
    }

    public void getMessageList(){
        Retrofit MListRetro = clientAPI.getClientAPI();
        MessageListAPI MlistAPI = MListRetro.create(MessageListAPI.class);
        Call<List<MessageListAPIModel>> resdata = MlistAPI.getMessageList("messagelist/" + user_id);

        resdata.enqueue(new Callback<List<MessageListAPIModel>>() {
            @Override
            public void onResponse(Call<List<MessageListAPIModel>> call, Response<List<MessageListAPIModel>> response) {
                if(response.isSuccessful()){
                    List<MessageListAPIModel> datas = response.body();
                    for(MessageListAPIModel adpdata : datas){
                        data.add(new MessageListModel(adpdata.getSender(), adpdata.getLast_message(), adpdata.getTime(),adpdata.getImage()));
                    }

                    loadMessageList(data);
                }else{
                    appFeature.showSnackbar(getContext(),parentView,"Tidak dapat terhubung ke server !");
                }

            }

            @Override
            public void onFailure(Call<List<MessageListAPIModel>> call, Throwable t) {
                Toast.makeText(getContext(), "Error : " + t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadMessageList(ArrayList<MessageListModel> data){
        Rv_Adapter = new MessageListAdapter(data);
        Rv_Manager = new LinearLayoutManager(getContext());
        if(Rv != null){
            Rv.setAdapter(Rv_Adapter);
            Rv.setLayoutManager(Rv_Manager);
        }
    }

    private void addEvent(){
        if(searchBtn!=null){
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(searchInput.getText().equals("")){
                        loadMessageList(data);
                    }else{
                        loadMessageList(searchData(searchInput.getText().toString(),data));
                    }
                }
            });
        }

        if(searchInput!= null){
            searchInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    return;
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    return;
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(searchInput.getText().equals("")){
                        loadMessageList(data);
                    }else{
                        loadMessageList(searchData(searchInput.getText().toString(),data));
                    }
                }
            });
        }
    }

    private ArrayList<MessageListModel> searchData(String key,ArrayList<MessageListModel> data){
        ArrayList<MessageListModel> result = new ArrayList<>();
        for(MessageListModel d : data){
            int len1  = d.getSender().length();
            int len2 = key.length();
            for(int i=0;i<= len1 - len2;i++){
                String param1 = d.getSender().substring(i,i+len2);
                if(param1.equalsIgnoreCase(key)){
                    result.add(d);
                    break;
                }
            }
        }

        return result;
    }
}
