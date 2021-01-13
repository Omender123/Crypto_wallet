package com.crypto.croytowallet.signup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.R;
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
    Button next2;
    KProgressHUD progressDialog;
    EditText enter_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail_verfiy_otp);

        next2 = findViewById(R.id.next2);
        enter_otp = findViewById(R.id.enter_otp);


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


                }


            }
        });

    }

    public void resendOTP(View view) {

        String username = " ";
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

    private void showRightCustomDialog() {
        ImageView imageView,close;
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dailog, viewGroup, false);

        imageView =viewGroup.findViewById(R.id.imageView4);
        close =viewGroup.findViewById(R.id.close);

        imageView.setImageResource(R.drawable.ic_green_email);
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private void showWrongCustomDialog() {
        ImageView imageView,close;
        TextView textView,text_dialog;
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.custom_dailog, viewGroup, false);

        imageView =viewGroup.findViewById(R.id.imageView4);
        close =viewGroup.findViewById(R.id.close);
        textView=viewGroup.findViewById(R.id.textView5);
        imageView.setImageResource(R.drawable.ic_red_email_1);
        text_dialog =dialogView.findViewById(R.id.text_dialog);

        textView.setText("Sorry");
        text_dialog.setText("your gmail id has been not verified");
        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

}