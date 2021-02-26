package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Adapter.Add_Currency_Adapter;

import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.Model_Class_Add_Currency;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_Currency extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Model_Class_Add_Currency> item_data;
    EditText search;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__currency);
        search = findViewById(R.id.search_currency);
        imageView =findViewById(R.id.back);
        item_data = new ArrayList<Model_Class_Add_Currency>();
        recyclerView=findViewById(R.id.recyclerView_add_currenecy);
        Coin_setdata();

        back();
    }

    public void Coin_setdata() {
        String url = "http://13.233.136.56:8080/api/currency/";
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
       // String username=user.getUsername();
        String token = user.getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET_COIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i <= jsonArray.length(); i++) {

                        Model_Class_Add_Currency currency_model = new Model_Class_Add_Currency();
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String id = jsonObject1.getString("_id");
                        //  String image = jsonObject1.getString("image");
                        String name = jsonObject1.getString("name");
//                        String rate = jsonObject1.getString("price_change_percentage_24h");
//                        String price = jsonObject1.getString("current_price");
//                        String high_price = jsonObject1.getString("high_24h");
//                        String low_price = jsonObject1.getString("low_24h");


//                        data_coin.setCoin_name(name);
//                        data_coin.setCoin_amount(price);
//                        data_coin.setCoin_Change(rate);
//                        data_coin.setImage(image);
////                        data_coin.setCoin_name(name);
////                        data_coin.setCoin_Current_Change(price);
////                        data_coin.setCoin_amount(rate);
////                        data_coin.setCoin_name(name);
                        currency_model.setCurrency_Title(name);
                        currency_model.setTitle_Des(id);
                        //currency_model.setImage(image);

                        item_data.add(currency_model);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Add_Currency_Adapter adapter = new Add_Currency_Adapter(item_data);
                RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(Add_Currency.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(mLayoutManager1);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);



                //  Toast.makeText(getContext(), ""+response.toString(), Toast.LENGTH_SHORT).show();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Add_Currency.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                 headers.put("Authorization", token);

                return headers;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


    }
//    private void fillExampleList() {
//        datavalue = new ArrayList<>();
//        datavalue.add(new Add_currency_Model(R.drawable.bitcoin_image, "One", "Ten"));
//        datavalue.add(new Add_currency_Model(R.drawable.bitcoin_image, "Two", "Eleven"));
//        datavalue.add(new Add_currency_Model(R.drawable.bitcoin_image, "Three", "Twelve"));
//        datavalue.add(new Add_currency_Model(R.drawable.bitcoin_image, "Four", "Thirteen"));
//        datavalue.add(new Add_currency_Model(R.drawable.bitcoin_image, "Four", "Thirteen"));
//        datavalue.add(new Add_currency_Model(R.drawable.bitcoin_image, "One", "Ten"));
//        datavalue.add(new Add_currency_Model(R.drawable.bitcoin_image, "One", "Ten"));
//        datavalue.add(new Add_currency_Model(R.drawable.bitcoin_image, "One", "Ten"));
//
//   }
//    private void setUpRecyclerView() {
//        RecyclerView recyclerView = findViewById(R.id.recyclerView_add_currenecy);
//        recyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        adapter = new Add_currency_Adapter(datavalue_model);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
//    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Currency.this, MainActivity.class);
                // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Add_Currency.this, MainActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
