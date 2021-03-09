package com.crypto.croytowallet.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.crypto.croytowallet.R;
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

public class Unlock_Account extends AppCompatActivity {
EditText ed_uername,ed_transactionPin,ed_otp,ed_new_pass,ed_mnemonic;
String  username,transactionPin,otp,new_pass,mnemonic;
Button btn_unlock;
    KProgressHUD progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock_account);
        ed_uername = findViewById(R.id.username);
        ed_transactionPin = findViewById(R.id.transactionPin);
        ed_otp = findViewById(R.id.otp);
        ed_new_pass = findViewById(R.id.new_password);
        ed_mnemonic = findViewById(R.id.mnemonic);
        btn_unlock = findViewById(R.id.btn_unlock);

        btn_unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = ed_uername.getText().toString().trim();
                transactionPin = ed_transactionPin.getText().toString().trim();
                otp = ed_otp.getText().toString().trim();
                new_pass = ed_new_pass.getText().toString().trim();
                mnemonic = ed_mnemonic.getText().toString().trim();

                if (username.isEmpty()||transactionPin.isEmpty()||otp.isEmpty()||new_pass.isEmpty()||mnemonic.isEmpty()){
                    Snacky.builder()
                            .setActivity(Unlock_Account.this)
                            .setText("please enter all details")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .warning()
                            .show();
                }else{

                   Unlock_Account_api();
                }
            }
        });

    }

    public void Unlock_Account_api() {

        progressDialog = KProgressHUD.create(Unlock_Account.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().Unlock_Account_Api(username,transactionPin,otp,new_pass,mnemonic);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hidepDialog();

                String s=null;
                if (response.code()==200){

                    startActivity(new Intent(getApplicationContext(),Login.class));

                }else if(response.code()==400){
                    try {

                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");

                        Snacky.builder()
                                .setActivity(Unlock_Account.this)
                                .setText(error)
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
                        .setActivity(Unlock_Account.this)
                        .setText("Please Check Your Internet Connection")
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),Login.class));
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(),Login.class));
    }
}