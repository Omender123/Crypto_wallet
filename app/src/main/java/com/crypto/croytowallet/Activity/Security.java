package com.crypto.croytowallet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;

import de.mateware.snacky.Snacky;

public class Security extends AppCompatActivity implements View.OnClickListener {
ActionBar actionBar;
Toolbar toolbar;
ImageView imageView;
CardView Two_FA1;
CheckBox passcode;

CardView backUp;

    SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        imageView =findViewById(R.id.back);
        Two_FA1 =findViewById(R.id.tofa);
        backUp = findViewById(R.id.backUp);

        passcode=findViewById(R.id.checkbox);


        sharedPreferences = getSharedPreferences("myKey1",0);

        Boolean booleanValue = sharedPreferences.getBoolean("passcode",false);
        if (booleanValue){
            passcode.setChecked(true);
        }

        passcode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (isChecked){
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.putBoolean("passcode",true);
                   editor.commit();

                   passcode.setChecked(true);
               }else{
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.putBoolean("passcode",false);
                   editor.commit();

                   passcode.setChecked(false);
               }
            }
        });

        Two_FA1.setOnClickListener(this);
        backUp.setOnClickListener(this);

         back();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(Security.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                Intent intent = new Intent(Security.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void actionBarSetup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);

         //   actionBar.setTitle("Price Action Strategy ");

        }
    }
    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Security.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();

        switch (id){

            case R.id.tofa:
                startActivity(new Intent(Security.this, Two_FA.class));
                finish();
                break;
            case R.id.backUp:
                Snacky.builder()
                        .setActivity(Security.this)
                        .setText("Coming Up Features")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setDuration(Snacky.LENGTH_SHORT)
                        .success()
                        .show();
                break;
        }
    }
}