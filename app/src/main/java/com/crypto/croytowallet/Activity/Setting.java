package com.crypto.croytowallet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.login.Login;

import java.util.Set;

import de.mateware.snacky.Snacky;

public class Setting extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;
    CardView Scan_devices,restore_wallet,notification,sound,currency;
    SharedPreferences sharedPreferences;

    TextView currencyType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        imageView =findViewById(R.id.back);
        Scan_devices =findViewById(R.id.Scan_devices);
        restore_wallet = findViewById(R.id.restore_wallet);
        notification = findViewById(R.id.notification);
        sound = findViewById(R.id.sound);
        currency = findViewById(R.id.currency);
        Scan_devices.setOnClickListener(this);
        restore_wallet.setOnClickListener(this);
        notification.setOnClickListener(this);
        sound.setOnClickListener(this);
        currency.setOnClickListener(this);

        currencyType = findViewById(R.id.currency1);

        sharedPreferences =getSharedPreferences("currency",0);
        String currency2 =sharedPreferences.getString("currency1","usd");

        currencyType.setText(currency2);
       // Toast.makeText(this, ""+currency, Toast.LENGTH_SHORT).show();

        back();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:


                Intent intent = new Intent(Setting.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return true;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.Scan_devices:
                startActivity(new Intent(getApplicationContext(),Sync_device.class));
                break;
            case R.id.restore_wallet:
                startActivity(new Intent(getApplicationContext(),Scretephases.class));
                break;
            case R.id.notification:
                Snacky.builder()
                        .setActivity(Setting.this)
                        .setText("Coming Up Features")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setDuration(Snacky.LENGTH_SHORT)
                        .success()
                        .show();

                break;
            case R.id.sound:
                Snacky.builder()
                        .setActivity(Setting.this)
                        .setText("Coming Up Features")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setDuration(Snacky.LENGTH_SHORT)
                        .success()
                        .show();
                break;
            case R.id.currency:
                startActivity(new Intent(getApplicationContext(),SelectCurrency.class));
              /*  Snacky.builder()
                        .setActivity(Setting.this)
                        .setText("Coming Up Features")
                        .setTextColor(getResources().getColor(R.color.white))
                        .setDuration(Snacky.LENGTH_SHORT)
                        .success()
                        .show();*/
                break;

        }
    }
}