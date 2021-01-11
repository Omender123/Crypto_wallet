package com.crypto.croytowallet.TransactionHistory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Activity.WalletBalance;
import com.crypto.croytowallet.Adapter.Transaaction_history_adapter;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.TransactionHistoryModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Transaction_history extends AppCompatActivity implements HistoryClickLister {
    ImageView imageView;
    RecyclerView recyclerView;
    ArrayList<TransactionHistoryModel> transactionHistoryModels;
    Transaaction_history_adapter transaaction_history_adapter;
    KProgressHUD progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        imageView =findViewById(R.id.back);

        back();
        recyclerView=findViewById(R.id.recyclerTransation);

        transactionHistoryModels =new ArrayList<TransactionHistoryModel>();

        getHistory();
    }

    public  void getHistory(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        progressDialog = KProgressHUD.create(Transaction_history.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.URL_TRANSACTION_HISTORY_FULL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                try {
                    JSONObject object  = new JSONObject(response);
                    String result =object.getString("result");
                    JSONArray jsonArray=new JSONArray(result);

                    for (int i=0;i<=jsonArray.length();i++){

                        String data =jsonArray.getString(i);
                        JSONObject  object1=new JSONObject(data);
                        TransactionHistoryModel transactionHistoryModel1=new TransactionHistoryModel();
                        String id = object1.getString("_id");
                        String sendername=object1.getString("senderName");
                        String receviername=object1.getString("receiverName");
                        String amount=object1.getString("amtOfCrypto");
                        String time=object1.getString("updatedAt");

                        transactionHistoryModel1.setId(id);
                        transactionHistoryModel1.setStatus(receviername);
                        transactionHistoryModel1.setUsername(sendername);
                        transactionHistoryModel1.setAmountTrans(amount);
                        transactionHistoryModel1.setDate(time);


                        transactionHistoryModels.add(transactionHistoryModel1);

                   //   Collections.reverse(transactionHistoryModels);
                      //  Toast.makeText(Transaction_history.this, ""+data, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                transaaction_history_adapter = new Transaaction_history_adapter(transactionHistoryModels,getApplicationContext(),Transaction_history.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(transaaction_history_adapter);

                //  Toast.makeText(WalletBalance.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                Toast.makeText(Transaction_history.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(Transaction_history.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });

        }

    @Override
    public void onHistoryItemClickListener(int position) {

        Intent intent = new Intent(Transaction_history.this,Full_Transaction_History.class);
        intent.putExtra("position",position);
        startActivity(intent);
    }
}