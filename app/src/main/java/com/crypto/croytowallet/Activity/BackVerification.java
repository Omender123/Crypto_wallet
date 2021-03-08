package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.crypto.croytowallet.CoinTransfer.Payout_verification;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BackVerification extends AppCompatActivity {
    UserData userData;
    KProgressHUD progressDialog;
    PinView pinView;
    EditText ed_otp;
    TextView next;
    String otp,userId,transactionPin,AuthToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_verification);
        next = findViewById(R.id.Next_btn);
        ed_otp = findViewById(R.id.enter_otp);
        pinView = findViewById(R.id.enter_pin);
        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp = ed_otp.getText().toString().trim();
                transactionPin = pinView.getText().toString();
                if (otp.isEmpty() || transactionPin.isEmpty()){
                    Snacky.builder()
                            .setView(v)
                            .setText("Please Filed All Requirement")
                            .setTextColor(getResources().getColor(R.color.white))
                            .setDuration(Snacky.LENGTH_SHORT)
                            .warning()
                            .show();
                }else{
               //  startActivity(new Intent(getApplicationContext(),Scretephases.class));
               getMeninonic();
                }
            }
        });

    }

    public void getMeninonic(){
         userId = userData.getId();
        AuthToken = userData.getToken();

        progressDialog = KProgressHUD.create(BackVerification.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().get_Mnenonic(AuthToken,userId,transactionPin,otp);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){
                    try {
                        s = response.body().string();
                        JSONObject object = new JSONObject(s);
                        String result = object.getString("result");

                        startActivity(new Intent(getApplicationContext(),Scretephases.class));

                        Log.d("mnemonic",result);

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getApplicationContext(),Scretephases.class));
                }else if(response.code()==400){
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(BackVerification.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                        // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                } else if(response.code()==401){
                    Snacky.builder()
                            .setActivity(BackVerification.this)
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
                        .setActivity(BackVerification.this)
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });

    }
    public void sendOtpAgain(View view) {
        String username = userData.getUsername();
        progressDialog = KProgressHUD.create(BackVerification.this)
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
                    //OTPexpire();
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
                                .setActivity(BackVerification.this)
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
                        .setActivity(BackVerification.this)
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
                    Toast.makeText(BackVerification.this, "Your Otp is expire", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),Security.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Security.class));
        finish();
    }

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}