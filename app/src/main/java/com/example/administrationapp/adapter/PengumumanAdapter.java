package com.example.administrationapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrationapp.R;
import com.example.administrationapp.model.PengumumanModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class PengumumanAdapter extends RecyclerView.Adapter<PengumumanAdapter.PengumumamHolder> {
    private ArrayList<PengumumanModel> resdata ;

    //Ambil data dari API
    public PengumumanAdapter(ArrayList<PengumumanModel> reqdata){
        resdata = reqdata;
    }

    @NonNull
    @Override
    public PengumumamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.pengumuman_list_layout,parent,false);
        PengumumamHolder holder = new PengumumamHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PengumumamHolder holder, int position) {
        PengumumanModel data = resdata.get(position);
        holder.Judul.setText(data.getTitle());
        holder.Isi.setText(data.getContent());
    }

    @Override
    public int getItemCount() {
        return resdata.size();
    }

    public class PengumumamHolder extends RecyclerView.ViewHolder{
        public TextView Judul,Isi;
        public PengumumamHolder(@NonNull View itemView) {
            super(itemView);
            Judul = itemView.findViewById(R.id.pengumuman_list_layout_judul);
            Isi = itemView.findViewById(R.id.pengumuman_list_layout_isi);
        }
    }
}
