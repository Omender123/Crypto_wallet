package com.crypto.croytowallet.Rewards;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.crypto.croytowallet.Activity.Add_Currency;
import com.crypto.croytowallet.Adapter.Add_Currency_Adapter;
import com.crypto.croytowallet.Adapter.Product_HistoryAdapter;
import com.crypto.croytowallet.Adapter.Product_adapter;
import com.crypto.croytowallet.R;

public class Rewards extends AppCompatActivity {
    RecyclerView recyclerView,history_recyclerView;
    Product_adapter product_adapter;
    Product_HistoryAdapter  product_historyAdapter;
    String [] price = {"50$","50$","50$","50$","50$","50$"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        recyclerView = findViewById(R.id.product_recycler);
        history_recyclerView= findViewById(R.id.history_recycler);


        getAllprouct();
        getAllprouctHistory();



    }

    private void getAllprouctHistory() {
        product_historyAdapter = new Product_HistoryAdapter(price);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(Rewards.this, LinearLayoutManager.VERTICAL, false);
        history_recyclerView.setLayoutManager(mLayoutManager1);
        history_recyclerView.setItemAnimator(new DefaultItemAnimator());
        history_recyclerView.setAdapter(product_historyAdapter);
    }

    private void getAllprouct() {
        product_adapter = new Product_adapter(price,Rewards.this);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(Rewards.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager1);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(product_adapter);
    }

    public void back(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onSaveInstanceState(new Bundle());
    }

    public void OpenRedeem(View view) {
        startActivity(new Intent(getApplicationContext(),Redeem.class));
    }
    public void Categories_btn(View view) {
        startActivity(new Intent(getApplicationContext(),Product_Categories.class));
    }

}