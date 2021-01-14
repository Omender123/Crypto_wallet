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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Activity.Security;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.database.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ForgetPassword extends AppCompatActivity {
    Button next1;
    KProgressHUD progressDialog;
    EditText enter_username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        next1=findViewById(R.id.next1);
        enter_username = findViewById(R.id.enter_user);
        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendOtp(v);
                hideKeyboard(v);

            }
        });
    }

    public void sendOtp(View view) {
        if (validate() == false) {
            onOTPFailed();
            return;
        }
        saveToServerDB(view);

    }
    public void onOTPFailed() {
       // Toast.makeText(getBaseContext(), "Please fill all requirement ", Toast.LENGTH_LONG).show();
        Snackbar warningSnackBar = Snacky.builder()
                .setActivity(ForgetPassword.this)
                .setText("Please enter the username")
                .setTextColor(getResources().getColor(R.color.white))
                .setDuration(Snacky.LENGTH_SHORT)
                .warning();
               warningSnackBar.show();

        next1.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String usernames = enter_username.getText().toString().trim();


        if (usernames.isEmpty()) {
          //  enter_username.setError("");
            requestFocus(enter_username);
            valid = false;
        } else {
            enter_username.setError(null);
        }

        return valid;
    }

    private void saveToServerDB(View view) {

        String usernames = enter_username.getText().toString().trim();

        progressDialog = KProgressHUD.create(ForgetPassword.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call=  RetrofitClient
                .getInstance()
                .getApi().sendOtp(usernames);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){
                    hideKeyboard(view);
                  /*  Snacky.builder()
                            .setView(view)
                            .setText("Otp send in your register Email")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();*/
/*
                    Snackbar snackbar = Snackbar.make(view,"",Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    Snackbar snackbar;
                    snackbar = Snackbar.make(view, "Otp send in your register Email", Snackbar.LENGTH_SHORT);
                    View snackBarView = snackbar.getView();
                    snackBarView.setBackgroundColor(getResources().getColor(R.color.green));
                    TextView textView = (TextView) snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.white));
                    snackbar.show();
 */
                    Intent intent= new Intent(getApplicationContext(), OTP_Activity.class);
                    intent.putExtra("username",usernames);
                    startActivity(intent);
                    finish();
                    OTPexpire();
                    Toast.makeText(ForgetPassword.this, "Otp send in your registered Email", Toast.LENGTH_SHORT).show();

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

        Intent intent = new Intent(ForgetPassword.this, Login.class);
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
        String usernames = enter_username.getText().toString().trim();


        Call<ResponseBody> call=  RetrofitClient
                .getInstance()
                .getApi().expireOtp(usernames);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){
                    Toast.makeText(ForgetPassword.this, "Your Otp is expire", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
}