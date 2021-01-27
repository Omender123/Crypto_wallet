package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.crypto.croytowallet.R;

public class Contact_details extends AppCompatActivity {
ImageView back_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        back_btn =findViewById(R.id.back);
        back();
    }
    public void back(){
      back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contact_details.this, Support.class);
                startActivity(intent);
            }
        });

    }
}