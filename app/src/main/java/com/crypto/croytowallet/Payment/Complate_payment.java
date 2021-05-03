package com.crypto.croytowallet.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cooltechworks.views.ScratchTextView;
import com.crypto.croytowallet.LunchActivity.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedRequestResponse;
import com.crypto.croytowallet.TopUp.TopUp_Acknowlegement;

import de.mateware.snacky.Snacky;

public class Complate_payment extends AppCompatActivity implements View.OnClickListener {
    TextView back;
   Button okay;
    TextView showEnteredAmount,amount_in_crypto,amount_in_Currency,trans_hash,trans_status,btncopy,reciverName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complate_payment);
        back =findViewById(R.id.toolbar_title);
        showEnteredAmount = findViewById(R.id.amount);
        amount_in_crypto = findViewById(R.id.amount_in_crypto);
        amount_in_Currency = findViewById(R.id.amount_in_currency);
        trans_hash = findViewById(R.id.trans_hash_id);
        trans_status = findViewById(R.id.status);
        reciverName = findViewById(R.id.recriver_address);
        btncopy  =findViewById(R.id.btn_copy);
        okay = findViewById(R.id.okay_btn);


        showEnteredAmount.setText( SharedRequestResponse.getInstans(getApplicationContext()).getIMT_U_Amount()+" Utility");
        amount_in_crypto.setText( SharedRequestResponse.getInstans(getApplicationContext()).getIMT_U_Amount()+" Utility");
        amount_in_Currency.setText( SharedRequestResponse.getInstans(getApplicationContext()).getEnter_Amount());
        trans_hash.setText( SharedRequestResponse.getInstans(getApplicationContext()).getTransferId());
        reciverName.setText( SharedRequestResponse.getInstans(getApplicationContext()).getRequest_ID());


        back.setOnClickListener(this);
        btncopy.setOnClickListener(this);
        okay.setOnClickListener(this);



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

            case  R.id.btn_copy:
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(trans_hash.getText().toString());
                Snacky.builder()
                        .setActivity(Complate_payment.this)
                        .setText("Copied")
                        .setDuration(Snacky.LENGTH_SHORT)
                        .setActionText(android.R.string.ok)
                        .success()
                        .show();
                break;

            case  R.id.okay_btn:
                startActivity(new Intent(Complate_payment.this, MainActivity.class));
                SharedRequestResponse.getInstans(getApplicationContext()).RemoveData();
                break;

        }
    }
}
  /* if(scratchTextView != null) {
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

          scratchTextView = (ScratchTextView) findViewById(R.id.scratch_view);
             ScratchTextView scratchTextView;

*/