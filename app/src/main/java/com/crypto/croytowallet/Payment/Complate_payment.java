package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.PearToPearModel;
import com.crypto.croytowallet.SharedPrefernce.PearToPearSharedPrefManager;

public class Complate_payment extends AppCompatActivity {
LottieAnimationView lottieAnimationView;
    ImageView imageView;
    CardView done;
    TextView success;
    PearToPearModel pearToPearModel;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complate_payment);
        imageView =findViewById(R.id.back);
        lottieAnimationView = findViewById(R.id.animationView);

        pearToPearModel= PearToPearSharedPrefManager.getInstance(getApplicationContext()).getPearToPear();
        preferences=getApplicationContext().getSharedPreferences("walletScan", Context.MODE_PRIVATE);
        String username = preferences.getString("username","");

      //  pearToPearModel.getStatus();
       // pearToPearModel.getAmtOfCrypto();
        Bundle bundle = getIntent().getExtras();


        success=findViewById(R.id.success);
        try {
            String status = bundle.getString("status");
            String amount = bundle.getString("amt");
            success.setText("Payment of "+amount+" To " +username+"\n"+status);
        }catch (Exception e){
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }


       /* success=findViewById(R.id.success);
            success.setText("Payment of "+pearToPearModel.getAmtOfCrypto()+" To " +username+"\n"+pearToPearModel.getStatus());
*/

                done=findViewById(R.id.done);
                done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Complate_payment.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        back();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Complate_payment.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Complate_payment.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}