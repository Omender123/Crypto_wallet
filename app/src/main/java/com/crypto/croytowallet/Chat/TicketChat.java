package com.crypto.croytowallet.Chat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.crypto.croytowallet.Activity.Support;
import com.crypto.croytowallet.R;

public class TicketChat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_chat);
    }

    public void back(View view) {
        startActivity(new Intent(getApplicationContext(), Support.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }
}