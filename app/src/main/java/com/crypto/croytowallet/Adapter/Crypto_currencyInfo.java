package com.crypto.croytowallet.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crypto.croytowallet.Interface.CryptoClickListner;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Crypto_currencyInfo extends RecyclerView.Adapter<Crypto_currencyInfo.MyHolder>{
ArrayList<CrptoInfoModel>crptoInfoModels;
Context context;
private CryptoClickListner cryptoClickListner;
    public Crypto_currencyInfo(ArrayList<CrptoInfoModel> crptoInfoModels, Context context, CryptoClickListner cryptoClickListner) {
        this.crptoInfoModels = crptoInfoModels;
        this.context = context;
        this.cryptoClickListner= cryptoClickListner;
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
        String highRate= crptoInfoModels.get(position).getCurrencyRate();
        String lowRate= crptoInfoModels.get(position).getCurrencyRate();
        String id=crptoInfoModels.get(position).getId();

      /*  String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May"};
        int[] yAxisData = {30, 10, 30, 10, 50};

        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();


        Line line = new Line(yAxisValues).setColor(Color.parseColor("#4658e0"));

        for (int i = 0; i < axisData.length; i++) {
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }

        for (int i = 0; i < yAxisData.length; i++) {
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }
        List lines = new ArrayList();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        Axis axis = new Axis();
        axis.setValues(axisValues);
        Axis yAxis = new Axis();
        data.setAxisYLeft(yAxis);
        holder.lineChartView.setLineChartData(data);
*/
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

       // Picasso.get().load(crptoInfoModels.get(position).getImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return crptoInfoModels.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
      //  LineChartView lineChartView;
        TextView increaseRate,name,currencyPrice,percentage;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.coinImage);
            increaseRate=itemView.findViewById(R.id.increaseRate);
            name=itemView.findViewById(R.id.coinname);
            currencyPrice=itemView.findViewById(R.id.coinrate);
            percentage=itemView.findViewById(R.id.null1);
           // lineChartView =itemView.findViewById(R.id.chart);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cryptoClickListner.onCryptoItemClickListener(getAdapterPosition());
                }
            });

        }
    }
}
