package com.crypto.croytowallet.SetTransactionPin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.crypto.croytowallet.R;

import de.mateware.snacky.Snacky;

public class NewPin extends AppCompatActivity {
PinView newPin,confirmPin;
Button confirm;
String newPintxt,confirmPintxt,currentPin,password,otp,type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pin);
        newPin = findViewById(R.id.enterNew_pin);
        confirmPin = findViewById(R.id.enter_confirm_pin);
        confirm = findViewById(R.id.confirm_btn);

       try {
           Bundle bundle = getIntent().getExtras();
           currentPin = bundle.getString("CurrentPin1");
           type = bundle.getString("Type1");
           password = bundle.getString("password");
           otp = bundle.getString("otp");

       }catch (Exception e){

       }



        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newPintxt = newPin.getText().toString();
                confirmPintxt = confirmPin.getText().toString();

                if (newPintxt.isEmpty() || confirmPintxt.isEmpty()){
                    Snacky.builder()
                            .setActivity(NewPin.this)
                            .setText("Please Enter all Details")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else if(!newPintxt.equals(confirmPintxt)){
                    Snacky.builder()
                            .setActivity(NewPin.this)
                            .setText(" New Transaction Pin Not Match")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else{


                    Toast.makeText(NewPin.this, ""+currentPin+password+otp+type+newPintxt, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}