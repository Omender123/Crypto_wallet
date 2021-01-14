package com.crypto.croytowallet.signup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SignUpData;
import com.crypto.croytowallet.SharedPrefernce.SignUpRefernace;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.login.Change_Password;
import com.crypto.croytowallet.login.OTP_Activity;
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

public class GmailVerfiyOtp extends AppCompatActivity {
    Button next2,done;
    KProgressHUD progressDialog;
    EditText enter_otp;
    SignUpData user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_verfiy_otp);

        next2 = findViewById(R.id.next2);
        enter_otp = findViewById(R.id.enter_otp);
        user = SignUpRefernace.getInstance(this).getUser();
       done = findViewById(R.id.Done_btn1);



        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = enter_otp.getText().toString().trim();

                if (otp.isEmpty()) {

                    hideKeyboard(v);
                    enter_otp.requestFocus();
                    Snackbar warningSnackBar = Snacky.builder()
                            .setActivity(GmailVerfiyOtp.this)
                            .setText("please enter One Time Password")
                            .setTextColor(getResources().getColor(R.color.white))
                            .setDuration(Snacky.LENGTH_SHORT)
                            .warning();
                    warningSnackBar.show();
                } else {
                    verifyOTP(v);


                }
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Add_Verification1.class));
            }
        });
    }

    public  void verifyOTP(View view){
        String otp = enter_otp.getText().toString().trim();
        String username=   user.getUsername();

        progressDialog = KProgressHUD.create(GmailVerfiyOtp.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().OtpVerify(username,otp);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s = null;
                if (response.code() == 200) {
                    hideKeyboard(view);
                    showRightCustomDialog();
                    next2.setVisibility(View.GONE);
                    done.setVisibility(View.VISIBLE);
                    OTPexpire();
                } else if (response.code() == 400) {
                    hideKeyboard(view);
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

                        showWrongCustomDialog();
                      /*  Snacky.builder()
                                .setView(view)
                                .setText(" Oops OTP is Wrong !!!!!")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();*/
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

    public void resendOTP(View view) {

        String username=   user.getUsername();
        progressDialog = KProgressHUD.create(GmailVerfiyOtp.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient
                .getInstance()
                .getApi().sendOtp(username);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s = null;
                if (response.code() == 200) {
                    hideKeyboard(view);
                    Snacky.builder()
                            .setView(view)
                            .setText("Otp resend in your register Email")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .success()
                            .show();

                } else if (response.code() == 400) {
                    hideKeyboard(view);
                    try {

                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");

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
        String username=   user.getUsername();

        Call<ResponseBody> call=  RetrofitClient
                .getInstance()
                .getApi().expireOtp(username);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                // hidepDialog();

                String s=null;
                if (response.code()==200){
                    Toast.makeText(GmailVerfiyOtp.this, "Your Otp is expire", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }
    private void showRightCustomDialog() {
        ImageView imageView,close;
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        // custom dialog
        final Dialog dialog = new Dialog(GmailVerfiyOtp.this);
        dialog.setContentView(R.layout.custom_dailog);


        imageView = (ImageView) dialog.findViewById(R.id.imageView4);
        close = (ImageView) dialog.findViewById(R.id.close);

        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_green_email));


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();



            }
        });

        dialog.show();
    }

    private void showWrongCustomDialog() {
        ImageView imageView,close;
        TextView textView,text_dialog;


        // custom dialog
        final Dialog dialog = new Dialog(GmailVerfiyOtp.this);
        dialog.setContentView(R.layout.custom_dailog);

        imageView = (ImageView) dialog.findViewById(R.id.imageView4);
        close = (ImageView) dialog.findViewById(R.id.close);
        textView= (TextView) dialog.findViewById(R.id.textView5);
        imageView.setImageResource(R.drawable.ic_red_email_1);

        text_dialog =dialog.findViewById(R.id.text_dialog);

        textView.setText("Sorry");
        text_dialog.setText("your gmail id has been not verified");

        dialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}