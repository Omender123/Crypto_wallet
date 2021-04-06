package com.crypto.croytowallet.ImtSmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.AppUtils;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SwapSharedPrefernce;
import com.crypto.croytowallet.SharedPrefernce.UserData;
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

public class SwapConfirmation extends AppCompatActivity {
    Button confirm;
    SwapModel swapModel;
    UserData userData;
    String sendData,receivedData,coinPrice,currencyType,currencySymbols,enterAmount,coinAmount,Token,ethAddress;
    int value;
    KProgressHUD progressDialog;
    TextView coinValue,showCoinAmount,showEnteredAmount;
    String balance,total;
    Double userBalance,TotalAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_confirmation);
        confirm = findViewById(R.id.confirm_btn);
        coinValue = findViewById(R.id.price_show);
        showCoinAmount = findViewById(R.id.cryto_amount);
        showEnteredAmount = findViewById(R.id.amount);

        swapModel = SwapSharedPrefernce.getInstance(getApplicationContext()).getSwapData();
        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        Token = userData.getToken();
        ethAddress = userData.getETH();

        sendData = swapModel.getSendData();
        receivedData = swapModel.getReceivedData();
        coinPrice = swapModel.getCoinPrice();
        currencyType = swapModel.getCurrencyType();
        currencySymbols = swapModel.getCurrencySymbol();
        enterAmount = swapModel.getEnterAmount();
        coinAmount = swapModel.getCoinAmount();
        value = swapModel.getValue();
        balance = swapModel.getUserBalance();
        total = swapModel.getTotalAmount();




        coinValue.setText("1 "+sendData.toUpperCase()+" = "+coinPrice+" "+currencyType.toUpperCase());
        if(sendData.equals("airdrop") || receivedData.equals("airdrop")){
            showCoinAmount.setText(coinAmount+" IMT-U");
        }else{
            showCoinAmount.setText(coinAmount+" "+sendData.toUpperCase());
        }



        showEnteredAmount.setText(enterAmount+" "+currencySymbols);


         try {
            userBalance = Double.parseDouble(balance);
            TotalAmount = Double.parseDouble(total);

          /*  if(TotalAmount>=userBalance) {

                confirm.setBackgroundColor(getResources().getColor(R.color.light_gray));
                confirm.setAlpha(0.5f);
            }else{
                confirm.setBackgroundColor(getResources().getColor(R.color.purple_500));
                confirm.setAlpha(0.9f);
            }*/
        }catch (Exception e){

           /* Snacky.builder()
                    .setActivity(SwapConfirmation.this)
                    .setText(" User Balance Not Found")
                    .setDuration(Snacky.LENGTH_SHORT)
                    .setActionText(android.R.string.ok)
                    .error()
                    .show();*/
        }


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TotalAmount>=userBalance){
                    Snacky.builder()
                            .setActivity(SwapConfirmation.this)
                            .setText(" Inefficient balance")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else {


                    SwapApi();
                }

                //

               // Toast.makeText(SwapConfirmation.this, total+" <= "+balance, Toast.LENGTH_SHORT).show();


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
     public void SwapApi() {

          progressDialog = KProgressHUD.create(SwapConfirmation.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

      showpDialog();




        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().IMT_SWAP(Token, sendData, receivedData, value, coinAmount, userData.getTransaction_Pin(), ethAddress);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                hidepDialog();
                if (response.code() == 200) {

                    try {
                        s = response.body().string();

                        if (s == null) {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            Toast.makeText(getApplicationContext(), "Error  occurred in Transaction", Toast.LENGTH_SHORT).show();
                        } else {
                               Intent intent = new Intent(SwapConfirmation.this,SwapAcknowledgement.class);
                               startActivity(intent);
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
                                .setActivity(SwapConfirmation.this)
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
                            .setActivity(SwapConfirmation.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                } else if (response.code() == 504) {
                    Snacky.builder()
                            .setActivity(SwapConfirmation.this)
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


                AppUtils.showMessageOKCancel("Your transaction is in process. Kindly check again for the confirmation.", SwapConfirmation.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

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

}