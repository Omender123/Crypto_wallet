package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;

public class Pay_money extends AppCompatActivity {
    ImageView imageView;
    CardView pay;
    EditText pay_enter_amount;
    TextView go_top_up,payUsername,payname,payEmail;
    SharedPreferences preferences;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_money);
        imageView =findViewById(R.id.back);
        pay_enter_amount=findViewById(R.id.pay_enter_amount);
        go_top_up=findViewById(R.id.go_top_up);
        pay=findViewById(R.id.pay);

        textView  = findViewById(R.id.balance);
        payUsername=findViewById(R.id.payUsername);
        payname=findViewById(R.id.name);
        payEmail=findViewById(R.id.email);

        preferences=getApplicationContext().getSharedPreferences("walletScan", Context.MODE_PRIVATE);
        String username = preferences.getString("username","");
        String name = preferences.getString("name","");
        String email = preferences.getString("email","");

        payUsername.setText(username);
        payname.setText(name);
        payEmail.setText(email);


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enter_amount=pay_enter_amount.getText().toString().trim();

              //  Toast.makeText(Pay_money.this, ""+enter_amount, Toast.LENGTH_SHORT).show();
               if (enter_amount.isEmpty()){
                    pay_enter_amount.setError("Please enter Amount to Pay");
                    pay_enter_amount.requestFocus();
                }else{

                   Intent intent =new Intent(getApplicationContext(),Enter_transaction_pin.class);
                 //  Double amount = Double.valueOf(enter_amount);
                   intent.putExtra("amount12",enter_amount);
                   startActivity(intent);
                }

            }
        });
//add money
       pay_enter_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = s.toString();

                if (amount.isEmpty()){
                    textView.setText("$ 0 ");
                }else{
                    Double a = Double.valueOf(s.toString());
                    double result = a*0.09;

                    textView.setText("$ " +result );

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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