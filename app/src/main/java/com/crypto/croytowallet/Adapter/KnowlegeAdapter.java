package com.crypto.croytowallet.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.R;

public class KnowlegeAdapter extends RecyclerView.Adapter<KnowlegeAdapter.MyViewHlolder> {
    String[] data;

    public KnowlegeAdapter(String[] data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHlolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.knowlage_base,parent,false);

        return new MyViewHlolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHlolder holder, int position) {
        holder.showbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.showdata.setVisibility(view.getVisibility());
            }
        });

        holder.showdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.showdata.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class MyViewHlolder extends RecyclerView.ViewHolder {
        TextView showbtn,showdata;
        public MyViewHlolder(@NonNull View itemView) {
            super(itemView);
            showbtn=itemView.findViewById(R.id.ReadMore);
            showdata=itemView.findViewById(R.id.Detail_Knowlege);
        }
    }
}
