package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Model.CurrencyModel;
import com.crypto.croytowallet.R;

import java.util.ArrayList;

public class SelectCurrencyAdapter extends RecyclerView.Adapter<SelectCurrencyAdapter.Myviewholder> {
ArrayList<CurrencyModel>currencyModels;
Context context;

    public SelectCurrencyAdapter(ArrayList<CurrencyModel> currencyModels, Context context) {
        this.currencyModels = currencyModels;
        this.context = context;
    }

    public SelectCurrencyAdapter() {

    }

    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customcurrencylayout, parent, false);

        return new SelectCurrencyAdapter.Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {

        holder.currency.setText(currencyModels.get(position).getCurrency());
        holder.CountryName.setText(currencyModels.get(position).getCountryName());
    }

    @Override
    public int getItemCount() {
        return currencyModels.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView currency, CountryName;


        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            currency = itemView.findViewById(R.id.currency);
            CountryName = itemView.findViewById(R.id.CountryName);

        }
    }
}
