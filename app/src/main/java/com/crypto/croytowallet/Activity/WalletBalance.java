package com.crypto.croytowallet.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Adapter.Transaaction_history_adapter;
import com.crypto.croytowallet.Extra_Class.MyPreferences;
import com.crypto.croytowallet.Extra_Class.PrefConf;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.Model.TransactionHistoryModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.TransactionHistorySharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Transaction_HistoryModel;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionHistory.Full_Transaction_History;
import com.crypto.croytowallet.TransactionHistory.Transaction_history;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.database.RetrofitClient;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class WalletBalance extends AppCompatActivity implements HistoryClickLister {
    ImageView imageView,sreach;
    TextView textView,textView1,more;
    RequestQueue requestQueue;
    RecyclerView recyclerView;
    ArrayList<TransactionHistoryModel> transactionHistoryModels;
    Transaaction_history_adapter transaaction_history_adapter;
    KProgressHUD progressDialog;
    TextView history_Empty;
   SharedPreferences sharedPreferences;
   String CurrencySymbols,currency2,balance,cal_balance;
   LinearLayout linearLayout;
    private ShimmerFrameLayout mShimmerViewContainer,mShimmerViewContainer1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_balance);
        imageView =findViewById(R.id.back);
        sreach =findViewById(R.id.sreach);
        back();
        textView  =findViewById(R.id.balance);
        textView1  =findViewById(R.id.balance1);

        more=findViewById(R.id.moretransactions);
        recyclerView=findViewById(R.id.recyclerViewAddBalance);
        history_Empty =findViewById(R.id.txt_list_is_empty);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer1 = findViewById(R.id.shimmer_view_container1);
        linearLayout =  findViewById(R.id.linear);


        sharedPreferences =getApplicationContext().getSharedPreferences("currency",0);
       currency2 =sharedPreferences.getString("currency1","usd");
        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");


        balance = MyPreferences.getInstance(getApplicationContext()).getString(PrefConf.USER_BALANCE,"0");
        cal_balance =MyPreferences.getInstance(getApplicationContext()).getString(PrefConf.CAL_USER_BALANCE,"0");

        textView.setText(balance);
        textView1.setText(CurrencySymbols+cal_balance);

        transactionHistoryModels =new ArrayList<TransactionHistoryModel>();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getHistory();
            }
        }, 2000);


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletBalance.this, Transaction_history.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        sreach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WalletBalance.this, Transaction_history.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }




    public  void getHistory(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.URL_TRANSACTION_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                transactionHistoryModels.clear();
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


                // stop animating Shimmer and hide the layout
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                mShimmerViewContainer1.stopShimmerAnimation();
                mShimmerViewContainer1.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);


                if(transactionHistoryModels!=null && transactionHistoryModels.size()>0){
                    transaaction_history_adapter = new Transaaction_history_adapter(transactionHistoryModels,getApplicationContext(),WalletBalance.this);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(transaaction_history_adapter);
                }else{

                    history_Empty.setVisibility(View.VISIBLE);

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


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

        onSaveInstanceState(new Bundle());
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    public void onHistoryItemClickListener(int position) {

        String id = transactionHistoryModels.get(position).getId();
        String sendername=transactionHistoryModels.get(position).getUsername();
        String receviername=transactionHistoryModels.get(position).getRecivedName();
        String amount=transactionHistoryModels.get(position).getAmountTrans();
        String status =transactionHistoryModels.get(position).getStatus();
        String time=transactionHistoryModels.get(position).getDate();

        Transaction_HistoryModel historyModel=new Transaction_HistoryModel(id,"OK",amount,sendername,receviername,time);

        //storing the user in shared preferences
        TransactionHistorySharedPrefManager.getInstance(getApplicationContext()).Transaction_History_Data(historyModel);

        Intent intent = new Intent(WalletBalance.this, Full_Transaction_History.class);
        startActivity(intent);

    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        mShimmerViewContainer1.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        mShimmerViewContainer1.stopShimmerAnimation();
        super.onPause();
    }
}