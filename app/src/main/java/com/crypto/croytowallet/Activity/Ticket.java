package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.crypto.croytowallet.Adapter.Ticket_Adapter;
import com.crypto.croytowallet.R;


public class Ticket extends AppCompatActivity {
RecyclerView recyclerView;
TextView add_ticket_btn;
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);
        recyclerView = findViewById(R.id.Recycler_Add_Ticket);
        imageView =findViewById(R.id.back);
        add_ticket_btn=findViewById(R.id.add_more_ticket);
        add_ticket_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Ticket.this,Add_Ticket.class);
                startActivity(i);
            }
        });
               back();
    }
    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Ticket.this, Support.class);

                startActivity(intent);
            }
        });
           String[] data={"madsad","adsd","dsafaf","vcdsf","madsad","adsd","dsafaf","vcdsf"};
        Ticket_Adapter adapter = new Ticket_Adapter(data);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }
}