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
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Adapter.Crypto_currencyInfo;
import com.crypto.croytowallet.CoinTransfer.CoinScan;
import com.crypto.croytowallet.CoinTransfer.Received_Coin;
import com.crypto.croytowallet.ImtSmart.ImtSmartVerification;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Updated_data;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
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
    private Exchange exchange;
    int position;
    String symbol,image,coinName,change,CurrencySymbols;
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

          send=findViewById(R.id.send_coin);
         swap.setOnClickListener(this);
         back.setOnClickListener(this);
        received.setOnClickListener(this);
        send.setOnClickListener(this);


       /* preferences=getApplicationContext().getSharedPreferences("symbols", Context.MODE_PRIVATE);
        position = preferences.getInt("position", -1);
        Updated_data.getInstans(getApplicationContext()).getUserId();
        price1 = preferences.getInt("price", -1);
        symbol = preferences.getString("symbol1", null);
        image = preferences.getString("image", null);
        coinName = preferences.getString("coinName", null);
        change = preferences.getString("change", null);*/
       // preferences=getApplicationContext().getSharedPreferences("symbols", Context.MODE_PRIVATE);
        position = Updated_data.getInstans(getApplicationContext()).getUserId();
        price1 =Updated_data.getInstans(getApplicationContext()).getprice();
        symbol = Updated_data.getInstans(getApplicationContext()).getmobile();
        image = Updated_data.getInstans(getApplicationContext()).getImage();
        coinName =Updated_data.getInstans(getApplicationContext()).getUsername();
        change =Updated_data.getInstans(getApplicationContext()).getChange();

      //  Toast.makeText(this, ""+symbol, Toast.LENGTH_SHORT).show();

        sharedPreferences =getApplicationContext().getSharedPreferences("currency",0);

        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");

        //   Log.d("price",getString(price1));

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();


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
        chart.setBackgroundColor(getResources().getColor(R.color.purple_500));





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

}