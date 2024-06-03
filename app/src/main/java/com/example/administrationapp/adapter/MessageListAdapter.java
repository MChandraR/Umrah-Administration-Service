package com.example.administrationapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.administrationapp.R;
import com.example.administrationapp.app.connection;
import com.example.administrationapp.model.MessageListModel;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListHolder> {
    public ArrayList<MessageListModel> data;
    private connection conn;

    public MessageListAdapter (ArrayList<MessageListModel> reqdata){
        data = reqdata;
        conn = new connection();
    }

    @NonNull
    @Override
    public MessageListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_list_viewmodel,parent,false);
        return new MessageListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListHolder holder, int position) {
        MessageListModel cdata = data.get(position);
        Picasso.get().load(conn.getRawUrl() + cdata.getImage()).into(holder.ProfilePicture);
        holder.Sender.setText(cdata.getSender());
        holder.LastMessage.setText(cdata.getLast_message());
        holder.LastSent.setText(cdata.getTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MessageListHolder extends RecyclerView.ViewHolder{
        private CircleImageView ProfilePicture;
        private TextView Sender,LastMessage,LastSent;
        public MessageListHolder(@NonNull View itemView) {
            super(itemView);
            ProfilePicture = itemView.findViewById(R.id.MessageListViewProfilePicture);
            Sender = itemView.findViewById(R.id.MessageListViewTitle);
            LastMessage = itemView.findViewById(R.id.MessageListLastMessage);
            LastSent = itemView.findViewById(R.id.MessageListLastSent);
        }
    }
}
