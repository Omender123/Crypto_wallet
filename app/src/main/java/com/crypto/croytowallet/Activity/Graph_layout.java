package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.CoinTransfer.CoinScan;
import com.crypto.croytowallet.CoinTransfer.Received_Coin;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.fragement.Exchange;

import org.eazegraph.lib.charts.ValueLineChart;
import org.eazegraph.lib.models.ValueLinePoint;
import org.eazegraph.lib.models.ValueLineSeries;

public class Graph_layout extends AppCompatActivity implements View.OnClickListener {
    ValueLineChart mCubicValueLineChart;
    TextView swap;
    private Exchange exchange;
    int position;
    ImageView back,received,send;

    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_layout);
         mCubicValueLineChart = (ValueLineChart) findViewById(R.id.cubiclinechart);
         swap =findViewById(R.id.swap_btc_btn);
         back =findViewById(R.id.back);
          received =findViewById(R.id.receive_coin);

          send=findViewById(R.id.send_coin);
         swap.setOnClickListener(this);
         back.setOnClickListener(this);
        received.setOnClickListener(this);
        send.setOnClickListener(this);

        preferences=getApplicationContext().getSharedPreferences("symbols", Context.MODE_PRIVATE);
        position = preferences.getInt("position", -1);

      /*  Bundle bundle = getIntent().getExtras();
        position=bundle.getInt("position");
*/

        ValueLineSeries series = new ValueLineSeries();
        series.addPoint(new ValueLinePoint("Jan", 2.4f));
        series.addPoint(new ValueLinePoint("Feb", 3.4f));
        series.addPoint(new ValueLinePoint("Mar", 0.4f));
        series.addPoint(new ValueLinePoint("Apr", 1.2f));
        series.addPoint(new ValueLinePoint("Mai", 2.6f));
        series.addPoint(new ValueLinePoint("Jun", 1.0f));
        series.addPoint(new ValueLinePoint("Jul", 3.5f));
        series.addPoint(new ValueLinePoint("Aug", 2.4f));
        series.addPoint(new ValueLinePoint("Sep", 2.4f));
        series.addPoint(new ValueLinePoint("Oct", 3.4f));
        series.addPoint(new ValueLinePoint("Nov", 5.4f));
        series.addPoint(new ValueLinePoint("Dec", 1.3f));

        mCubicValueLineChart.addSeries(series);
        mCubicValueLineChart.startAnimation();
        //   series.setColor(R.drawable.gradient);
        series.setColor(Color.rgb(228, 159, 14));

        mCubicValueLineChart.setBackgroundColor(Color.rgb(244, 198, 30));

        switch (position){
            case 0:

            break;

            case 1:

                break;

            case 2:

                break;

            case 3:

                break;

            case 4:

                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(Graph_layout.this, MainActivity.class));
                break;

            case R.id.receive_coin:
               Intent intent=new Intent(getApplicationContext(), Received_Coin.class);
               intent.putExtra("position",position);
               startActivity(intent);
                break;

            case R.id.send_coin:
                Intent intent1=new Intent(getApplicationContext(), CoinScan.class);
                intent1.putExtra("position",position);
                startActivity(intent1);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
    }
}