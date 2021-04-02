package com.crypto.croytowallet.fragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crypto.croytowallet.Adapter.CustomSpinnerAdapter;
import com.crypto.croytowallet.AppUtils;
import com.crypto.croytowallet.ImtSmart.ImtSmartGraphLayout;
import com.crypto.croytowallet.ImtSmart.SwapConfirmation;
import com.crypto.croytowallet.ImtSmart.imtSwap;
import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SwapSharedPrefernce;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.VolleyDatabase.VolleySingleton;
import com.crypto.croytowallet.database.RetrofitClient;
import com.crypto.croytowallet.database.RetrofitGraph;
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

public class Exchange extends Fragment implements View.OnClickListener {
    Spinner sendSpinner, reciveSpinner;
    String sendData, receviedData, SwapAmount, low_gasFees, average_gasFees, high_gasFees, min_amount, half_amount, max_amount,priceCoinId,coinPrice;
    ImageView img_low, img_average, img_high;
    TextView swapBtn, txt_low, txt_average, txt_high, gwei_low, gwei_average, gwei_high, min_low, min_average, min_high, min_rate, half_rate, max_rate;
    LinearLayout lyt_low, lyt_average, lyt_high;
    EditText enter_Swap_Amount;
    String[] coinName = {"ImSmart", "Bitcoin","Ethereum","Tether","XRP","Litecoin","USD Coin","ImSmart Utility"};
    String[] coinSymbols = {"IMT", "BTC","ETH","USDT","XRP","LTC","USDC","IMT-U"};
    String[] coinId = {"imt", "btc","eth","usdt","xrp","ltc","usdc","airdrop"};
    String[] PricecoinId = {"imsmart-token", "bitcoin","ethereum","tether","ripple","litecoin","usd-coin","airdrop"};
    int[] coinImage = {R.mipmap.imt,R.mipmap.bitcoin_image,R.mipmap.group_blue,R.mipmap.usdt,R.mipmap.xrp,R.mipmap.ltc,R.mipmap.usdc,R.drawable.ic_imt__u};

    String[] coinName1 = {"ImSmart Utility","ImSmart"};
    String[] coinSymbols1 = {"IMT-U","IMT"};
    String[] coinId1 = {"airdrop","imt"};
    String[] PricecoinId1 = {"airdrop","imt"};
    int[] coinImage1 = {R.drawable.ic_imt__u,R.mipmap.imt};
    int value,userBalance;
    SeekBar seekBar;

    KProgressHUD progressDialog;
    SharedPreferences sharedPreferences;
    String currency2,CurrencySymbols,token;
    UserData userData;


