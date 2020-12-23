package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;

import org.w3c.dom.Text;

public class Top_up_Money extends AppCompatActivity {
    ImageView imageView;
    CardView add_money;
    CheckBox checkBox;
    EditText enter_amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up__money);
        imageView =findViewById(R.id.back);
        add_money=findViewById(R.id.add_money);
        checkBox=findViewById(R.id.checkbox);
        enter_amount=findViewById(R.id.enter_add_money_amont);

        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Complate_payment.class));
                finish();
            }
        });
        back();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Top_up_Money.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Top_up_Money.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
}