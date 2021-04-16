package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.AppUtils;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.RozerPayModelData;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Updated_data;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import de.mateware.snacky.Snacky;

public class Top_up_Money extends AppCompatActivity {
    ImageView imageView;
    CardView add_money;
    CheckBox checkBox;
    EditText enter_amount;
    KProgressHUD progressDialog;
    String currency2;
    UserData userData;
    RozerPayModelData rozerPayModelData1;
    private PaymentData paymentData1;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up__money);
        imageView =findViewById(R.id.back);
        add_money=findViewById(R.id.add_money);
        checkBox=findViewById(R.id.checkbox);
        enter_amount=findViewById(R.id.enter_add_money_amont);
        userData= SharedPrefManager.getInstance(getApplicationContext()).getUser();


        sharedPreferences =getSharedPreferences("currency",0);
        currency2 =sharedPreferences.getString("currency1","usd");

        Toast.makeText(this, ""+currency2, Toast.LENGTH_SHORT).show();
        rozerPayModelData1=new RozerPayModelData();
        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount=enter_amount.getText().toString().trim();
           if (amount.isEmpty()){
                    enter_amount.requestFocus();
                    enter_amount.setError("Please enter amount to add money");
                }else if(!checkBox.isChecked()){
                    checkBox.requestFocus();
                    Snacky.builder()
                            .setView(v)
                            .setText(" Please Accept the Terms & Conditions !")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else{

               GenrateQrCode(amount);
                }
                hideKeyboard(v);

            }
        });
        back();
    }

    public void GenrateQrCode(String amount) {

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