package com.crypto.croytowallet.ImtSmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Activity.Graph_layout;
import com.crypto.croytowallet.CoinTransfer.CoinScan;
import com.crypto.croytowallet.CoinTransfer.Received_Coin;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.kaopiz.kprogresshud.KProgressHUD;

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
    TextView price,balance,coinprice,increaseRate,null1;
    KProgressHUD progressDialog;
    UserData userData;
    private LineChart chart;
    TextView swap;
    String balance1,  price1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imt_smart_graph_layout);
        chart =  findViewById(R.id.cubiclinechart);
        swap =findViewById(R.id.swap_btc_btn);
        back =findViewById(R.id.back);
        received =findViewById(R.id.receive_coin);
        price = findViewById(R.id.price);
        balance =findViewById(R.id.balance);
        coinprice= findViewById(R.id.coinPrice);

        increaseRate  =findViewById(R.id.increaseRate);
         null1  = findViewById(R.id.null1);



        send=findViewById(R.id.send_coin);
        swap.setOnClickListener(this);
        back.setOnClickListener(this);
        received.setOnClickListener(this);
        send.setOnClickListener(this);

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
        set1.setColor(Color.WHITE);
        set1.setDrawFilled(true);
        chart.setBackgroundColor(getResources().getColor(R.color.purple_500));

        Bundle bundle = getIntent().getExtras();
        price1 = bundle.getString("price");
        String increaseRate1 = bundle.getString("chanage");

        increaseRate.setText(increaseRate1);
        price.setText("$"+price1);

        increaseRate.setTextColor(increaseRate1.contains("-")?
                getApplicationContext().getResources().getColor(R.color.red): getApplicationContext().getResources().getColor(R.color.green)  );

        null1.setTextColor(increaseRate1.contains("-")?
                getApplicationContext().getResources().getColor(R.color.red): getApplicationContext().getResources().getColor(R.color.green)  );

        if(increaseRate1.contains("-")){
          increaseRate.setText(increaseRate1);
        }else{
            increaseRate.setText("+"+increaseRate1);
        }
        getBalance();

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
    }
}
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        onSaveInstanceState(new Bundle());
    }

    public void getBalance(){

        String token = userData.getToken();

        progressDialog = KProgressHUD.create(ImtSmartGraphLayout.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Balance(token,"imt");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;
                hidepDialog();

                if (response.code()==200){
                    try {
                        s=response.body().string();

                        JSONObject jsonObject = new JSONObject(s);
                        balance1 = jsonObject.getString("balance");


                        double balance2 = Double.parseDouble(balance1);
                        double price = Double.parseDouble(price1);

                        double total = balance2*price;
                      //  Toast.makeText(ImtSmartGraphLayout.this, ""+total, Toast.LENGTH_SHORT).show();
                        balance.setText("$ "+total);
                        coinprice.setText(""+total);

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