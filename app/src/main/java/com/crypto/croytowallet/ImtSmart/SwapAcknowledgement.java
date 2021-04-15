package com.crypto.croytowallet.ImtSmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.Model.SwapRespoinseModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SwapResponsePrefernce;
import com.crypto.croytowallet.SharedPrefernce.SwapSharedPrefernce;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.kaopiz.kprogresshud.KProgressHUD;

import de.mateware.snacky.Snacky;

public class SwapAcknowledgement extends AppCompatActivity {
Button okay;
    SwapModel swapModel;
    UserData userData;
    String sendData,receivedData,coinPrice,currencyType,currencySymbols,enterAmount,coinAmount,Token,ethAddress,status,type;
    int value;
    KProgressHUD progressDialog;
    TextView coinValue,showCoinAmount,showEnteredAmount,amount_in_crypto,amount_in_Currency,trans_hash,trans_status,btncopy,reciverName;
    SwapRespoinseModel   swapRespoinseModel;
    ImageView statusImage;
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
        trans_status = findViewById(R.id.status);
        btncopy  =findViewById(R.id.btn_copy);
        statusImage = findViewById(R.id.statusImage);
        reciverName = findViewById(R.id.recriver_address);

        swapModel = SwapSharedPrefernce.getInstance(getApplicationContext()).getSwapData();
        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        swapRespoinseModel = SwapResponsePrefernce.getInstance(getApplicationContext()).getData();
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
        status = swapRespoinseModel.getStatus();
        type = swapModel.getType();



     //   coinValue.setText("1 "+sendData.toUpperCase()+" = "+coinPrice+" "+currencyType.toUpperCase());
      //  showCoinAmount.setText(coinAmount+" "+sendData.toUpperCase());
        showEnteredAmount.setText(enterAmount+" "+currencySymbols);
      //  amount_in_crypto.setText(coinAmount+" "+sendData.toUpperCase());
        amount_in_Currency.setText(enterAmount+" "+currencyType.toUpperCase());
        trans_hash.setText(swapRespoinseModel.getTransId());

        if(sendData.equals("airdrop")){
            showCoinAmount.setText(coinAmount+" IMT-U");
            amount_in_crypto.setText(coinAmount+" IMT-U");
            coinValue.setText("1 IMT-U = "+coinPrice+" "+currencyType.toUpperCase());
        }else{
            showCoinAmount.setText(coinAmount+" "+sendData.toUpperCase());
            coinValue.setText("1 "+sendData.toUpperCase()+" = "+coinPrice+" "+currencyType.toUpperCase());
            amount_in_crypto.setText(coinAmount+" "+sendData.toUpperCase());

        }

        if (type.equalsIgnoreCase("Swap")){
            reciverName.setText("Admin");
        }else{
            reciverName.setText(swapModel.getReceivedData());
        }



        if (status.equalsIgnoreCase("true")){
            trans_status.setText("Done");
        }else {
            trans_status.setText("Pending");
            statusImage.setImageResource(R.drawable.ic_baseline_panding_24);
        }


        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(SwapAcknowledgement.this, MainActivity.class));
                SwapSharedPrefernce.getInstance(getApplicationContext()).ClearSwapData();
                SwapResponsePrefernce.getInstance(getApplicationContext()).ClearData();
            }
        });

        btncopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(trans_hash.getText().toString());
               // Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();
                Snacky.builder()
                        .setActivity(SwapAcknowledgement.this)
                        .setText("Copied")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .success()
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
}