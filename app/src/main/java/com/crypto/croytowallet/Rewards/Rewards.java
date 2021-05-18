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
import com.crypto.croytowallet.Adapter.Product_adapter;
import com.crypto.croytowallet.R;

public class Rewards extends AppCompatActivity {
    RecyclerView recyclerView;
    Product_adapter product_adapter;
    String [] price = {"50$","50$","50$","50$","50$","50$"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

        recyclerView = findViewById(R.id.product_recycler);



        getAllprouct();



    }

    private void getAllprouct() {
        product_adapter = new Product_adapter(price);
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
}