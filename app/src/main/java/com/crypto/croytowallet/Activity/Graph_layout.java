package com.crypto.croytowallet.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.Crypto_currencyInfo;
import com.crypto.croytowallet.CoinTransfer.CoinScan;
import com.crypto.croytowallet.CoinTransfer.Received_Coin;
import com.crypto.croytowallet.ImtSmart.ImtSmartVerification;
import com.crypto.croytowallet.ImtSmart.imtSwap;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Updated_data;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitGraph;
import com.crypto.croytowallet.fragement.Exchange;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Graph_layout extends AppCompatActivity implements View.OnClickListener {

    TextView swap,price,balance,coinname,coinsymbols,coinprice,sync,increaseRate,null1;
    LinearLayout h_24,d_7,m_1,m_3,m_6,y_1;
    private Exchange exchange;
    int position;
    String symbol,image,coinName,change,CurrencySymbols,coinId,currency2;
    ImageView back,received,send;
    KProgressHUD progressDialog;
    SharedPreferences preferences,sharedPreferences;;
    private LineChart chart;
    UserData userData;
    CircleImageView circleImageView;
    String balance1,  price1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_layout);
        chart =  findViewById(R.id.cubiclinechart);
         swap =findViewById(R.id.swap_btc_btn);
         back =findViewById(R.id.back);
          received =findViewById(R.id.receive_coin);
          price = findViewById(R.id.price);
          balance =findViewById(R.id.balance);
        coinname= findViewById(R.id.coinName);
        coinsymbols= findViewById(R.id.coinsymbols);
        coinprice= findViewById(R.id.coinPrice);
        circleImageView = findViewById(R.id.coinImage);
        // sync = findViewById(R.id.sync);
        increaseRate  =findViewById(R.id.increaseRate);
        null1  = findViewById(R.id.null1);
        h_24 = findViewById(R.id.h_24);
        d_7 = findViewById(R.id.d_7);
        m_1 = findViewById(R.id.m_1);
        m_3 = findViewById(R.id.m_3);
        m_6 = findViewById(R.id.m_6);
        y_1 = findViewById(R.id.y_1);

          send=findViewById(R.id.send_coin);
         swap.setOnClickListener(this);
         back.setOnClickListener(this);
        received.setOnClickListener(this);
        send.setOnClickListener(this);
        h_24.setOnClickListener(this);
        d_7.setOnClickListener(this);
        m_1.setOnClickListener(this);
        m_3.setOnClickListener(this);
        m_6.setOnClickListener(this);
        y_1.setOnClickListener(this);


        position = Updated_data.getInstans(getApplicationContext()).getUserId();
        price1 =Updated_data.getInstans(getApplicationContext()).getprice();
        symbol = Updated_data.getInstans(getApplicationContext()).getmobile();
        image = Updated_data.getInstans(getApplicationContext()).getImage();
        coinName =Updated_data.getInstans(getApplicationContext()).getUsername();
        change =Updated_data.getInstans(getApplicationContext()).getChange();

      //  Toast.makeText(this, ""+symbol, Toast.LENGTH_SHORT).show();

        sharedPreferences =getApplicationContext().getSharedPreferences("currency",0);

        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");
        coinId = Updated_data.getInstans(getApplicationContext()).getCoinId();
         currency2 =sharedPreferences.getString("currency1","usd");


        //   Log.d("price",getString(price1));

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();






        Picasso.get().load(image).into(circleImageView);
        price.setText(CurrencySymbols+price1);
        coinname.setText(coinName);
        coinsymbols.setText("("+symbol+")");
     //   sync.setText(symbol+" Price");
        increaseRate.setText(change);
        increaseRate.setTextColor(change.contains("-")?
                getApplicationContext().getResources().getColor(R.color.red): getApplicationContext().getResources().getColor(R.color.green)  );

        null1.setTextColor(change.contains("-")?
                getApplicationContext().getResources().getColor(R.color.red): getApplicationContext().getResources().getColor(R.color.green)  );

        if(change.contains("-")){
            increaseRate.setText(change);
        }else{
            increaseRate.setText("+"+change);
        }
        getBalance();
        getGraph();



    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(Graph_layout.this, MainActivity.class));
                break;

            case R.id.receive_coin:
               Intent intent=new Intent(getApplicationContext(), Received_Coin.class);
              // intent.putExtra("position",position);
               startActivity(intent);
                break;

            case R.id.send_coin:
                Intent intent1=new Intent(getApplicationContext(), CoinScan.class);
              //  intent1.putExtra("position",position);
                startActivity(intent1);
                break;
            case R.id.swap_btc_btn:
                Intent intent2=new Intent(getApplicationContext(), imtSwap.class);
                startActivity(intent2);
                break;

            case R.id.h_24:
                h_24.setBackgroundColor(getResources().getColor(R.color.purple_500));
                d_7.setBackgroundColor(getResources().getColor(R.color.white));
                m_1.setBackgroundColor(getResources().getColor(R.color.white));
                m_3.setBackgroundColor(getResources().getColor(R.color.white));
                m_6.setBackgroundColor(getResources().getColor(R.color.white));
                y_1.setBackgroundColor(getResources().getColor(R.color.white));
                getGraph();

                break;

            case R.id.d_7:
                h_24.setBackgroundColor(getResources().getColor(R.color.white));
                d_7.setBackgroundColor(getResources().getColor(R.color.purple_500));
                m_1.setBackgroundColor(getResources().getColor(R.color.white));
                m_3.setBackgroundColor(getResources().getColor(R.color.white));
                m_6.setBackgroundColor(getResources().getColor(R.color.white));
                y_1.setBackgroundColor(getResources().getColor(R.color.white));
                getGraphData(coinId,currency2,"7","daily");
                break;

            case R.id.m_1:
                h_24.setBackgroundColor(getResources().getColor(R.color.white));
                d_7.setBackgroundColor(getResources().getColor(R.color.white));
                m_1.setBackgroundColor(getResources().getColor(R.color.purple_500));
                m_3.setBackgroundColor(getResources().getColor(R.color.white));
                m_6.setBackgroundColor(getResources().getColor(R.color.white));
                y_1.setBackgroundColor(getResources().getColor(R.color.white));
                getGraphData(coinId,currency2,"30","daily");

                break;

            case R.id.m_3:
                h_24.setBackgroundColor(getResources().getColor(R.color.white));
                d_7.setBackgroundColor(getResources().getColor(R.color.white));
                m_1.setBackgroundColor(getResources().getColor(R.color.white));
                m_3.setBackgroundColor(getResources().getColor(R.color.purple_500));
                m_6.setBackgroundColor(getResources().getColor(R.color.white));
                y_1.setBackgroundColor(getResources().getColor(R.color.white));
                getGraphData(coinId,currency2,"90","daily");

                break;

            case R.id.m_6:
                h_24.setBackgroundColor(getResources().getColor(R.color.white));
                d_7.setBackgroundColor(getResources().getColor(R.color.white));
                m_1.setBackgroundColor(getResources().getColor(R.color.white));
                m_3.setBackgroundColor(getResources().getColor(R.color.white));
                m_6.setBackgroundColor(getResources().getColor(R.color.purple_500));
                y_1.setBackgroundColor(getResources().getColor(R.color.white));
                getGraphData(coinId,currency2,"180","daily");

                break;

            case R.id.y_1:
                h_24.setBackgroundColor(getResources().getColor(R.color.white));
                d_7.setBackgroundColor(getResources().getColor(R.color.white));
                m_1.setBackgroundColor(getResources().getColor(R.color.white));
                m_3.setBackgroundColor(getResources().getColor(R.color.white));
                m_6.setBackgroundColor(getResources().getColor(R.color.white));
                y_1.setBackgroundColor(getResources().getColor(R.color.purple_500));
                getGraphData(coinId,currency2,"365","daily");
                break;


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
    }

    public void getBalance(){

        String token = userData.getToken();
       String  symbols = Updated_data.getInstans(getApplicationContext()).getmobile();
        progressDialog = KProgressHUD.create(Graph_layout.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Balance(token,symbols);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;
              hidepDialog();

                if (response.code()==200){
                    try {
                        s=response.body().string();
/*
                        JSONObject jsonObject = new JSONObject(s);
                        balance1 = jsonObject.getInt("balance");

                        String bal =jsonObject.getString("balance");
                        Toast.makeText(Graph_layout.this, ""+bal, Toast.LENGTH_SHORT).show();
                      //  int price2 = Integer.parseInt(price1);
                        Double balance2 = Double.valueOf(balance1*price1);

                     //   balance.setText(CurrencySymbols+balance2);
                       coinprice.setText(""+bal);*/

                        JSONObject jsonObject = new JSONObject(s);
                        balance1 = jsonObject.getString("balance");


                        double balance2 = Double.parseDouble(balance1);
                        double price = Double.parseDouble(price1);
                        double total = balance2*price;
                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);

                        balance.setText(CurrencySymbols+df.format(total));
                        coinprice.setText(""+df.format(balance2));

                       // Log.d("bal",df.format(total));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Graph_layout.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==401){

                    Snacky.builder()
                            .setActivity(Graph_layout.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(Graph_layout.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void getGraph() {


        Call<ResponseBody>call = RetrofitGraph.getInstance().getApi().getGraphData(coinId,currency2,"1");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;
                if (response.code()==200){
                    ArrayList<Entry> yvalue=new ArrayList<>();
                    yvalue.clear();
                    try {
                        s=response.body().string();

                        JSONObject object = new JSONObject(s);

                        String prices = object.getString("prices");
                        JSONArray jsonArray = new JSONArray(prices);

                        for (int i=0;i<=jsonArray.length();i++){

                            String peice1 = jsonArray.getString(i);

                            JSONArray jsonArray1 = new JSONArray(peice1);
                            for (int j=0; j<=jsonArray1.length();j++){
                                String lowPrices = jsonArray1.getString(1);

                                Float aFloat = Float.parseFloat(lowPrices);
                                yvalue.add(new Entry(i,aFloat));
                            }

                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    chart.setDragEnabled(true);
                    chart.setScaleEnabled(true);
                    chart.setPinchZoom(true);
                    /* create marker to display box when values are selected */
                    MyMarkerView mv = new MyMarkerView(Graph_layout.this, R.layout.custom_marker_view);

                    // Set the marker to the chart
                    mv.setChartView(chart);
                    chart.setMarker(mv);
                    chart.animateXY(1000,1000);
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


                    LineDataSet set1=new LineDataSet(yvalue,"");
                    set1.setFillAlpha(110);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);
                    LineData data = new LineData(dataSets);
                    chart.setData(data);

                    if (Utils.getSDKInt() >= 18) {
                        Drawable drawable = ContextCompat.getDrawable(Graph_layout.this, R.drawable.gradient1);
                        set1.setFillDrawable(drawable);
                    } else {
                        set1.setFillColor(Color.rgb(229, 146, 19));
                    }
                    set1.setMode(LineDataSet.Mode.LINEAR);
                    set1.setLineWidth(2f);
                    set1.setColor(Color.WHITE);
                    set1.setDrawFilled(true);
                    set1.setDrawCircles(false);
                    chart.setBackgroundColor(getResources().getColor(R.color.purple_500));


                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");
                        Snacky.builder()
                                .setActivity(Graph_layout.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(Graph_layout.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    public void getGraphData(String id,String currency,String days,String interval){
        Call<ResponseBody>call = RetrofitGraph.getInstance().getApi().getGraphData1(id,currency,days,interval);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;
                if (response.code()==200){
                    ArrayList<Entry> yvalue=new ArrayList<>();
                    yvalue.clear();
                    try {
                        s=response.body().string();

                        JSONObject object = new JSONObject(s);

                        String prices = object.getString("prices");
                        JSONArray jsonArray = new JSONArray(prices);

                        for (int i=0;i<=jsonArray.length();i++){

                            String peice1 = jsonArray.getString(i);

                            JSONArray jsonArray1 = new JSONArray(peice1);
                            for (int j=0; j<=jsonArray1.length();j++){
                                String lowPrices = jsonArray1.getString(1);

                                Float aFloat = Float.parseFloat(lowPrices);
                                yvalue.add(new Entry(i,aFloat));
                            }

                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    chart.setDragEnabled(true);
                    chart.setScaleEnabled(true);
                    chart.setPinchZoom(true);
                    /* create marker to display box when values are selected */
                    MyMarkerView mv = new MyMarkerView(Graph_layout.this, R.layout.custom_marker_view);

                    // Set the marker to the chart
                    mv.setChartView(chart);
                    chart.setMarker(mv);
                    chart.animateXY(1000,1000);
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


                    LineDataSet set1=new LineDataSet(yvalue,"");
                    set1.setFillAlpha(110);
                    ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                    dataSets.add(set1);
                    LineData data = new LineData(dataSets);
                    chart.setData(data);

                    if (Utils.getSDKInt() >= 18) {
                        Drawable drawable = ContextCompat.getDrawable(Graph_layout.this, R.drawable.gradient1);
                        set1.setFillDrawable(drawable);
                    } else {
                        set1.setFillColor(Color.rgb(229, 146, 19));
                    }
                    set1.setMode(LineDataSet.Mode.LINEAR);
                    set1.setLineWidth(2f);
                    set1.setColor(Color.WHITE);
                    set1.setDrawFilled(true);
                    set1.setDrawCircles(false);
                    chart.setBackgroundColor(getResources().getColor(R.color.purple_500));


                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");
                        Snacky.builder()
                                .setActivity(Graph_layout.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(Graph_layout.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }
}