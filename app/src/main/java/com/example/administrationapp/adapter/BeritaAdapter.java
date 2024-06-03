package com.example.administrationapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrationapp.R;
import com.example.administrationapp.apimodel.BeritaModel;
import com.example.administrationapp.app.connection;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BeritaAdapter extends RecyclerView.Adapter<BeritaAdapter.BeritaHolder> {
    ArrayList<BeritaModel> data = new ArrayList<>();
    connection Conn = new connection();

    public BeritaAdapter(ArrayList<BeritaModel> resData){
        data = resData;
    }

    @NonNull
    @Override
    public BeritaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BeritaHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.newscardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BeritaHolder holder, int position) {
        BeritaModel current = data.get(position);
        holder.Judul.setText(current.getJudul());
        holder.Isi.setText(current.getIsi());
        holder.Tgl.setText(current.getTanggal().substring(0,10)+" "+current.getTanggal().substring(11,19));
        Picasso.get().load(Conn.getRawUrl()+"/storage/img/berita/"+String.valueOf(current.getId_berita())+".png").memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.Gambar);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class BeritaHolder extends  RecyclerView.ViewHolder{
        private TextView Judul,Isi,Tgl;
        private ImageView Gambar;
        public BeritaHolder(@NonNull View itemView) {
            super(itemView);
            Judul =itemView.findViewById(R.id.BeritaJudulField);
            Isi =itemView.findViewById(R.id.BeritaIsiField);
            Tgl =itemView.findViewById(R.id.BeritaTglField);
            Gambar = itemView.findViewById(R.id.BeritaImageField);
        }
    }
}
