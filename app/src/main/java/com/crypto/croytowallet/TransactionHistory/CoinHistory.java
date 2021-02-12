package com.crypto.croytowallet.TransactionHistory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.Adapter.Coin_History_Adapter;
import com.crypto.croytowallet.Adapter.Transaaction_history_adapter;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.CoinModal;
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
import java.util.HashMap;
import java.util.Map;

public class CoinHistory extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<CoinModal> coinModals;
    Coin_History_Adapter coin_history_adapter;
    KProgressHUD progressDialog;
    SharedPreferences sharedPreferences;
    TextView history_Empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_history);
        recyclerView=findViewById(R.id.recyclerCoin);

        history_Empty =findViewById(R.id.txt_list_is_empty);
        coinModals =new ArrayList<CoinModal>();

        getCoinHistory();
    }

    public void getCoinHistory() {
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String token=user.getToken();

        progressDialog = KProgressHUD.create(CoinHistory.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest stringRequest=new StringRequest(Request.Method.GET, URLs.URL_GET_COIN_HISTORY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                try {
                    JSONObject object  = new JSONObject(response);
                    String result =object.getString("result");

                    JSONArray jsonArray  =  new JSONArray(result);
                    for (int i=0;i<=jsonArray.length();i++){
                        JSONObject object1 = jsonArray.getJSONObject(1);

                        CoinModal modal = new CoinModal();
                        String type = object1.getString("type");
                        String amount = object1.getString("amtOfCrypto");
                        String id = object1.getString("_id");
                        String date = object1.getString("updatedAt");
                        String userData = object1.getString("userId");

                        JSONObject object2 = new JSONObject(userData);
                        String username =object2.getString("username");


                       modal.setUsername(username);
                       modal.setTime(date);
                       modal.setAmount(amount);
                       modal.setType(type);
                       modal.setId(id);
                       coinModals.add(modal);

                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(coinModals!=null && coinModals.size()>0){
                    coin_history_adapter = new Coin_History_Adapter(coinModals,getApplicationContext());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(coin_history_adapter);
                }else{

                    history_Empty.setVisibility(View.VISIBLE);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                // Toast.makeText(Transaction_history.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CoinHistory.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }


    public void onClick(View view) {
        Intent intent = new Intent(CoinHistory.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(CoinHistory.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}