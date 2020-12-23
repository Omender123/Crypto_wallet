package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.StringRequest;
import com.chaos.view.PinView;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;

public class Enter_transaction_pin extends AppCompatActivity {
PinView pinView;
CardView pay_money;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_transaction_pin);
        imageView =findViewById(R.id.back);
        pinView = findViewById(R.id.enter_pin);
        pay_money = findViewById(R.id.pay_money);

        pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enterPin=pinView.getText().toString();

                Toast.makeText(Enter_transaction_pin.this, ""+enterPin, Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(),Complate_payment.class));
                finish();
            }
        });

        back();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Enter_transaction_pin.this, Top_up_Money.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Enter_transaction_pin.this, Top_up_Money.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}