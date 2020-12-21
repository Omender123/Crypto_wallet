package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Crypto_currencyInfo extends RecyclerView.Adapter<Crypto_currencyInfo.MyHolder>{
ArrayList<CrptoInfoModel>crptoInfoModels;
Context context;

    public Crypto_currencyInfo(ArrayList<CrptoInfoModel> crptoInfoModels, Context context) {
        this.crptoInfoModels = crptoInfoModels;
        this.context = context;
    }

    public Crypto_currencyInfo() {
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.customdashboard,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        String image=crptoInfoModels.get(position).getImage();
        String iconname= crptoInfoModels.get(position).getName();
        String CurrentPrice= crptoInfoModels.get(position).getCurrentPrice();
        String currencyRate= crptoInfoModels.get(position).getCurrencyRate();
        String id=crptoInfoModels.get(position).getId();


        Picasso.get().load(image).into(holder.imageView);
        holder.name.setText(iconname);
        holder.currencyPrice.setText("$"+CurrentPrice);
        holder.increaseRate.setText(currencyRate);

        holder.increaseRate.setTextColor(crptoInfoModels.get(position).getCurrencyRate().contains("-")?
                context.getResources().getColor(R.color.red): context.getResources().getColor(R.color.green)  );
        holder.percentage.setTextColor(crptoInfoModels.get(position).getCurrencyRate().contains("-")?
                context.getResources().getColor(R.color.red): context.getResources().getColor(R.color.green) );

       if(crptoInfoModels.get(position).getCurrencyRate().contains("-")){
           holder.increaseRate.setText(currencyRate);
       }else{
           holder.increaseRate.setText("+"+currencyRate);
       }
         /*   int rate=Integer.parseInt(currencyRate);
        if(rate<=0) {
            holder.increaseRate.setTextColor(context.getResources().getColor(R.color.green));
        }else{
            holder.increaseRate.setTextColor(context.getResources().getColor(R.color.red));
        }
*/

       // Picasso.get().load(crptoInfoModels.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return crptoInfoModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView increaseRate,name,currencyPrice,percentage;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.coinImage);
            increaseRate=itemView.findViewById(R.id.increaseRate);
            name=itemView.findViewById(R.id.coinname);
            currencyPrice=itemView.findViewById(R.id.coinrate);
            percentage=itemView.findViewById(R.id.null1);
        }
    }
}
