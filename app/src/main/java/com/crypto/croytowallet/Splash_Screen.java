package com.crypto.croytowallet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crypto.croytowallet.login.Login;

public class Splash_Screen extends AppCompatActivity {
    private static  int SPLASH_SCREEN=1500;
    ImageView imageView,logo,left,right;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        logo=findViewById(R.id.logo);
        layout=findViewById(R.id.splash);
        imageView=findViewById(R.id.image_view);
        left=findViewById(R.id.left_image);
        right=findViewById(R.id.right_image);

        Animation animation_left = AnimationUtils.loadAnimation(this,R.anim.slide_in_left);
        left.setAnimation(animation_left);
        Animation animation_right = AnimationUtils.loadAnimation(this,R.anim.slide_in_right);
        right.setAnimation(animation_right);

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.toptobottom);
        layout.setAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash_Screen.this, Login.class));
                finish();
            }
        },SPLASH_SCREEN);
    }
}