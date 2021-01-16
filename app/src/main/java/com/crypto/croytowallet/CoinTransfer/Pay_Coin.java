package com.crypto.croytowallet.CoinTransfer;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay__coin);
        imageView =findViewById(R.id.back);
        toolbar_title=findViewById(R.id.toolbar_title);
        pinView = findViewById(R.id.enter_pin);
        enterAmount=findViewById(R.id.ed_enter_amount);
        token =findViewById(R.id.ed_token);
        enterAmount1=findViewById(R.id.user);
        token1 =findViewById(R.id.pass);

        next=findViewById(R.id.next);
        send=findViewById(R.id.pay);
        showTransaction = findViewById(R.id.transaction);

        Bundle bundle = getIntent().getExtras();
        position=bundle.getInt("position");
        result=bundle.getString("result");


        preferences=getApplicationContext().getSharedPreferences("symbols", Context.MODE_PRIVATE);
         cryptoCurrency = preferences.getString("symbol1","");
      //  Toast.makeText(this, ""+position+cryptoCurrency, Toast.LENGTH_SHORT).show();


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Amount = enterAmount.getText().toString().trim();
                Token = token.getText().toString().trim();
                if (Amount.isEmpty()){
                    enterAmount.setError("Please enter amount");
                    enterAmount.requestFocus();
                }else {
                    showTransaction.setVisibility(View.VISIBLE);
                    pinView.setVisibility(View.VISIBLE);
                    send.setVisibility(View.VISIBLE);
                    enterAmount.setVisibility(View.GONE);
                    token.setVisibility(View.GONE);
                    enterAmount1.setVisibility(View.GONE);
                    token1.setVisibility(View.GONE);
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

        switch (position){
            case 0:
                toolbar_title.setText("Send BTC");
                break;

            case 1:
                toolbar_title.setText("Send ETH");
                break;

            case 2:
                toolbar_title.setText("Receive Tether");
                break;

            case 3:
                toolbar_title.setText("Receive XRP");
                break;

            case 4:
                toolbar_title.setText("Receive Lite");
                break;}
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



        StringRequest request=new StringRequest(Request.Method.POST, URLs.URL_COIN_TRANSFER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                Toast.makeText(Pay_Coin.this, "Send successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                  pinView.setLineColor(getResources().getColor(R.color.light_gray));
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                parseVolleyError(error);
                pinView.setLineColor(getResources().getColor(R.color.light_gray));
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cryptoCurrency",cryptoCurrency);
                params.put("cryptoAmt",Amount);
                params.put("transactionPin",enterPin);
                params.put("token",Token);
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
     /*   RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);*/

    }

    public void parseVolleyError(VolleyError error) {
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