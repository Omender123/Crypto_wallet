package com.crypto.croytowallet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.zip.Inflater;

public class Ticket_Adapter extends RecyclerView.Adapter<Ticket_Adapter.myVeiwholder> {
    String[] data;

    public Ticket_Adapter(String[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public myVeiwholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(com.crypto.croytowallet.R.layout.custom_transactio_history,parent,false);

        return new myVeiwholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myVeiwholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class myVeiwholder extends RecyclerView.ViewHolder {


        public myVeiwholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
