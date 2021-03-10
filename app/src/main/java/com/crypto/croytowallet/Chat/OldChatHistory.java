package com.crypto.croytowallet.Chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.crypto.croytowallet.Adapter.TickectChatAdapter;
import com.crypto.croytowallet.Model.TicketChatModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;

public class OldChatHistory extends AppCompatActivity {
    TextView first_textUsername,fullName;
    String message,sendername,messageId;
    KProgressHUD progressDialog;
    UserData userData;
    RecyclerView chatRecyclerView;
    TickectChatAdapter tickectChatAdapter;
    ArrayList<TicketChatModel> ticketChatModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_chat_history);
         first_textUsername = findViewById(R.id.userFirsttext);
        fullName = findViewById(R.id.username);
        chatRecyclerView = findViewById(R.id.recycler_view);

        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        ticketChatModels = new ArrayList<TicketChatModel>();
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }
}