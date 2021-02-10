package com.crypto.croytowallet.ImtSmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Activity.SelectCurrency;
import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.Activity.Sync_device;
import com.crypto.croytowallet.Adapter.CustomSpinnerAdapter;
import com.crypto.croytowallet.Adapter.SelectCurrencyAdapter;
import com.crypto.croytowallet.AppUtils;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.CurrencyModel;
import com.crypto.croytowallet.Payment.Top_up_Money;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.login.Login;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mateware.snacky.Snacky;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class imtSwap extends AppCompatActivity implements View.OnClickListener {
    Spinner sendSpinner, reciveSpinner;
    String sendData, receviedData,SwapAmount,low_gasFees,average_gasFees,high_gasFees;
    ImageView imageView,img_low,img_average,img_high;
    TextView swapBtn,txt_low,txt_average,txt_high,gwei_low,gwei_average,gwei_high,min_low,min_average,min_high;
    LinearLayout lyt_low,lyt_average,lyt_high;
    EditText enter_Swap_Amount;
    String [] coinName ={"ImSmart","Airdrop"};
    String [] coinSymbols ={"imt","airdrop"};
    int [] coinImage = {R.mipmap.imt,R.mipmap.airdrop};

    String [] coinName1 ={"Airdrop","ImSmart"};
    String [] coinSymbols1 ={"airdrop","imt"};
    int [] coinImage1 = {R.mipmap.airdrop,R.mipmap.imt};
    int value ;
    SeekBar seekBar;

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
         lyt_low = findViewById(R.id.lyt_low);
         lyt_average = findViewById(R.id.lyt_average);
         lyt_high = findViewById(R.id.lyt_high);
         txt_low = findViewById(R.id.txt_low);
         txt_average = findViewById(R.id.txt_average);
         txt_high = findViewById(R.id.txt_high);
        img_low = findViewById(R.id.img_low);
        img_average = findViewById(R.id.img_average);
        img_high = findViewById(R.id.img_high);
        gwei_low = findViewById(R.id.gwei_low);
        gwei_average = findViewById(R.id.gwei_average);
        gwei_high = findViewById(R.id.gwei_high);
        min_low = findViewById(R.id.min_low);
        min_average = findViewById(R.id.min_average);
        min_high = findViewById(R.id.min_high);
        seekBar = findViewById(R.id.seekbar02);
         lyt_low.setOnClickListener(this);
         lyt_average.setOnClickListener(this);
        lyt_high.setOnClickListener(this);


        CustomSpinnerAdapter customAdapter=new CustomSpinnerAdapter(getApplicationContext(),coinImage,coinName,coinSymbols);
        sendSpinner.setAdapter(customAdapter);
        sendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendData=coinSymbols[position];
              //  Toast.makeText(view.getContext(), sendData,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CustomSpinnerAdapter customAdapter1=new CustomSpinnerAdapter(getApplicationContext(),coinImage1,coinName1,coinSymbols1);

        reciveSpinner.setAdapter(customAdapter1);
        reciveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                receviedData=coinSymbols1[position];
               // Toast.makeText(view.getContext(), receviedData,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


      back();
        GET_GAS();
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
                      // Log.d("datat",sendData+receviedData+String.valueOf(value)+SwapAmount);
                    }
                }
            });

            if(txt_low.getText().toString().equals("Low")){
                value =1;
            }else if(txt_low.getText().toString().equals("Average")){
                value =2;
            }else if(txt_low.getText().toString().equals("High")){
                value =3;
            }


            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    double balance2 = Double.parseDouble(String.valueOf(progress));
                    double total = balance2*100;
                    enter_Swap_Amount.setText(String.valueOf(total));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

   // Toast.makeText(this, ""+value, Toast.LENGTH_SHORT).show();
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

        Call<ResponseBody>call = RetrofitClient.getInstance().getApi().IMT_SWAP(Token,sendData,receviedData,value,SwapAmount,"",eth_Address);


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
                          Toast.makeText(imtSwap.this, "Error  occurred in Transaction", Toast.LENGTH_SHORT).show();
                        }else {
                            startActivity(new Intent(getApplicationContext(), ImtSmartGraphLayout.class));
                            Toast.makeText(imtSwap.this, " Successfully \t"+sendData+"\t to \t"+receviedData, Toast.LENGTH_SHORT).show();
                        }



                    // Toast.makeText(imtSwap.this, ""+s, Toast.LENGTH_SHORT).show();

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
            /*  Snacky.builder()
                        .setActivity(imtSwap.this)
                        .setText("Please Check Your Internet Connection")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();*/

              /*  startActivity(new Intent(getApplicationContext(), ImtSmartGraphLayout.class));
                Toast.makeText(imtSwap.this, "Your Amount is Not detected ", Toast.LENGTH_SHORT).show();*/

                AppUtils.showMessageOKCancel("Your transaction is in process. Kindly check again for the confirmation.", imtSwap.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(imtSwap.this, ImtSmartGraphLayout.class);
                        startActivity(intent);
                        finish();
                    }
                });

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


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.lyt_low:
                lyt_average.setBackground(null);
                lyt_low.setBackground(getResources().getDrawable(R.drawable.backgorund_border1));
                lyt_high.setBackground(null);
                txt_average.setTextColor(getResources().getColor(R.color.black));
                txt_low.setTextColor(getResources().getColor(R.color.white));
                txt_high.setTextColor(getResources().getColor(R.color.black));
                img_high.setVisibility(View.GONE);
                img_low.setVisibility(View.VISIBLE);
                img_average.setVisibility(View.GONE);
                gwei_low.setTextColor(getResources().getColor(R.color.toolbar_text_color));
                gwei_average.setTextColor(getResources().getColor(R.color.txt_hide));
                gwei_high.setTextColor(getResources().getColor(R.color.txt_hide));
                min_low.setTextColor(getResources().getColor(R.color.light_gray));
                min_average.setTextColor(getResources().getColor(R.color.txt_hide));
                min_high.setTextColor(getResources().getColor(R.color.txt_hide));
                value =1;
              //  Toast.makeText(this, ""+value, Toast.LENGTH_SHORT).show();
               break;

            case R.id.lyt_average:
                lyt_average.setBackgroundColor(getResources().getColor(R.color.purple_500));
                lyt_low.setBackground(null);
                lyt_high.setBackground(null);
                txt_average.setTextColor(getResources().getColor(R.color.white));
                txt_low.setTextColor(getResources().getColor(R.color.black));
                txt_high.setTextColor(getResources().getColor(R.color.black));
                img_high.setVisibility(View.GONE);
                img_low.setVisibility(View.GONE);
                img_average.setVisibility(View.VISIBLE);
                gwei_low.setTextColor(getResources().getColor(R.color.txt_hide));
                gwei_average.setTextColor(getResources().getColor(R.color.toolbar_text_color));
                gwei_high.setTextColor(getResources().getColor(R.color.txt_hide));
                min_low.setTextColor(getResources().getColor(R.color.txt_hide));
                min_average.setTextColor(getResources().getColor(R.color.light_gray));
                min_high.setTextColor(getResources().getColor(R.color.txt_hide));
                value =2;
            //   Toast.makeText(this, ""+value, Toast.LENGTH_SHORT).show();
                break;

            case R.id.lyt_high:
                lyt_low.setBackground(null);
                lyt_average.setBackground(null);
                lyt_high.setBackground(getResources().getDrawable(R.drawable.background_border2));
                txt_high.setTextColor(getResources().getColor(R.color.white));
                txt_average.setTextColor(getResources().getColor(R.color.black));
                txt_low.setTextColor(getResources().getColor(R.color.black));
                img_high.setVisibility(View.VISIBLE);
                img_low.setVisibility(View.GONE);
                img_average.setVisibility(View.GONE);

                gwei_low.setTextColor(getResources().getColor(R.color.txt_hide));
                gwei_average.setTextColor(getResources().getColor(R.color.txt_hide));
                gwei_high.setTextColor(getResources().getColor(R.color.toolbar_text_color));
                min_low.setTextColor(getResources().getColor(R.color.txt_hide));
                min_average.setTextColor(getResources().getColor(R.color.txt_hide));
                min_high.setTextColor(getResources().getColor(R.color.light_gray));
                value = 3;
         //      Toast.makeText(this, ""+value, Toast.LENGTH_SHORT).show();
                break;

        }
    }
