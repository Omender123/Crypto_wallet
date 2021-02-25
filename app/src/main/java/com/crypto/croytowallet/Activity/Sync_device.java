package com.crypto.croytowallet.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.crypto.croytowallet.ImtSmart.ImtSmartGraphLayout;
import com.crypto.croytowallet.ImtSmart.imtSwap;
import com.crypto.croytowallet.Interface.HistoryClickLister;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.ActiveDeviceModel;
import com.crypto.croytowallet.Model.TicketModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Sync_device extends AppCompatActivity implements HistoryClickLister {
    ImageView imageView;
    RecyclerView recyclerView;
    ArrayList<ActiveDeviceModel> modelArrayList;
    KProgressHUD progressDialog;
    ActiveDeviceAdapter activeDeviceAdapter;
    TextView balances ,textView1;
    SharedPreferences sharedPreferences;
    String CurrencySymbols,currency2;
    String jwt_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_device);
        imageView = findViewById(R.id.back);
        balances = findViewById(R.id.balance);
        textView1  =findViewById(R.id.balance1);
        modelArrayList = new ArrayList<ActiveDeviceModel>();

        recyclerView = findViewById(R.id.active_device_recyclerView);
        back();

        sharedPreferences =getApplicationContext().getSharedPreferences("currency",0);
        currency2 =sharedPreferences.getString("currency1","usd");
        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");


      //  checkBalance();
        AirDropBalance();
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

              //  Log.d("respon",response);
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
                        String jwt =jsonObject.getString("jwt");

                       activeDeviceModel1.setIP_Address(iP_address);
                        activeDeviceModel1.setOS_Name(osName);
                        activeDeviceModel1.setLocation(location);
                        activeDeviceModel1.setJwt(jwt);
                        modelArrayList.add(activeDeviceModel1);

                        Collections.reverse(modelArrayList);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                activeDeviceAdapter =new ActiveDeviceAdapter(modelArrayList,getApplicationContext(),Sync_device.this);
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

               // headers.put("Authorization", token);

                return headers;
            }


        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);


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
                            balances.setText(""+df.format(balance2));
                            textView1.setText(CurrencySymbols+"0");
                        }else{

                            double balance2 = Double.parseDouble(balance);
                            double calBalance2 = Double.parseDouble(calBalance);

                            balances.setText(""+df.format(balance2));
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
                                .setActivity(Sync_device.this)
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
                            .setActivity(Sync_device.this)
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
                        .setActivity(Sync_device.this)
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


        StringRequest stringRequest =new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object=new JSONObject(response);
                    int   checkBalance=object.getInt("airDrop");



                    balances.setText(checkBalance+".00");
                    Double balance = checkBalance*0.09;
                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);
                    textView1.setText(CurrencySymbols+df.format(balance));

                   // textView1.setText(CurrencySymbols+balance);


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

    @Override
    public void onHistoryItemClickListener(int position) {

        jwt_token =modelArrayList.get(position).getJwt();
        AlertDialogBox();
       // Log.d("jwt",modelArrayList.get(position).getJwt());
      //  Toast.makeText(this, ""+position, Toast.LENGTH_SHORT).show();
    }
    public void AlertDialogBox(){

        //Logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Sync_device.this);

        // set title
        alertDialogBuilder.setTitle("Crypto Wallet");

        // set dialog message
        alertDialogBuilder.setIcon(R.mipmap.ic_launcher_round);
        alertDialogBuilder
                .setMessage("Are you sure to Logout !!!!!")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Remove_Jwt();
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(Sync_device.this, "Logout Failed", Toast.LENGTH_SHORT).show();
                        dialog.cancel();

                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void Remove_Jwt() {
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

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().remove_JWT(token,username,jwt_token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();
                String s =null;
                hidepDialog();
                if (response.code()==200){

                    try {
                        s=response.body().string();

                        Snacky.builder()
                                .setActivity(Sync_device.this)
                                .setText("Successfully remove")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setTextColor(getResources().getColor(R.color.white))
                                .setActionText(android.R.string.ok)
                                .success()
                                .show();
                        getActiveDeviceDetails();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(Sync_device.this)
                                .setText(error)
                                .setTextColor(getResources().getColor(R.color.white))
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(Sync_device.this)
                            .setText("unAuthorization Request")
                            .setTextColor(getResources().getColor(R.color.white))
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
                        .setActivity(Sync_device.this)
                        .setText("Internet Problem")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }




}
