package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Model.CurrencyModel;
import com.crypto.croytowallet.Model.Model_Class_Add_Currency;
import com.crypto.croytowallet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Add_Currency_Adapter extends RecyclerView.Adapter<Add_Currency_Adapter.Myviewholder> implements Filterable {
    ArrayList<Model_Class_Add_Currency> currency;
    private List<Model_Class_Add_Currency> exampleListFull;
    public Add_Currency_Adapter() {
    }

    public Add_Currency_Adapter(ArrayList<Model_Class_Add_Currency> currency) {
        this.currency = currency;
        exampleListFull = new ArrayList<>(currency);
    }


    @NonNull
    @Override
    public Myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.currencylayout, parent, false);

        return new Myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myviewholder holder, int position) {
        // String Icom = currency.get(position).getImage();
        String title = currency.get(position).getCurrency_Title();
        String des = currency.get(position).getTitle_Des();

        holder.Title.setText(title);
        holder.Descrition.setText(des);
        //  Picasso.get().load(Icom).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return currency.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        TextView Title, Descrition;
        //      ImageView imageView;

        public Myviewholder(@NonNull View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.Currency_Title);
            Descrition = itemView.findViewById(R.id.Currency_Des);
            //    imageView = itemView.findViewById(R.id.Image_cuurency);
        }
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Model_Class_Add_Currency> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(exampleListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Model_Class_Add_Currency item : exampleListFull) {
                    if (item.getCurrency_Title().toLowerCase().contains(filterPattern)|| item.getTitle_Des().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            currency.clear();
            currency.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
