package com.crypto.croytowallet.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTP_Activity extends AppCompatActivity {
Button next2;
    KProgressHUD progressDialog;
    EditText enter_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_);
        next2=findViewById(R.id.next2);
        enter_otp= findViewById(R.id.enter_otp);



        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp=enter_otp.getText().toString().trim();

          if (otp.isEmpty()){

              hideKeyboard(v);
           /*   enter_otp.setError("please enter One Time Password");
          */    enter_otp.requestFocus();
              Snackbar warningSnackBar = Snacky.builder()
                      .setActivity(OTP_Activity.this)
                      .setText("please enter One Time Password")
                      .setTextColor(getResources().getColor(R.color.white))
                      .setDuration(Snacky.LENGTH_SHORT)
                      .warning();
              warningSnackBar.show();
          }else{

              Intent intent = new Intent(getApplicationContext(),Change_Password.class);
              intent.putExtra("otp",otp);
              startActivity(intent);
              finish();

          }


             /*   startActivity(new Intent(getApplicationContext(),Change_Password.class));

                Bundle bundle = getIntent().getExtras();
                String username = bundle.getString("username");
                Toast.makeText(OTP_Activity.this, ""+username, Toast.LENGTH_SHORT).show();*/
            }
        });
    }

    public void resendOTP(View view) {
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");


        progressDialog = KProgressHUD.create(OTP_Activity.this)
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
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){
                    hideKeyboard(view);
                   Snacky.builder()
                            .setView(view)
                            .setText("Otp resend in your register Email")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();
                }else if(response.code()==400){
                    hideKeyboard(view);
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setView(view)
                                .setText(" Oops Username Not Found !!!!!")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                        OTPexpire();
                        // Toast.makeText(SignUp.this, jsonObject1.getString("error")+"", Toast.LENGTH_SHORT).show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
                hideKeyboard(view);
                Snacky.builder()
                        .setView(view)
                        .setText("Please Check Your Internet Connection")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });


    }

    public void hideKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) {
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

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), ForgetPassword.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    public void OTPexpire(){
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                expire();
            }
        }, 300000);
    }

    public void expire(){
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");




        Call<ResponseBody> call=  RetrofitClient
                .getInstance()
                .getApi().expireOtp(username);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){
                    Toast.makeText(OTP_Activity.this, "Your Otp is expire", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

}