package com.example.administrationapp.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    class NewsHolder extends RecyclerView.ViewHolder{

        public NewsHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
