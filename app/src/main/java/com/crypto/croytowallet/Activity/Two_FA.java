package com.crypto.croytowallet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Payment.Complate_payment;
import com.crypto.croytowallet.Payment.Enter_transaction_pin;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.login.Login;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.zcw.togglebutton.ToggleButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class Two_FA extends AppCompatActivity {
    ImageView imageView;
    ToggleButton google_to_fa,email_to_fa;
    KProgressHUD progressDialog;
    UserData userData;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two__f);
        imageView =findViewById(R.id.back);

        google_to_fa=findViewById(R.id.toogle1);
        email_to_fa=findViewById(R.id.toogle2);
        userData= SharedPrefManager.getInstance(getApplicationContext()).getUser();

        google_to_fa.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){
                    google_2FA_Enable();
                }
                else{
                    Disable_2FA();
                }
            }
        });
        email_to_fa.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on){
                   Email_2FA_Enable();
                }
                else{
                    Disable_2FA();
                }
            }
        });

        back();
    }
    public void google_2FA_Enable(){

        String id=userData.getId();
        progressDialog = KProgressHUD.create(Two_FA.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();
        StringRequest request=new StringRequest(Request.Method.POST, URLs.URL_2FA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(Two_FA.this)
                        .setText("successful Turn ON ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .success()
                        .show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                parseVolleyError(error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
              //  params.put("cryptoCurrency",cryptoCurrency);
                params.put("userId",id);
                params.put("google2fa","true");
                params.put("email2fa","false");




                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

               // headers.put("Authorization", token);

                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);


    }

    public void Email_2FA_Enable(){
      String id=userData.getId();
        progressDialog = KProgressHUD.create(Two_FA.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();
        StringRequest request=new StringRequest(Request.Method.POST, URLs.URL_2FA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(Two_FA.this)
                        .setText("successful Turn ON ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .success()
                        .show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                parseVolleyError(error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //  params.put("cryptoCurrency",cryptoCurrency);
                params.put("userId",id);
                params.put("google2fa","false");
                params.put("email2fa","true");



                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                // headers.put("Authorization", token);

                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);

    }

    public void Disable_2FA(){
        String id=userData.getId();
        progressDialog = KProgressHUD.create(Two_FA.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();
        StringRequest request=new StringRequest(Request.Method.POST, URLs.URL_2FA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                Snacky.builder()
                        .setActivity(Two_FA.this)
                        .setText("successful Turn OFF ")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .success()
                        .show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                parseVolleyError(error);

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //  params.put("cryptoCurrency",cryptoCurrency);
                params.put("userId",id);
                params.put("google2fa","false");
                params.put("email2fa","false");



                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                // headers.put("Authorization", token);

                return headers;
            }

        };
        VolleySingleton.getInstance(this).addToRequestQueue(request);


    }
    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String message=data.getString("error");
            Snacky.builder()
                    .setActivity(Two_FA.this)
                    .setText(message)
                    .setDuration(Snacky.LENGTH_SHORT)
                    .setActionText(android.R.string.ok)
                    .error()
                    .show();
            //      Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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

        Intent intent = new Intent(Two_FA.this, Security.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Two_FA.this, Security.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

}