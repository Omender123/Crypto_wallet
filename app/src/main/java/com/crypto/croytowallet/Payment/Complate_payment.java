package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cooltechworks.views.ScratchTextView;
import com.crypto.croytowallet.R;

public class Complate_payment extends AppCompatActivity implements View.OnClickListener {
    TextView back;
    SharedPreferences preferences;
    ScratchTextView scratchTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complate_payment);
        back =findViewById(R.id.toolbar_title);
        scratchTextView = (ScratchTextView) findViewById(R.id.scratch_view);


        preferences=getApplicationContext().getSharedPreferences("walletScan", Context.MODE_PRIVATE);
        String username = preferences.getString("username","");
        Bundle bundle = getIntent().getExtras();

        /*  try {
            String status = bundle.getString("status");
            String amount = bundle.getString("amt");
            success.setText("Payment of "+amount+" To " +username+"\n"+status);
        }catch (Exception e){

        }*/

        if(scratchTextView != null) {
            scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
                @Override
                public void onRevealed(ScratchTextView tv) {

                    //mScratchTitleView.setText(R.string.flat_200_offer);
                }

                @Override
                public void onRevealPercentChangedListener(ScratchTextView stv, float percent) {
                    // on percent reveal.
                }
            });
        }

        back.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       onSaveInstanceState(new Bundle());
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case  R.id.back:
            onBackPressed();
            break;
        }
    }
}