package com.crypto.croytowallet.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.Activity.Support;
import com.crypto.croytowallet.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class TicketChat extends AppCompatActivity {
CardView send;
EditText text_message;
TextView textView_Send;
String message;

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("https://api.imx.global");
        } catch (URISyntaxException e) {}
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_chat);
        send= findViewById(R.id.btn_send);
        text_message = findViewById(R.id.text_send);
        textView_Send = findViewById(R.id.textView_send);

        send.setAlpha(0.4f);
        textView_Send.setAlpha(0.4f);
        text_message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String msg = s.toString();
                if (msg.isEmpty()){
                    send.setAlpha(0.4f);
                    textView_Send.setAlpha(0.4f);


                }else{
                    send.setAlpha(0.9f);
                    textView_Send.setAlpha(0.9f);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                message = text_message.getText().toString().trim();

                if(message.isEmpty()){

                }else {
                    sendMessage();
                }

            }
        });


        mSocket.connect();
        mSocket.on("hello", onNewMessage);

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    Log.d("hello",data.toString());
                    Toast.makeText(TicketChat.this, ""+data.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    };




    private void sendMessage() {
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