package com.crypto.croytowallet.ImtSmart;

import androidx.appcompat.app.AppCompatActivity;

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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.chaos.view.PinView;
import com.crypto.croytowallet.CoinTransfer.Payout_verification;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImtSmartVerification extends AppCompatActivity {
    String result,Amount,Token,enterPin,cryptoCurrency,otp,AuthToken,email2fa,google2fa;
    KProgressHUD progressDialog;
    UserData userData;
    TextView next,email_otp,googleToken,btn_sendOtp;
    PinView pinView;
    EditText ed_token,ed_otp;
    ImageView imageView;
    SharedPreferences sharedPreferences;
    TextInputLayout lyt_emiail,lyt_Google;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imt_smart_verification);
        next = findViewById(R.id.Next_btn);
        ed_otp = findViewById(R.id.enter_otp);
        ed_token = findViewById(R.id.enter_GoogleToken);
        pinView = findViewById(R.id.enter_pin);
        imageView =findViewById(R.id.back);
        email_otp = findViewById(R.id.Email_payout);
        googleToken = findViewById(R.id.Gmail_payout);
        lyt_emiail = findViewById(R.id.pass);
        lyt_Google = findViewById(R.id.pass1);
        btn_sendOtp=findViewById(R.id.btn_sendOtp);
        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();


        sharedPreferences=getSharedPreferences("ImtScan", Context.MODE_PRIVATE);

        Bundle bundle = getIntent().getExtras();
        // position=bundle.getInt("position");
       // result=bundle.getString("result2");

        result =sharedPreferences.getString("Imtaddress","");
        Amount = bundle.getString("amount2");

       // Toast.makeText(this, ""+result, Toast.LENGTH_SHORT).show();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enterPin = pinView.getText().toString();
                Token = ed_token.getText().toString().trim();
                otp = ed_otp.getText().toString().trim();

                String trans = userData.getTransaction_Pin();

                if (enterPin.isEmpty()) {
                    pinView.setError("Please enter transaction pin");
                    pinView.requestFocus();
                } else if (enterPin.equals(trans)) {
                    pinView.setLineColor(getResources().getColor(R.color.green));
                    Toast.makeText(ImtSmartVerification.this, ""+enterPin, Toast.LENGTH_SHORT).show();
                    sendcoin();
                }

            }
        });

        get2fa();
        back();



    }

    public void get2fa(){

        String token = userData.getToken();
        StringRequest request=new StringRequest(Request.Method.GET, URLs.URL_GET_2FA, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object =new JSONObject(response);
                    String result =  object.getString("result");
                    JSONObject object1 = new JSONObject(result);

                    String    email2fa1 = object1.getString("email2fa");
                    String  google2fa1 = object1.getString("google2fa");


                    if (email2fa1.equals("true")){

                        ed_otp.setError("Please enter Email Otp");
                        ed_otp.requestFocus();
                        email_otp.setVisibility(View.VISIBLE);
                        lyt_emiail.setVisibility(View.VISIBLE);
                        btn_sendOtp.setVisibility(View.VISIBLE);
                    }else {
                            email_otp.setVisibility(View.GONE);
                        lyt_emiail.setVisibility(View.GONE);
                        btn_sendOtp.setVisibility(View.GONE);
                       // Toast.makeText(ImtSmartVerification.this, "Your Email 2FA OFF", Toast.LENGTH_SHORT).show();
                    }

                    if (google2fa1.equals("true")){

                        ed_otp.setError("Please enter Google Token");
                        ed_otp.requestFocus();
                        googleToken.setVisibility(View.VISIBLE);
                        lyt_Google.setVisibility(View.VISIBLE);
                        btn_sendOtp.setVisibility(View.GONE);

                    }else {
                        googleToken.setVisibility(View.GONE);
                        lyt_Google.setVisibility(View.GONE);
                        btn_sendOtp.setVisibility(View.GONE);
                      //  Toast.makeText(ImtSmartVerification.this, "Your Google 2FA OFF", Toast.LENGTH_SHORT).show();
                    }
                    // Toast.makeText(Two_FA.this, ""+email2fa1+google2fa1, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Toast.makeText(Two_FA.this, ""+response, Toast.LENGTH_SHORT).show();
            }

        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", token);

                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);


    }

    public void sendcoin(){
        progressDialog = KProgressHUD.create(ImtSmartVerification.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        AuthToken =userData.getToken();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().coinTransfer(AuthToken,"IMT","0",Token,otp,Amount,enterPin,result);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;
                hidepDialog();
                if (response.code()==200){

                    try {
                        s=response.body().string();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(ImtSmartVerification.this, "Coin send Successfully", Toast.LENGTH_SHORT).show();

                        pinView.setLineColor(getResources().getColor(R.color.light_gray));
                        // Toast.makeText(ImtSmartVerification.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(ImtSmartVerification.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        pinView.setLineColor(getResources().getColor(R.color.light_gray));

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(ImtSmartVerification.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                    pinView.setLineColor(getResources().getColor(R.color.light_gray));
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();

                pinView.setLineColor(getResources().getColor(R.color.light_gray));
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(ImtSmartVerification.this, "Your Transaction is Pending. And Please Check your Status in Coin Transaction History  ", Toast.LENGTH_SHORT).show();

            }
        });
    }
    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    public void sendOtpAgain1(View view) {
        String username = userData.getUsername();
        progressDialog = KProgressHUD.create(ImtSmartVerification.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call=  RetrofitClient
                .getInstance()
                .getApi().sendOtp(username);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){


                    OTPexpire();
                    Snacky.builder()
                            .setView(view)
                            .setText("Otp resend in your register Email")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();
                }else if(response.code()==400){
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(ImtSmartVerification.this)
                                .setText(" Oops Username Not Found !!!!!")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(ImtSmartVerification.this)
                        .setText("Please Check Your Internet Connection")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }
    public void OTPexpire(){
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                expire();
            }
        }, 60000);
    }

    public void expire(){
        String username = userData.getUsername();


        Call<ResponseBody> call=  RetrofitClient
                .getInstance()
                .getApi().expireOtp(username);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){
                    Toast.makeText(ImtSmartVerification.this, "Your Otp is expire", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

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


}