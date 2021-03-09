package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class MyReferral_code extends AppCompatActivity {
    TextView barcodeAddress;
    ImageView qrImage,imageView;
    CardView barCodeshare;
    String myReferral_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_referral_code);

        barcodeAddress=findViewById(R.id.barCodeAddress);
        qrImage = findViewById(R.id.qrPlaceHolder);
        barCodeshare=findViewById(R.id.barCodeshare);
        UserData userData= SharedPrefManager.getInstance(this).getUser();

        myReferral_code = userData.getReferral_code();

        barcodeAddress.setText(myReferral_code);

        barcodeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(myReferral_code);
                Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();

            }
        });

        QRGEncoder qrgEncoder = new QRGEncoder(myReferral_code,null, QRGContents.Type.TEXT,500);
        try {
            Bitmap qrBits = qrgEncoder.encodeAsBitmap();
            qrImage.setImageBitmap(qrBits);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        barCodeshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, "Use this Referral Code \n "+myReferral_code);
                startActivity(Intent.createChooser(i, "Share With"));
            }
        });


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