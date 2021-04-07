package com.crypto.croytowallet.SetTransactionPin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.crypto.croytowallet.R;

import de.mateware.snacky.Snacky;

public class EnterDetails extends AppCompatActivity {
Button next;
String password,otp,currentPin,type;
EditText ed_password,ed_otp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_details);
        next =findViewById(R.id.nxt_btn);
        ed_password =findViewById(R.id.enter_pass);
        ed_otp =findViewById(R.id.enter_otp);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = ed_password.getText().toString().trim();
                otp = ed_otp.getText().toString().trim();
                Bundle bundle = getIntent().getExtras();
                 currentPin = bundle.getString("CurrentPin");
                 type = bundle.getString("Type");
                if (password.isEmpty() || otp.isEmpty()){
                    Snacky.builder()
                            .setActivity(EnterDetails.this)
                            .setText("Please Enter all Details")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }else{
                    Intent intent = new Intent(getApplicationContext(),NewPin.class);
                    intent.putExtra("CurrentPin1",currentPin);
                    intent.putExtra("Type1",type);
                    intent.putExtra("password",password);
                    intent.putExtra("otp",otp);
                    startActivity(intent);
                }
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