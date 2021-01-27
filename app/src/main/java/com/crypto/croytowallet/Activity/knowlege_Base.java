package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.crypto.croytowallet.About_Pager;
import com.crypto.croytowallet.Account_Pager;
import com.crypto.croytowallet.Adapter.Pager_Adapter;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Payment_Pager;
import com.crypto.croytowallet.R;
import com.google.android.material.tabs.TabLayout;

import static com.google.android.material.tabs.TabLayout.*;

public class knowlege_Base extends AppCompatActivity {
TabLayout tabLayout;
ViewPager viewPager;
Pager_Adapter pager_adapter;
TextView show_deatils,detail;
ImageView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowlege__base);

        back_btn =findViewById(R.id.back);
        back();

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.pager);
        pager_adapter = new Pager_Adapter(getSupportFragmentManager(),1);
        pager_adapter.addFragment(new Account_Pager(),"Account");
        pager_adapter.addFragment(new Payment_Pager(),"Payment");
        pager_adapter.addFragment(new About_Pager(),"About");
        tabLayout.setTabGravity(GRAVITY_FILL);

        viewPager.setAdapter(pager_adapter);
       tabLayout.setupWithViewPager( viewPager);
       viewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(Tab tab) {

            }

            @Override
            public void onTabReselected(Tab tab) {

            }
        });
    }

    public void back(){
        back_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(knowlege_Base.this,Support.class);
                startActivity(intent);
            }
        });

    }
}