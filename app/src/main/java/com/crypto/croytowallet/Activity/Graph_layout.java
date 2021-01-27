package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;


public class Graph_layout extends AppCompatActivity implements View.OnClickListener {

    TextView swap;
    private Exchange exchange;
    int position;
    ImageView back,received,send;

    SharedPreferences preferences;
    private LineChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_layout);
        chart =  findViewById(R.id.cubiclinechart);
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


        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        //chart.setBackgroundColor(Color.rgb(244, 198, 30));
        chart.animateXY(2000,2000);
        chart.getXAxis().setDrawGridLines(false);
        chart.getAxisLeft().setDrawGridLinesBehindData(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getDescription().setEnabled(false);
        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawLabels(false);

        YAxis y = chart.getAxisRight();
        y.setEnabled(false);
        y.setDrawAxisLine(false);

        YAxis y1 = chart.getAxisLeft();
        y1.setDrawAxisLine(false);

        XAxis x = chart.getXAxis();
        x.setDrawAxisLine(false);
        x.setDrawGridLines(false);

        ArrayList<Entry> yvalue=new ArrayList<>();
        yvalue.add(new Entry(0,60f));
        yvalue.add(new Entry(1,10f));
        yvalue.add(new Entry(2,30f));
        yvalue.add(new Entry(3,0f));
        yvalue.add(new Entry(4,50f));
        yvalue.add(new Entry(5,10f));

        LineDataSet set1=new LineDataSet(yvalue,"");
        set1.setFillAlpha(110);
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        LineData data = new LineData(dataSets);
        chart.setData(data);

        if (Utils.getSDKInt() >= 18) {
            // drawables only supported on api level 18 and above
            Drawable drawable = ContextCompat.getDrawable(this, R.drawable.gradient1);
            set1.setFillDrawable(drawable);
        } else {
            set1.setFillColor(Color.rgb(229, 146, 19));
        }
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set1.setLineWidth(2f);
//        set1.setFormLineDashEffect(new DashPathEffect(new float[]{1f, 3f}, 1f));
//        set1.setHighlightLineWidth(2);
        //  set1.enableDashedHighlightLine(10f, 5f, 0f);
        set1.setColor(Color.WHITE);
        set1.setDrawFilled(true);
        chart.setBackgroundColor(Color.rgb(243, 193, 24));





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