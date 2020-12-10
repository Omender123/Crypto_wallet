package com.crypto.croytowallet.signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.TransactionPin.TransactionPin;
import com.crypto.croytowallet.login.ForgetPassword;
import com.crypto.croytowallet.login.Login;

public class SignUp extends AppCompatActivity {
    Animation fade_in;
    ConstraintLayout constraintLayout;
    Button ready_to1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // animation
        constraintLayout =findViewById(R.id.signup1);
        fade_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        constraintLayout.startAnimation(fade_in);

        ready_to1=findViewById(R.id.ready_to1);
        ready_to1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* startActivity(new Intent(getApplicationContext(), TransactionPin.class));
                finish();
*/
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}