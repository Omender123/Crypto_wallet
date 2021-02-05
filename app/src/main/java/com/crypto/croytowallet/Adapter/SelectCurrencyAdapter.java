package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.CurrencyModel;
import com.crypto.croytowallet.R;

import java.util.ArrayList;

public class SelectCurrencyAdapter extends RecyclerView.Adapter<SelectCurrencyAdapter.Myviewholder> {
ArrayList<CurrencyModel>currencyModels;
Context context;
    private HistoryClickLister historyClickLister;
    private int lastSelectedPosition = -1;

    SharedPreferences sharedPreferences = null;

    public SelectCurrencyAdapter(ArrayList<CurrencyModel> currencyModels, Context contex, HistoryClickLister historyClickLister) {
        this.currencyModels = currencyModels;
        this.context = context;
        this.historyClickLister=historyClickLister;
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
        // holder.radioButton.setChecked(lastSelectedPosition == position);

    }

    @Override
    public int getItemCount() {
        return currencyModels.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView currency, CountryName;
        RadioButton radioButton;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            currency = itemView.findViewById(R.id.currency);
            CountryName = itemView.findViewById(R.id.CountryName);
         //   radioButton =itemView.findViewById(R.id.clickRadio);



          /*  radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    lastSelectedPosition = getAdapterPosition();
                   // notifyDataSetChanged();

                    Toast.makeText(SelectCurrencyAdapter.this.context,
                            "Country Name " + CountryName.getText(),
                            Toast.LENGTH_LONG).show();
                }
            });

*/
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    historyClickLister.onHistoryItemClickListener(getAdapterPosition());
                }
            });

        }
        }
    }

