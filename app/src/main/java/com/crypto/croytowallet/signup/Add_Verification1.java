package com.crypto.croytowallet.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.crypto.croytowallet.R;

public class Add_Verification1 extends AppCompatActivity {
CheckBox checkgoogle;
    Button skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__verification1);

        checkgoogle = findViewById(R.id.checkbox2);
        skip =findViewById(R.id.show_dailog);

        checkgoogle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked){
                    startActivity(new Intent(getApplicationContext(),Google_auth.class));
                }
            }
        });


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),sucessfullverification.class));
            }
        });
    }


}