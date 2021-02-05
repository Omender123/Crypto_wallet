package com.crypto.croytowallet.ImtSmart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.crypto.croytowallet.Activity.Setting;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;

import java.util.ArrayList;
import java.util.List;

public class imtSwap extends AppCompatActivity  {
    Spinner sendSpinner, reciveSpinner;
    String sendData, receviedData;
    ImageView imageView;
    String[] currency1 = {"select Currency ","IMT","Airdrop"};

    String[] currency2 = {"select Currency ","IMT","Airdrop"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imt_swap);
        sendSpinner = findViewById(R.id.sendSpinner);
        reciveSpinner = findViewById(R.id.recivedSpiner);
        imageView = findViewById(R.id.back);



        ArrayAdapter<String> aa = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, currency1);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sendSpinner.setAdapter(aa);
        sendSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sendData=currency1[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ArrayAdapter<String> bb = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, currency2);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reciveSpinner.setAdapter(bb);
        reciveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                receviedData=currency2[position];
             //   Toast.makeText(view.getContext(), "Your choose :" + currency1[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

            back();
    }

    public void back() {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(imtSwap.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(imtSwap.this, MainActivity.class);
        startActivity(intent);
    }


}