       public Exchange() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_exchange, container, false);


        sendSpinner = view.findViewById(R.id.sendSpinner);
        reciveSpinner = view.findViewById(R.id.recivedSpiner);
        enter_Swap_Amount = view.findViewById(R.id.enter_swap);
        swapBtn = view.findViewById(R.id.swap_btn);
        lyt_low = view.findViewById(R.id.lyt_low);
        lyt_average = view.findViewById(R.id.lyt_average);
        lyt_high = view.findViewById(R.id.lyt_high);
        txt_low = view.findViewById(R.id.txt_low);
        txt_average = view.findViewById(R.id.txt_average);
        txt_high = view.findViewById(R.id.txt_high);
        img_low = view.findViewById(R.id.img_low);
        img_average = view.findViewById(R.id.img_average);
        img_high = view.findViewById(R.id.img_high);
        gwei_low = view.findViewById(R.id.gwei_low);
        gwei_average = view.findViewById(R.id.gwei_average);
        gwei_high = view.findViewById(R.id.gwei_high);
        min_low = view.findViewById(R.id.min_low);
        min_average = view.findViewById(R.id.min_average);
        min_high = view.findViewById(R.id.min_high);
        seekBar = view.findViewById(R.id.seekbar02);
        min_rate = view.findViewById(R.id.min);
        half_rate = view.findViewById(R.id.half);
        max_rate = view.findViewById(R.id.max);
        min_rate.setOnClickListener(this);
        half_rate.setOnClickListener(this);
        max_rate.setOnClickListener(this);

        lyt_low.setOnClickListener(this);
        lyt_average.setOnClickListener(this);
        lyt_high.setOnClickListener(this);

        userData = SharedPrefManager.getInstance(getContext()).getUser();
        token = userData.getToken();
        sharedPreferences =getActivity().getSharedPreferences("currency",0);
        currency2 =sharedPreferences.getString("currency1","usd");
        CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");


        CustomSpinnerAdapter customAdapter = new CustomSpinnerAdapter(getContext(), coinImage, coinName, coinSymbols,coinId,PricecoinId);
        sendSpinner.setAdapter(customAdapter);
        sendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                sendData = coinId[position];
                priceCoinId = PricecoinId[position];
                AirDropBalance(token,sendData,currency2);

                  if(sendData.equals(receviedData)){
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("You can't select the same currency for Swap ")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else if (priceCoinId.equals("airdrop")){

                  }else{
                      
                      String coinid=priceCoinId.toLowerCase();
                      String currency=currency2.toLowerCase();
                      
                     getCoinPrice(coinid,currency);

                  }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        CustomSpinnerAdapter customAdapter1 = new CustomSpinnerAdapter(getContext(), coinImage1, coinName1, coinSymbols1,coinId1,PricecoinId1);

        reciveSpinner.setAdapter(customAdapter1);
        reciveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                receviedData = coinId1[position];
                // Toast.makeText(view.getContext(), receviedData,Toast.LENGTH_SHORT).show();

                if(sendData.equals(receviedData)){
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("You can't select the same currency for Swap ")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        GET_GAS();
        swapBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                SwapAmount = enter_Swap_Amount.getText().toString().trim();

                int mini = Integer.parseInt(min_amount);
                int to = --mini ;

                if (SwapAmount.isEmpty()) {
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("Please enter Swap Amount")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                } else if(sendData.equals(receviedData)){
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("You can't select the same currency for Swap ")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                     }/*else if(Integer.parseInt(SwapAmount)<=to){
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("Please enter  the minimum amount of  "+min_amount)
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }*/ else if(Integer.parseInt(SwapAmount)>=userBalance){
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText(" Inefficient balance")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();



                }else if(priceCoinId.equals("airdrop")){
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText(" Coming Soon Airdrop Swap")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else {

                    DecimalFormat df = new DecimalFormat();
                    df.setMaximumFractionDigits(2);

                    Double coinprices,enterAmount,totalAmoumt;
                    coinprices=Double.parseDouble(coinPrice);
                    enterAmount=Double.parseDouble(SwapAmount);

                    totalAmoumt = enterAmount/coinprices;

                  String coinAmount = String.valueOf(totalAmoumt);

                    SwapModel swapModel = new SwapModel(sendData,receviedData,coinPrice,currency2,CurrencySymbols,coinAmount,SwapAmount,value);
                    SwapSharedPrefernce.getInstance(getContext()).SetData(swapModel);


                      Intent intent = new Intent(getActivity(), SwapConfirmation.class);
                    startActivity(intent);

                }
            }
        });

        if (txt_low.getText().toString().equals("Low")) {
            value = 1;
        } else if (txt_low.getText().toString().equals("Average")) {
            value = 2;
        } else if (txt_low.getText().toString().equals("High")) {
            value = 3;
        }


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
               // double balance2 = Double.parseDouble(String.valueOf(progress));

                int total = progress * 10;
                enter_Swap_Amount.setText(String.valueOf(total));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        GET_AMOUNT();



        return view; }


    public void getCoinPrice(String coinId,String currency) {

           String url = "https://api.coingecko.com/api/v3/simple/price?ids="+coinId+"&vs_currencies="+currency;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    String id = object.getString(coinId);
                    JSONObject object1 = new JSONObject(id);
                    coinPrice = object1.getString(currency);


                } catch (JSONException e) {
                    e.printStackTrace();
                }



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
                    body = new String(error.networkResponse.data, "UTF-8");

                    Toast.makeText(getContext(), "" + body, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {

                }


                //  Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
       }

   /* public void SwapApi() {

        UserData userData = SharedPrefManager.getInstance(getContext()).getUser();

        String Token = userData.getToken();
        String eth_Address = userData.getETH();
        progressDialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

      showpDialog();




        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().IMT_SWAP(Token, sendData, receviedData, value, SwapAmount, "", eth_Address);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                hidepDialog();
                if (response.code() == 200) {

                    try {
                        s = response.body().string();

                        if (s == null) {
                            startActivity(new Intent(getContext(), ImtSmartGraphLayout.class));
                            Toast.makeText(getContext(), "Error  occurred in Transaction", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(getContext(), ImtSmartGraphLayout.class));
                            Toast.makeText(getContext(), " Successfully \t" + sendData + "\t to \t" + receviedData, Toast.LENGTH_SHORT).show();
                        }


                        // Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 400) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(getActivity())
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {
                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                } else if (response.code() == 504) {
                    Snacky.builder()
                            .setActivity(getActivity())
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
            *//*  Snacky.builder()
                        .setActivity(getContext())
                        .setText("Please Check Your Internet Connection")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();

                startActivity(new Intent(getContext(), ImtSmartGraphLayout.class));
                Toast.makeText(getContext(), "Your Amount is Not detected ", Toast.LENGTH_SHORT).show();
*//*
                AppUtils.showMessageOKCancel("Your transaction is in process. Kindly check again for the confirmation.", getActivity(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getContext(), ImtSmartGraphLayout.class);
                        startActivity(intent);

                    }
                });

            }
        });


    }*/

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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
                value = 1;
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
                value = 2;
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

            case R.id.min:
                min_rate.setBackground(getResources().getDrawable(R.drawable.round_background));
                half_rate.setBackground(null);
                max_rate.setBackground(null);
                min_rate.setTextColor(getResources().getColor(R.color.white));
                half_rate.setTextColor(getResources().getColor(R.color.black));
                max_rate.setTextColor(getResources().getColor(R.color.black));
                enter_Swap_Amount.setText(min_amount);
                Integer min=Integer.parseInt(min_amount);
                int min1=min/10;
                seekBar.setProgress(min1);
                break;

            case R.id.half:
                min_rate.setBackground(null);
                half_rate.setBackground(getResources().getDrawable(R.drawable.round_background));
                max_rate.setBackground(null);
                min_rate.setTextColor(getResources().getColor(R.color.black));
                half_rate.setTextColor(getResources().getColor(R.color.white));
                max_rate.setTextColor(getResources().getColor(R.color.black));
                enter_Swap_Amount.setText(half_amount);
                Integer min2=Integer.parseInt(half_amount);
                int min3=min2/10;
                seekBar.setProgress(min3);
                // seekBar.setProgress(Integer.parseInt(half_amount));
                break;

            case R.id.max:
                min_rate.setBackground(null);
                half_rate.setBackground(null);
                max_rate.setBackground(getResources().getDrawable(R.drawable.round_background));
                min_rate.setTextColor(getResources().getColor(R.color.black));
                half_rate.setTextColor(getResources().getColor(R.color.black));
                max_rate.setTextColor(getResources().getColor(R.color.white));
                enter_Swap_Amount.setText(max_amount);
                Integer min4=Integer.parseInt(max_amount);
                int min5=min4/10;
                seekBar.setProgress(min5);

                // seekBar.setProgress(Integer.parseInt(max_amount));
                break;

        }
    }

    public void GET_GAS() {
        UserData userData = SharedPrefManager.getInstance(getContext()).getUser();

        String Token = userData.getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET_GAS_FEES, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject object = new JSONObject(response);
                    String response1 = object.getString("GASFEE");

                    JSONObject object1 = new JSONObject(response1);
                    low_gasFees = object1.getString("SafeGasPrice");
                    average_gasFees = object1.getString("ProposeGasPrice");
                    high_gasFees = object1.getString("FastGasPrice");

                    gwei_low.setText(low_gasFees + " gwei");
                    gwei_average.setText(average_gasFees + " gwei");
                    gwei_high.setText(high_gasFees + " gwei");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //  Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
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
                    body = new String(error.networkResponse.data, "UTF-8");

                    Toast.makeText(getContext(), "" + body, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {

                }


                //  Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", Token);

                return headers;
            }


        };

        VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);

    }

    public void GET_AMOUNT() {
        UserData userData = SharedPrefManager.getInstance(getContext()).getUser();

        String Token = userData.getToken();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET_AMOUNT, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);

                    min_amount = object.getString("min");
                    half_amount = object.getString("half");
                    max_amount = object.getString("max");

                } catch (JSONException e) {
                    e.printStackTrace();
                }


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
                    body = new String(error.networkResponse.data, "UTF-8");

                    Toast.makeText(getContext(), "" + body, Toast.LENGTH_SHORT).show();
                } catch (UnsupportedEncodingException e) {

                }


                //  Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("Authorization", Token);

                return headers;
            }


        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }
    public void AirDropBalance(String token,String coinType,String currency){

        progressDialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();


        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().AirDropBalance(token,coinType,currency);

        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                String s =null;

                hidepDialog();

                if (response.code()==200){
                    try {
                        s=response.body().string();

                        JSONObject object = new JSONObject(s);
                        userBalance = object.getInt("balance");


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==400){
                    try {
                        s=response.errorBody().string();
                        JSONObject jsonObject1=new JSONObject(s);
                        String error =jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(getActivity())
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if(response.code()==401){

                    Snacky.builder()
                            .setActivity(getActivity())
                            .setText("unAuthorization Request")
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
                        .setActivity(getActivity())
                        .setText(t.getMessage())
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .error()
                        .show();
            }
        });
    }
}