package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Model.Model_Class_Add_Currency;
import com.crypto.croytowallet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Add_Currency_Adapter extends RecyclerView.Adapter<Add_Currency_Adapter.Myviewholder> {

    ArrayList<Model_Class_Add_Currency> currency;

    public Add_Currency_Adapter() {
    }

    public Add_Currency_Adapter(ArrayList<Model_Class_Add_Currency> currency) {
        this.currency = currency;

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

}
