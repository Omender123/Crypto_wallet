package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;

public class Pay_money extends AppCompatActivity {
    ImageView imageView;
    CardView pay;
    EditText pay_enter_amount;
    TextView go_top_up,payUsername;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_money);
        imageView =findViewById(R.id.back);
        pay_enter_amount=findViewById(R.id.pay_enter_amount);
        go_top_up=findViewById(R.id.go_top_up);
        pay=findViewById(R.id.pay);

        payUsername=findViewById(R.id.payUsername);

        preferences=getApplicationContext().getSharedPreferences("walletScan", Context.MODE_PRIVATE);
        String username = preferences.getString("username","");
        payUsername.setText(username);

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enter_amount=pay_enter_amount.getText().toString().trim();
                if (enter_amount.isEmpty()){
                    pay_enter_amount.setError("Please enter Amount to Pay");
                    pay_enter_amount.requestFocus();
                }else{
                    startActivity(new Intent(getApplicationContext(),Enter_transaction_pin.class).putExtra("amount12",enter_amount));
                    finish();
                }

            }
        });
//add money
        go_top_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Top_up_Money.class));
                finish();
            }
        });

        back();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                /*Intent intent = new Intent(Pay_money.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);*/
            }
        });

    }
}