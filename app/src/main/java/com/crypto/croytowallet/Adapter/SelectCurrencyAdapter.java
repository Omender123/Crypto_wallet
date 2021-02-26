package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.CurrencyModel;
import com.crypto.croytowallet.R;

import java.util.ArrayList;
import java.util.List;

public class SelectCurrencyAdapter extends RecyclerView.Adapter<SelectCurrencyAdapter.Myviewholder> {
ArrayList<CurrencyModel>currencyModels;
Context context;
    private HistoryClickLister historyClickLister;
    List<CurrencyModel> mDataFiltered ;

    SharedPreferences sharedPreferences = null;

    public SelectCurrencyAdapter(ArrayList<CurrencyModel> currencyModels, Context contex, HistoryClickLister historyClickLister) {
        this.currencyModels = currencyModels;
        this.context = context;
        this.historyClickLister=historyClickLister;
        this.mDataFiltered = currencyModels;
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

   /* @Override
    public Filter getFilter() {

        return  new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String Key = constraint.toString();
                if (Key.isEmpty()) {
                    mDataFiltered = currencyModels ;
                }
                else {
                    List<CurrencyModel> lstFiltered = new ArrayList<>();
                    for (CurrencyModel row : currencyModels) {

                        if (row.getCurrency().toLowerCase().contains(Key.toLowerCase()) || row.getCountryName().toLowerCase().contains(Key.toLowerCase())){

                            lstFiltered.add(row);
                        }

                    }

                    mDataFiltered = lstFiltered;

                }


                FilterResults filterResults = new FilterResults();
                filterResults.values= mDataFiltered;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataFiltered = (List<CurrencyModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    */

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

