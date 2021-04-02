package com.crypto.croytowallet.ImtSmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SwapSharedPrefernce;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.kaopiz.kprogresshud.KProgressHUD;

public class SwapAcknowledgement extends AppCompatActivity {
Button okay;
    SwapModel swapModel;
    UserData userData;
    String sendData,receivedData,coinPrice,currencyType,currencySymbols,enterAmount,coinAmount,Token,ethAddress;
    int value;
    KProgressHUD progressDialog;
    TextView coinValue,showCoinAmount,showEnteredAmount,amount_in_crypto,amount_in_Currency,trans_hash;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_acknowledgement);

        okay = findViewById(R.id.okay_btn);
       coinValue = findViewById(R.id.price_show);
        showCoinAmount = findViewById(R.id.cryto_amount);
        showEnteredAmount = findViewById(R.id.amount);
        amount_in_crypto = findViewById(R.id.amount_in_crypto);
        amount_in_Currency = findViewById(R.id.amount_in_currency);
        trans_hash = findViewById(R.id.trans_hash_id);

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


        coinValue.setText("1 "+sendData.toUpperCase()+" = "+coinPrice+" "+currencyType.toUpperCase());
        showCoinAmount.setText(coinAmount+" "+sendData.toUpperCase());
        showEnteredAmount.setText(enterAmount+" "+currencySymbols);
        amount_in_crypto.setText(coinAmount+" "+sendData.toUpperCase());
        amount_in_Currency.setText(enterAmount+" "+currencyType.toUpperCase());
        trans_hash.setText("P2038ruqu5727q8q");


        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SwapAcknowledgement.this, MainActivity.class));
                SwapSharedPrefernce.getInstance(getApplicationContext()).ClearSwapData();
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
}