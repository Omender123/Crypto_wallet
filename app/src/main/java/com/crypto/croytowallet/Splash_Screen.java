package com.crypto.croytowallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.crypto.croytowallet.login.Login;

public class Splash_Screen extends AppCompatActivity {
    private static  int SPLASH_SCREEN=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash_Screen.this, Login.class));
                finish();
            }
        },SPLASH_SCREEN);
    }
}