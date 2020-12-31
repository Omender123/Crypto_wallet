package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Model.TransactionHistoryModel;
import com.crypto.croytowallet.R;

import java.util.ArrayList;

public class Transaaction_history_adapter extends RecyclerView.Adapter<Transaaction_history_adapter.myViewHolder> {
ArrayList<TransactionHistoryModel>transactionHistoryModels;
Context context;

    public Transaaction_history_adapter(ArrayList<TransactionHistoryModel> transactionHistoryModels, Context context) {
        this.transactionHistoryModels = transactionHistoryModels;
        this.context = context;
    }

    public Transaaction_history_adapter() {

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_transactio_history,parent,false);

        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
      holder.transaction_status.setText("Paid To "+transactionHistoryModels.get(position).getStatus());
        holder.transaction_amount.setText(transactionHistoryModels.get(position).getAmountTrans());
        holder.transaction_username.setText("Paid From "+transactionHistoryModels.get(position).getUsername());
        holder.transaction_time.setText(transactionHistoryModels.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return transactionHistoryModels.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView transaction_status,transaction_amount,transaction_username,transaction_time;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            transaction_status=itemView.findViewById(R.id.transaction_status);
            transaction_amount=itemView.findViewById(R.id.transaction_amount);
            transaction_username=itemView.findViewById(R.id.transaction_username);
            transaction_time=itemView.findViewById(R.id.transaction_date);
        }
    }
}
