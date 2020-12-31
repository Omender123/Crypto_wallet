package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
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
import com.chaos.view.PinView;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.PearToPearModel;
import com.crypto.croytowallet.SharedPrefernce.PearToPearSharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionPin.EnterConfirmMnemonic;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.login.Login;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Enter_transaction_pin extends AppCompatActivity {
PinView pinView;
CardView pay_money;
    ImageView imageView;
    EditText enter_token;
    KProgressHUD progressDialog;
    String url="http://13.233.136.56:8080/api/transaction/peerToPeer";
    UserData userData;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_transaction_pin);
        imageView =findViewById(R.id.back);
        pinView = findViewById(R.id.enter_pin);
        pay_money = findViewById(R.id.pay_money);
       // enter_token = findViewById(R.id.enter_token);
         userData= SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String trans =userData.getTransaction_Pin();
        preferences=getApplicationContext().getSharedPreferences("walletScan", Context.MODE_PRIVATE);

        pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enterPin=pinView.getText().toString();

                if (enterPin.isEmpty()){
                    pinView.setError("Please enter transaction pin");
                    pinView.requestFocus();
                }else if (enterPin.equals(trans)){
                    pinView.setLineColor(getResources().getColor(R.color.green));
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
               // Toast.makeText(Enter_transaction_pin.this, ""+enterPin, Toast.LENGTH_SHORT).show();


            }
        });

        back();
    }


    public void done(){

        progressDialog = KProgressHUD.create(Enter_transaction_pin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        String userAddressID=userData.getId();
        String cryptoCurrency="airDropIMT";
        Bundle bundle = getIntent().getExtras();
        String Amount = bundle.getString("amount12");
        String enterPin=pinView.getText().toString();
        String to_addressID=preferences.getString("id","");
        String token=userData.getToken();



        StringRequest request=new StringRequest(Request.Method.POST, URLs.URL_PEER_TO_PEER, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                try {
                    JSONObject object=new JSONObject(response);
                    String result=object.getString("result");
                    JSONObject object1=new JSONObject(result);
                    String id=object1.getString("_id");
                    String status=object1.getString("status");
                    String amtOfCrypto=object1.getString("amtOfCrypto");

                  /*  PearToPearModel pearToPearModel=new PearToPearModel(id,status,amtOfCrypto);
                    PearToPearSharedPrefManager.getInstance(getApplicationContext()).pearToPearData(pearToPearModel);
                  */

                   // Toast.makeText(Enter_transaction_pin.this, ""+id+"\n"+status+"\n"+amtOfCrypto, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Enter_transaction_pin.this, Complate_payment.class);
                    intent.putExtra("status",status);
                    intent.putExtra("amt",amtOfCrypto);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            //  Toast.makeText(Enter_transaction_pin.this, ""+response, Toast.LENGTH_SHORT).show();
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
                params.put("receiverId",to_addressID);


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", token);

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
        Intent intent = new Intent(Enter_transaction_pin.this, Pay_money.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Enter_transaction_pin.this, Pay_money.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}