package com.crypto.croytowallet.CoinTransfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chaos.view.PinView;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.Payment.Complate_payment;
import com.crypto.croytowallet.Payment.Enter_transaction_pin;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.login.Login;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class Pay_Coin extends AppCompatActivity {
    int position;
    String result,Amount,Token,enterPin,cryptoCurrency;
    TextView toolbar_title,showTransaction;
    ImageView imageView;
    EditText enterAmount,token;
    TextInputLayout enterAmount1,token1;
    Button send,next;
    PinView pinView;
    UserData userData;
    KProgressHUD progressDialog;
    SharedPreferences preferences;
    Socket socket;

    private AppCompatActivity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay__coin);
        imageView =findViewById(R.id.back);
        toolbar_title=findViewById(R.id.toolbar_title);
        pinView = findViewById(R.id.enter_pin);
        enterAmount=findViewById(R.id.ed_enter_amount);
      //  token =findViewById(R.id.ed_token);
        enterAmount1=findViewById(R.id.user);
        token1 =findViewById(R.id.pass);

        next=findViewById(R.id.next);
        send=findViewById(R.id.pay);
        showTransaction = findViewById(R.id.transaction);

        Bundle bundle = getIntent().getExtras();
       // position=bundle.getInt("position");
        result=bundle.getString("result");


         preferences=getApplicationContext().getSharedPreferences("symbols", Context.MODE_PRIVATE);
         cryptoCurrency = preferences.getString("symbol1","");
        position = preferences.getInt("position", -1);

        toolbar_title.setText("Send "+cryptoCurrency);


        Toast.makeText(this, ""+position+cryptoCurrency, Toast.LENGTH_SHORT).show();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amount = enterAmount.getText().toString().trim();
             //   Token = token.getText().toString().trim();
                if (Amount.isEmpty()){
                    enterAmount.setError("Please enter amount");
                    enterAmount.requestFocus();
                }else {
                    showTransaction.setVisibility(View.VISIBLE);
                    pinView.setVisibility(View.VISIBLE);
                    send.setVisibility(View.VISIBLE);
                    enterAmount.setVisibility(View.GONE);
                  //  token.setVisibility(View.GONE);
                    enterAmount1.setVisibility(View.GONE);
               //     token1.setVisibility(View.GONE);
                    next.setVisibility(View.GONE);
                }


            }
        });





        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPin=pinView.getText().toString();
                userData= SharedPrefManager.getInstance(getApplicationContext()).getUser();
                String trans =userData.getTransaction_Pin();


                if (enterPin.isEmpty()){
                    pinView.setError("Please enter transaction pin");
                    pinView.requestFocus();
                }else if (enterPin.equals(trans)){
                    pinView.setLineColor(getResources().getColor(R.color.green));

            //   Toast.makeText(Pay_Coin.this, ""+Amount+cryptoCurrency+Token+result+enterPin, Toast.LENGTH_SHORT).show();

                 done();

                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            pinView.setLineColor(getResources().getColor(R.color.light_gray));
                        }
                    }, 200);
                    pinView.setLineColor(getResources().getColor(R.color.red));
                }

            }
        });
      //  Toast.makeText(this, ""+position+result, Toast.LENGTH_SHORT).show();
        back();

        socketJoin();
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

    public void done(){

        progressDialog = KProgressHUD.create(Pay_Coin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        String Authtoken=userData.getToken();



        StringRequest request = new StringRequest(Request.Method.POST, URLs.URL_COIN_TRANSFER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                pinView.setLineColor(getResources().getColor(R.color.light_gray));
                Toast.makeText(Pay_Coin.this, "Send successfully", Toast.LENGTH_SHORT).show();
               // Toast.makeText(Pay_Coin.this, ""+response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();

                try{
                    parseVolleyError(error);
                }catch (Exception e){
                    Snacky.builder()
                            .setActivity(Pay_Coin.this)
                            .setText("insufficient Coin")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }


                pinView.setLineColor(getResources().getColor(R.color.light_gray));

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cryptoCurrency",cryptoCurrency);
                params.put("cryptoAmt",Amount);
               params.put("transactionPin",enterPin);
                //  params.put("token",Token);
                params.put("receiverAddress",result);



                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

               headers.put("Authorization", Authtoken);

                return headers;
            }


        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);


    }

    public void parseVolleyError(VolleyError error) {

        if (error.networkResponse.statusCode==401){
            Snacky.builder()
                    .setActivity(Pay_Coin.this)
                    .setText("Unauthorised request")
                    .setDuration(Snacky.LENGTH_SHORT)
                    .setActionText(android.R.string.ok)
                    .error()
                    .show();

        }else {
            try {
                String responseBody = new String(error.networkResponse.data, "utf-8");
                JSONObject data = new JSONObject(responseBody);

                String message=data.getString("error");
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
            } catch (UnsupportedEncodingException errorr) {
            }
        }

    }

    private void socketJoin(){
        try {
           socket = IO.socket("http://13.233.136.56:8080");

            socket.connect();

           /* socket.emit("join",userName);
            try {
                JSONObject event = new JSONObject();
                event.put("eventId",events.get(position).getId());
                socket.emit("getEvent",event);
            }catch (JSONException je){
                je.printStackTrace();
            }*/

            getMessageOn();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private void getMessageOn() {
        socket.on("transactionPending", args ->activity.runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];

            System.out.println("message");

            Toast.makeText(activity, "Message", Toast.LENGTH_SHORT).show();
           /* try {
                JSONObject user = data.getJSONObject("user");

            } catch (JSONException e) {
                e.printStackTrace();
            }*/



        }));

    }


}