package com.crypto.croytowallet.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Adapter.Crypto_currencyInfo;
import com.crypto.croytowallet.Adapter.Transaaction_history_adapter;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.TransactionHistoryModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionHistory.Transaction_history;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.fragement.Wallet;
import com.crypto.croytowallet.login.Login;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class WalletBalance extends AppCompatActivity implements HistoryClickLister {
    ImageView imageView;
    TextView textView,textView1,more;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    ArrayList<TransactionHistoryModel> transactionHistoryModels;
    Transaaction_history_adapter transaaction_history_adapter;
    KProgressHUD progressDialog;
    TextView history_Empty;
   SharedPreferences sharedPreferences;
   String CurrencySymbols,currency2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_balance);
        imageView =findViewById(R.id.back);
        back();
        textView  =findViewById(R.id.balance);
        textView1  =findViewById(R.id.balance1);

        more=findViewById(R.id.moretransactions);
        recyclerView=findViewById(R.id.recyclerViewAddBalance);
        history_Empty =findViewById(R.id.txt_list_is_empty);



        sharedPreferences =getApplicationContext().getSharedPreferences("currency",0);
       currency2 =sharedPreferences.getString("currency1","usd");
        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");


        transactionHistoryModels =new ArrayList<TransactionHistoryModel>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
            //   checkBalance();
                AirDropBalance();
                getHistory();
            }
        }, 50);


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletBalance.this, Transaction_history.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    public void AirDropBalance(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token = user.getToken();

        String currency = currency2.toUpperCase();
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().AirDropBalance(token,"airdrop",currency);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
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
                            textView.setText(""+df.format(balance2));
                            textView1.setText(CurrencySymbols+"0");
                        }else{

                            double balance2 = Double.parseDouble(balance);
                            double calBalance2 = Double.parseDouble(calBalance);

                            textView.setText(""+df.format(balance2));
                            textView1.setText(CurrencySymbols+df.format(calBalance2));
                        }


                        //  Log.d("airDrop",s);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(WalletBalance.this)
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
                            .setActivity(WalletBalance.this)
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
                        .setActivity(WalletBalance.this)
                        .setText("Internet Problem ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }


    public void checkBalance(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String id=user.getId();

        String url1= URLs.URL_AIRDROP_BALANCE+""+id;
        progressDialog = KProgressHUD.create(WalletBalance.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest stringRequest =new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {
                hidepDialog();
                //   Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject object=new JSONObject(response);
                     int   checkBalance=object.getInt("airDrop");


                    textView.setText(checkBalance+".00");

                    Double balance = checkBalance*0.09;
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    textView1.setText(CurrencySymbols+df.format(balance));

                  //  textView1.setText(CurrencySymbols+balance);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();

               Toast.makeText(WalletBalance.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }

    public  void getHistory(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.URL_TRANSACTION_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               try {
                    JSONObject object  = new JSONObject(response);
                    String result =object.getString("result");
                    JSONArray jsonArray=new JSONArray(result);

                    for (int i=0;i<=9;i++){

                       String data =jsonArray.getString(i);
                       JSONObject  object1=new JSONObject(data);
                        TransactionHistoryModel transactionHistoryModel1=new TransactionHistoryModel();

                        String id = object1.getString("_id");
                        String sendername=object1.getString("senderName");
                        String receviername=object1.getString("receiverName");
                        String amount=object1.getString("amount");
                        String status =object1.getString("status");
                        String time=object1.getString("updatedAt");

                        transactionHistoryModel1.setId(id);
                        transactionHistoryModel1.setStatus(status);
                        transactionHistoryModel1.setRecivedName(receviername);
                        transactionHistoryModel1.setUsername(sendername);
                        transactionHistoryModel1.setAmountTrans(amount);
                        transactionHistoryModel1.setDate(time);


                        transactionHistoryModels.add(transactionHistoryModel1);

                    //  Toast.makeText(WalletBalance.this, ""+data, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(transactionHistoryModels!=null && transactionHistoryModels.size()>0){
                    transaaction_history_adapter = new Transaaction_history_adapter(transactionHistoryModels,getApplicationContext(),WalletBalance.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(transaaction_history_adapter);
                }else{

                    history_Empty.setVisibility(View.VISIBLE);

                }


               // Toast.makeText(WalletBalance.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
              /*  params.put("username", usernames);
                params.put("password", passwords);
*/

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", token);

                return headers;
            }


        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletBalance.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onHistoryItemClickListener(int position) {
        Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
    }
}