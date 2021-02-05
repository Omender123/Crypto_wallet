package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Adapter.ActiveDeviceAdapter;
import com.crypto.croytowallet.Adapter.Ticket_Adapter;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.ActiveDeviceModel;
import com.crypto.croytowallet.Model.TicketModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;


import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class Sync_device extends AppCompatActivity {
    ImageView imageView;
    RecyclerView recyclerView;
    ArrayList<ActiveDeviceModel> modelArrayList;
    KProgressHUD progressDialog;
    ActiveDeviceAdapter activeDeviceAdapter;

    TextView balance ,textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_device);
        imageView = findViewById(R.id.back);
        balance = findViewById(R.id.balance);
        textView1  =findViewById(R.id.balance1);
        modelArrayList = new ArrayList<ActiveDeviceModel>();

        recyclerView = findViewById(R.id.active_device_recyclerView);
        back();

        checkBalance();
        getActiveDeviceDetails();
    }

    public void getActiveDeviceDetails(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String username=user.getUsername();
        String token = user.getToken();

        progressDialog = KProgressHUD.create(Sync_device.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        StringRequest stringRequest =new StringRequest(Request.Method.POST, URLs.URL_ACTIVE_DEVICE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                try {
                    JSONObject object = new JSONObject(response);
                    String result = object.getString("result");

                    JSONArray jsonArray =new JSONArray(result);

                    for (int i=0;i<=jsonArray.length();i++){

                        ActiveDeviceModel activeDeviceModel1 =new ActiveDeviceModel();
                        JSONObject jsonObject =jsonArray.getJSONObject(i);

                        String location =jsonObject.getString("location");
                        String osName =jsonObject.getString("osName");
                        String iP_address =jsonObject.getString("ipV4");

                       activeDeviceModel1.setIP_Address(iP_address);
                        activeDeviceModel1.setOS_Name(osName);
                        activeDeviceModel1.setLocation(location);

                        modelArrayList.add(activeDeviceModel1);

                        Collections.reverse(modelArrayList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                activeDeviceAdapter =new ActiveDeviceAdapter(modelArrayList,getApplicationContext());
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),2);
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(activeDeviceAdapter);


                // Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                if (error == null || error.networkResponse == null) {
                    return;
                }

                String body;
                //get status code here
                final String statusCode = String.valueOf(error.networkResponse.statusCode);
                //get response body and parse with appropriate encoding
                try {
                    body = new String(error.networkResponse.data,"UTF-8");

                    Toast.makeText(Sync_device.this, ""+body, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {

                }

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);

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

    public void checkBalance(){
        UserData user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String id=user.getId();
        String url1= URLs.URL_AIRDROP_BALANCE+""+id;


        StringRequest stringRequest =new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object=new JSONObject(response);
                    int   checkBalance=object.getInt("airDrop");



                    balance.setText(checkBalance+".00");
                    Double balance = checkBalance*0.09;
                    textView1.setText("$"+balance);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                /* params.put("_id", id);*/

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                // headers.put("Authorization", "Bearer "+Token);

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
        Intent intent = new Intent(Sync_device.this, Setting.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void back() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sync_device.this, Setting.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}
