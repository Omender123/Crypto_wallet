package com.crypto.croytowallet.ImtSmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.login.Login;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class imtSwap extends AppCompatActivity  {
    Spinner sendSpinner, reciveSpinner;
    String sendData, receviedData,SwapAmount;
    ImageView imageView;
    TextView swapBtn;
    EditText enter_Swap_Amount;
    String[] currency1 = {"select Currency ","imt","airdrop"};

    String[] currency2 = {"select Currency ","imt","airdrop"};
    KProgressHUD progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imt_swap);
        sendSpinner = findViewById(R.id.sendSpinner);
        reciveSpinner = findViewById(R.id.recivedSpiner);
        imageView = findViewById(R.id.back);

        enter_Swap_Amount = findViewById(R.id.enter_swap);
        swapBtn = findViewById(R.id.swap_btn);



        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, currency1);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sendSpinner.setAdapter(aa);
        sendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendData=currency1[position];
                Toast.makeText(view.getContext(), sendData,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> bb = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, currency2);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reciveSpinner.setAdapter(bb);
        reciveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                receviedData=currency2[position];
              Toast.makeText(view.getContext(), receviedData,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            back();

            swapBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SwapAmount = enter_Swap_Amount.getText().toString().trim();

                    if (SwapAmount.isEmpty()){
                        Snacky.builder()
                                .setActivity(imtSwap.this)
                                 .setText("Please enter Swap Amount")
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();
                    }else{
                        SwapApi();
                    }
                }
            });

    }

    public void SwapApi() {

        UserData userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        String Token = userData.getToken();
        String eth_Address = userData.getETH();
        progressDialog = KProgressHUD.create(imtSwap.this)
                   .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().IMT_SWAP(Token,sendData,receviedData,SwapAmount,"",eth_Address);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s =null;
                hidepDialog();
                if (response.code()==200){

                    try {
                        s=response.body().string();

                        if (s==null){
                            startActivity(new Intent(getApplicationContext(), ImtSmartGraphLayout.class));
                          //  Toast.makeText(imtSwap.this, "Error  occurred in Transaction", Toast.LENGTH_SHORT).show();
                        }else {
                            startActivity(new Intent(getApplicationContext(), ImtSmartGraphLayout.class));
                            Toast.makeText(imtSwap.this, " Successfully "+sendData+"to"+receviedData, Toast.LENGTH_SHORT).show();
                        }



                        Toast.makeText(imtSwap.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(imtSwap.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code()==401){
                    Snacky.builder()
                            .setActivity(imtSwap.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else if (response.code()==504){
                    Snacky.builder()
                            .setActivity(imtSwap.this)
                            .setText("Gate Way Time Down")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
              Snacky.builder()
                        .setActivity(imtSwap.this)
                        .setText("Please Check Your Internet Connection")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

              /*  startActivity(new Intent(getApplicationContext(), ImtSmartGraphLayout.class));
                Toast.makeText(imtSwap.this, "Your Amount is Not detected ", Toast.LENGTH_SHORT).show();*/
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

    public void back() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(imtSwap.this, ImtSmartGraphLayout.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(imtSwap.this, ImtSmartGraphLayout.class);
        startActivity(intent);
    }


}