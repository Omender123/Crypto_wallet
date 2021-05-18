package com.crypto.croytowallet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.R;

public class Product_HistoryAdapter extends RecyclerView.Adapter<Product_HistoryAdapter.myViewHolder> {

    String [] name;

    public Product_HistoryAdapter(String[] name) {
        this.name = name;
    }

    public Product_HistoryAdapter() {
    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transactio_history,parent,false);

        return new Product_HistoryAdapter.myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.transaction_amount.setText(name[position]);

    }

    @Override
    public int getItemCount() {
        return name.length;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView transaction_status,transaction_amount,transaction_username,transaction_time,transaction_date;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            transaction_status=itemView.findViewById(R.id.transaction_status);
            transaction_amount=itemView.findViewById(R.id.transaction_amount);
            transaction_username=itemView.findViewById(R.id.transaction_username);
            transaction_date=itemView.findViewById(R.id.transaction_date);
            transaction_time = itemView.findViewById(R.id.transaction_Time);




        }
    }

}
