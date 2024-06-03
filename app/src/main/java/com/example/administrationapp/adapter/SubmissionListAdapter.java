package com.example.administrationapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrationapp.R;
import com.example.administrationapp.apimodel.FormPelayananModel;
import com.example.administrationapp.apimodel.SubmissionListModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SubmissionListAdapter extends RecyclerView.Adapter<SubmissionListAdapter.SubmissionListHolder> {
    ArrayList<SubmissionListModel> data;

    public SubmissionListAdapter(ArrayList<SubmissionListModel> req){
        data = req;
    }

    @NonNull
    @Override
    public SubmissionListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SubmissionListHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.submission_list_cardview,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SubmissionListHolder holder, int position) {
        SubmissionListModel current = data.get(position);
        holder.JenisPelayanan.setText(current.getContent());
        holder.TglPermohonan.setText("Tanggal Pengajuan : " + current.getCreated_at().substring(0,10));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class SubmissionListHolder extends  RecyclerView.ViewHolder{
        TextView JenisPelayanan,TglPermohonan;
        public SubmissionListHolder(@NonNull View itemView) {
            super(itemView);
            JenisPelayanan = itemView.findViewById(R.id.SubListJenisPermohonan);
            TglPermohonan = itemView.findViewById(R.id.SubListTglPermohonan);
        }
    }
}
