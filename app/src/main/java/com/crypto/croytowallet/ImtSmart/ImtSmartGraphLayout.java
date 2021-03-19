package com.crypto.croytowallet.ImtSmart;

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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Activity.Graph_layout;
import com.crypto.croytowallet.Activity.MyMarkerView;
import com.crypto.croytowallet.CoinTransfer.CoinScan;
import com.crypto.croytowallet.CoinTransfer.Received_Coin;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitGraph;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImtSmartGraphLayout extends AppCompatActivity implements View.OnClickListener {
    ImageView back,received,send;
    TextView price,balances,coinprice,increaseRate,null1;
    KProgressHUD progressDialog;
    UserData userData;
    private LineChart chart;
    TextView swap;
    String balance1,  price1;
    SharedPreferences sharedPreferences1,sharedPreferences;
    String currency2,CurrencySymbols;
    LinearLayout h_24,d_7,m_1,m_3,m_6,y_1;
    ArrayList<Entry> yvalue ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imt_smart_graph_layout);
        chart =  findViewById(R.id.cubiclinechart);
        swap =findViewById(R.id.swap_btc_btn);
        back =findViewById(R.id.back);
        received =findViewById(R.id.receive_coin);
        price = findViewById(R.id.price);
        balances =findViewById(R.id.balance);
        coinprice= findViewById(R.id.coinPrice);

        increaseRate  =findViewById(R.id.increaseRate);
         null1  = findViewById(R.id.null1);
        h_24 = findViewById(R.id.h_24);
        d_7 = findViewById(R.id.d_7);
        m_1 = findViewById(R.id.m_1);
        m_3 = findViewById(R.id.m_3);
        m_6 = findViewById(R.id.m_6);
        y_1 = findViewById(R.id.y_1);

        yvalue = new ArrayList<>();

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

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        sharedPreferences1=getSharedPreferences("imtInfo", Context.MODE_PRIVATE);

        sharedPreferences =getApplicationContext().getSharedPreferences("currency",0);
        currency2 =sharedPreferences.getString("currency1","usd");
        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");




        price1 = sharedPreferences1.getString("price",null);
        String increaseRate1 = sharedPreferences1.getString("chanage",null);
        increaseRate.setText(increaseRate1);
        price.setText("$"+price1);

        try {
            increaseRate.setTextColor(increaseRate1.contains("-")?
                getApplicationContext().getResources().getColor(R.color.red): getApplicationContext().getResources().getColor(R.color.green)  );

        null1.setTextColor(increaseRate1.contains("-")?
                getApplicationContext().getResources().getColor(R.color.red): getApplicationContext().getResources().getColor(R.color.green)  );

        if(increaseRate1.contains("-")){
          increaseRate.setText(increaseRate1);
        }else{
            increaseRate.setText("+"+increaseRate1);
        }
        }catch (Exception e){

        }
        getBalance();

        GetImtGraph1d("imt",currency2,24);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                startActivity(new Intent(ImtSmartGraphLayout.this, MainActivity.class));
                break;

            case R.id.receive_coin:
                Intent intent=new Intent(getApplicationContext(), ImtSmartRecevied.class);
                startActivity(intent);
                break;

            case R.id.send_coin:
                Intent intent1=new Intent(getApplicationContext(), ImtSmartCoinScan.class);
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
                GetImtGraph1d("imt",currency2,24);
                break;

            case R.id.d_7:
                h_24.setBackgroundColor(getResources().getColor(R.color.white));
                d_7.setBackgroundColor(getResources().getColor(R.color.purple_500));
                m_1.setBackgroundColor(getResources().getColor(R.color.white));
                m_3.setBackgroundColor(getResources().getColor(R.color.white));
                m_6.setBackgroundColor(getResources().getColor(R.color.white));
                y_1.setBackgroundColor(getResources().getColor(R.color.white));
                GetImtGraphAll("imt",currency2,7);
                break;

            case R.id.m_1:
                h_24.setBackgroundColor(getResources().getColor(R.color.white));
                d_7.setBackgroundColor(getResources().getColor(R.color.white));
                m_1.setBackgroundColor(getResources().getColor(R.color.purple_500));
                m_3.setBackgroundColor(getResources().getColor(R.color.white));
                m_6.setBackgroundColor(getResources().getColor(R.color.white));
                y_1.setBackgroundColor(getResources().getColor(R.color.white));
                GetImtGraphAll("imt",currency2,30);
                break;

            case R.id.m_3:
                h_24.setBackgroundColor(getResources().getColor(R.color.white));
                d_7.setBackgroundColor(getResources().getColor(R.color.white));
                m_1.setBackgroundColor(getResources().getColor(R.color.white));
                m_3.setBackgroundColor(getResources().getColor(R.color.purple_500));
                m_6.setBackgroundColor(getResources().getColor(R.color.white));
                y_1.setBackgroundColor(getResources().getColor(R.color.white));
                GetImtGraphAll("imt",currency2,90);
               break;

            case R.id.m_6:
                h_24.setBackgroundColor(getResources().getColor(R.color.white));
                d_7.setBackgroundColor(getResources().getColor(R.color.white));
                m_1.setBackgroundColor(getResources().getColor(R.color.white));
                m_3.setBackgroundColor(getResources().getColor(R.color.white));
                m_6.setBackgroundColor(getResources().getColor(R.color.purple_500));
                y_1.setBackgroundColor(getResources().getColor(R.color.white));

                GetImtGraphAll("imt",currency2,180);
                break;

            case R.id.y_1:
                h_24.setBackgroundColor(getResources().getColor(R.color.white));
                d_7.setBackgroundColor(getResources().getColor(R.color.white));
                m_1.setBackgroundColor(getResources().getColor(R.color.white));
                m_3.setBackgroundColor(getResources().getColor(R.color.white));
                m_6.setBackgroundColor(getResources().getColor(R.color.white));
                y_1.setBackgroundColor(getResources().getColor(R.color.purple_500));

               GetImtGraphAll("imt",currency2,365);
                break;

        }
}
    @Override
    public void onBackPressed() {
        super.onBackPressed();

     //   onSaveInstanceState(new Bundle());
        Intent intent = new Intent(ImtSmartGraphLayout.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void getBalance(){

        String token = userData.getToken();


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().AirDropBalance(token,"imt",currency2);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;

                if (response.code()==200){
                    try {
                       s=response.body().string();

                         JSONObject object = new JSONObject(s);
                        String balance = object.getString("balance");
                        String cal = object.getString("calculationPrice");
                        JSONObject object1 = new JSONObject(cal);
                        String calBalance = object1.getString("calculation");

                        DecimalFormat df = new DecimalFormat();
                        df.setMaximumFractionDigits(2);

                        if (calBalance.equals("null")){
                            double balance2 = Double.parseDouble(balance);
                            coinprice.setText(""+df.format(balance2));
                            balances.setText(CurrencySymbols+"0");
                        }else{

                            double balance2 = Double.parseDouble(balance);
                            double calBalance2 = Double.parseDouble(calBalance);

                            coinprice.setText(""+df.format(balance2));
                            balances.setText(CurrencySymbols+df.format(calBalance2));
                        }


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(ImtSmartGraphLayout.this)
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
                            .setActivity(ImtSmartGraphLayout.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Snacky.builder()
                        .setActivity(ImtSmartGraphLayout.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    public void GetImtGraph1d(String coinName, String currency2, int hour) {

        progressDialog = KProgressHUD.create(ImtSmartGraphLayout.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitGraph.RetrofitGraph1.getInstance().getApi1().ImtGraph1d(coinName,currency2,hour);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;
                hidepDialog();

                if (response.isSuccessful()){
                    ArrayList<Entry> yvalue = new ArrayList<>();
                    yvalue.clear();
                    try {
                        s=response.body().string();

                        JSONObject object = new JSONObject(s);
                        String response1 = object.getString("Response");

                        if (response1.equals("Error")){
                            String Message = object.getString("Message");
                            Snacky.builder()
                                    .setActivity(ImtSmartGraphLayout.this)
                                    .setText(Message)
                                    .setDuration(Snacky.LENGTH_SHORT)
                                    .setActionText(android.R.string.ok)
                                    .error()
                                    .show();
                        }else {
                            String Data = object.getString("Data");

                            JSONObject object1 = new JSONObject(Data);
                            String Data1 = object1.getString("Data");

                            JSONArray jsonArray = new JSONArray(Data1);
                            for (int i=0 ; i<=jsonArray.length();i++){
                                JSONObject object2 = jsonArray.getJSONObject(i);
                                String high = object2.getString("high");
                                Float aFloat = Float.parseFloat(high);
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
                    MyMarkerView mv = new MyMarkerView(ImtSmartGraphLayout.this, R.layout.custom_marker_view);

                    // Set the marker to the chart
                    mv.setChartView(chart);
                    chart.setMarker(mv);
                    chart.animateXY(3000,1000);

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
                        Drawable drawable = ContextCompat.getDrawable(ImtSmartGraphLayout.this, R.drawable.gradient1);
                        set1.setFillDrawable(drawable);
                    } else {
                        set1.setFillColor(Color.rgb(229, 146, 19));
                    }
                    set1.setMode(LineDataSet.Mode.LINEAR);
                    set1.setLineWidth(2f);
                    set1.setColor(Color.WHITE);
                    set1.setDrawValues(!set1.isDrawValuesEnabled());
                    set1.setDrawFilled(true);
                    set1.setDrawCircles(false);
                    chart.setBackgroundColor(getResources().getColor(R.color.purple_500));

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(ImtSmartGraphLayout.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }

    public void GetImtGraphAll(String coinName, String currency2,int days) {

        progressDialog = KProgressHUD.create(ImtSmartGraphLayout.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitGraph.RetrofitGraph1.getInstance().getApi1().ImtGraphall(coinName,currency2,days);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;
                hidepDialog();

                if (response.isSuccessful()){

                    yvalue.clear();
                    try {
                        s=response.body().string();

                        JSONObject object = new JSONObject(s);
                        String response1 = object.getString("Response");

                        if (response1.equals("Error")){
                            String Message = object.getString("Message");
                            Snacky.builder()
                                    .setActivity(ImtSmartGraphLayout.this)
                                    .setText(Message)
                                    .setDuration(Snacky.LENGTH_SHORT)
                                    .setActionText(android.R.string.ok)
                                    .error()
                                    .show();
                        }else {
                            String Data = object.getString("Data");

                            JSONObject object1 = new JSONObject(Data);
                            String Data1 = object1.getString("Data");

                            JSONArray jsonArray = new JSONArray(Data1);
                            for (int i=0 ; i<=jsonArray.length();i++){
                                JSONObject object2 = jsonArray.getJSONObject(i);
                                String high = object2.getString("high");
                                Float aFloat = Float.parseFloat(high);
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
                    MyMarkerView mv = new MyMarkerView(ImtSmartGraphLayout.this, R.layout.custom_marker_view);

                    // Set the marker to the chart
                    mv.setChartView(chart);
                    chart.setMarker(mv);
                    chart.animateXY(3000,1000);
                   // chart.animateX(2000);
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
                        Drawable drawable = ContextCompat.getDrawable(ImtSmartGraphLayout.this, R.drawable.gradient1);
                        set1.setFillDrawable(drawable);
                    } else {
                        set1.setFillColor(Color.rgb(229, 146, 19));
                    }
                    set1.setMode(LineDataSet.Mode.LINEAR);
                    set1.setLineWidth(2f);
                    set1.setColor(Color.WHITE);
                    set1.setDrawValues(!set1.isDrawValuesEnabled());
                    set1.setDrawFilled(true);
                    set1.setDrawCircles(false);
                    chart.setBackgroundColor(getResources().getColor(R.color.purple_500));

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(ImtSmartGraphLayout.this)
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
}