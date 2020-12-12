package com.crypto.croytowallet.TransactionPin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.R;

public class ShowMnemonic extends AppCompatActivity {
Context context;
TextView textView;
Button copy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_mnemonic);
        //textView=findViewById(R.id.mnemonic);
        copy=findViewById(R.id.copy);

        final TextView txtcopypaste = findViewById(R.id.mnemonic);
        txtcopypaste.setText("dasfad");
        // my textview
        copy.setOnClickListener(new View.OnClickListener() { // set onclick listener to my textview
            @Override
            public void onClick(View view) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(txtcopypaste.getText().toString());
                Toast.makeText(getApplicationContext(), "Copied :)", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void Next(View view) {
    }

    public void copy(View view) {
        ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(textView.getText());
        Toast.makeText(context, cm+"", Toast.LENGTH_SHORT).show();
    }
}