package com.crypto.croytowallet.TopUp;

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
import com.crypto.croytowallet.Activity.BackVerification;
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

public class Top_up_Money extends AppCompatActivity implements View.OnClickListener {
CardView card_paypal,card_bank;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up__money);
        card_paypal = findViewById(R.id.card_paypal);
          card_bank = findViewById(R.id.card_bank);

          card_paypal.setOnClickListener(this);
          card_bank.setOnClickListener(this);

      }
    public void back(View view) {
          onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    @Override
    public void onClick(View v) {

          switch (v.getId()){

              case R.id.card_bank:
                  startActivity(new Intent(Top_up_Money.this,Enter_TopUp_Amount.class));
                  break;

              case R.id.card_paypal:

                  Snacky.builder()
                          .setActivity(Top_up_Money.this)
                          .setText("Coming Soon")
                          .setTextColor(getResources().getColor(R.color.white))
                          .setDuration(Snacky.LENGTH_SHORT)
                          .setActionText(android.R.string.ok)
                          .success()
                          .show();

                  break;
          }
    }
}