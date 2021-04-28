package com.crypto.croytowallet.TopUp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.crypto.croytowallet.Activity.Kyc;
import com.crypto.croytowallet.Activity.SelectCurrency;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.CurrencyModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.signup.SignUp;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EnterTop_Up extends AppCompatActivity {
    EditText ed_bank_name, ed_acc_no, ed_holder_name, ed_transactionId, ed_upi_id, ed_Amount;
    Spinner sp_mode, sp_currency;
    ArrayList<String> Currency;
    Button done;
    String BankName, Acc_no, Holder_name, trans_id, Upi_Id, Amount, Payment_mode, currencyType;
    UserData userData;
    String[] country = {"Select Payment Mode ", "UPI Mode", "Bank Mode"};
    KProgressHUD progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_top__up);
        ed_bank_name = findViewById(R.id.bank_name);
        ed_acc_no = findViewById(R.id.acc_no);
        ed_holder_name = findViewById(R.id.holder_name);
        ed_transactionId = findViewById(R.id.trans_id);
        ed_upi_id = findViewById(R.id.upi_id);
        ed_Amount = findViewById(R.id.enter_amount);
        sp_mode = findViewById(R.id.mode);
        sp_currency = findViewById(R.id.select_currency);
        done = findViewById(R.id.show_dailog);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        sp_mode.setAdapter(aa);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        Currency = new ArrayList<String>();
        getCurrency();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BankName = ed_bank_name.getText().toString().trim();
                Acc_no = ed_acc_no.getText().toString().trim();
                Holder_name = ed_holder_name.getText().toString().trim();
                trans_id = ed_transactionId.getText().toString().trim();
                Upi_Id = ed_upi_id.getText().toString().trim();
                Amount = ed_Amount.getText().toString().trim();

                if (BankName.isEmpty() || Acc_no.isEmpty() || Holder_name.isEmpty() || trans_id.isEmpty() || Amount.isEmpty()){
                    Snacky.builder()
                            .setActivity(EnterTop_Up.this)
                            .setText("Please filled all required details")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .warning()
                            .show();

                }else{

                    if (Payment_mode.equalsIgnoreCase("UPI Mode")){
                        
                        if (Upi_Id.isEmpty()){
                            Snacky.builder()
                                    .setActivity(EnterTop_Up.this)
                                    .setText("Please Enter Your UPI ID ")
                                    .setDuration(Snacky.LENGTH_SHORT)
                                    .setActionText(android.R.string.ok)
                                    .warning()
                                    .show();

                        }else{
                            PaymentDone(BankName, Acc_no, Holder_name, trans_id, Upi_Id, Amount, Payment_mode, currencyType);
                        }
                       
                    }else {
                        PaymentDone(BankName, Acc_no, Holder_name, trans_id, "NA", Amount, Payment_mode, currencyType);
                    }

                }


            }
        });

        sp_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Payment_mode = parent.getItemAtPosition(position).toString();
                if (Payment_mode.equalsIgnoreCase("UPI Mode")){
                    ed_upi_id.setVisibility(View.VISIBLE);
                }else {
                    ed_upi_id.setVisibility(View.GONE);
                }

              }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void PaymentDone(String bankName, String acc_no, String holder_name, String trans_id, String upi_id, String amount, String payment_mode, String currencyType) {

        String token = userData.getToken();

        progressDialog = KProgressHUD.create(EnterTop_Up.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().SendAddAmountRequest(token,bankName,acc_no,holder_name,trans_id,upi_id,amount,payment_mode,currencyType);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s=null;
                if (response.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    Toast.makeText(getApplicationContext(), "Your request is successfully send to our team.", Toast.LENGTH_SHORT).show();
                }else{
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(EnterTop_Up.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();

                Snacky.builder()
                        .setActivity(EnterTop_Up.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
        

    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }
    public void getCurrency() {
        String token = userData.getToken();

        progressDialog = KProgressHUD.create(EnterTop_Up.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Loading.........")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getAllCurrency(token);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();
                String s=null;
                Currency.clear();
                if (response.isSuccessful()){
                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);
                        String result = object.getString("currency");
                        JSONArray jsonArray = new JSONArray(result);

                        for (int i = 0; i <= jsonArray.length(); i++) {
                           JSONObject object1 = jsonArray.getJSONObject(i);
                            String currency = object1.getString("currency");

                            Currency.add(currency);

                        }

                    }
                    catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> adp = new ArrayAdapter<String> (EnterTop_Up.this,android.R.layout.simple_spinner_dropdown_item,Currency);
                    sp_currency.setAdapter(adp);

                    sp_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            currencyType = parent.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });



                }else {
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(EnterTop_Up.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();

                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }


                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();

                Snacky.builder()
                        .setActivity(EnterTop_Up.this)
                        .setText(t.getLocalizedMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
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
}