public void GET_GAS(){
    UserData userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

    String Token = userData.getToken();

    StringRequest stringRequest=new StringRequest(Request.Method.GET, URLs.URL_GET_GAS_FEES ,new com.android.volley.Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            try {
                JSONObject object = new JSONObject(response);
                String response1 = object.getString("GASFEE");

                JSONObject object1 = new JSONObject(response1);
                low_gasFees = object1.getString("SafeGasPrice");
                average_gasFees = object1.getString("ProposeGasPrice");
                high_gasFees = object1.getString("FastGasPrice");

                gwei_low.setText(low_gasFees+" gwei");
                gwei_average.setText(average_gasFees+" gwei");
                gwei_high.setText(high_gasFees+" gwei");
            } catch (JSONException e) {
                e.printStackTrace();
            }



        //  Toast.makeText(getApplicationContext(), ""+response, Toast.LENGTH_SHORT).show();
        }
    }, new com.android.volley.Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error == null || error.networkResponse == null) {
                return;
            }

            String body;
            //get status code here
            final String statusCode = String.valueOf(error.networkResponse.statusCode);
            //get response body and parse with appropriate encoding
            try {
                body = new String(error.networkResponse.data,"UTF-8");

                Toast.makeText(getApplicationContext(), ""+body, Toast.LENGTH_SHORT).show();
            } catch (UnsupportedEncodingException e) {

            }


          //  Toast.makeText(getApplicationContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
        }
    }){

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String> headers = new HashMap<String, String>();

            headers.put("Authorization", Token);

            return headers;
        }


    };

    VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

   }

}