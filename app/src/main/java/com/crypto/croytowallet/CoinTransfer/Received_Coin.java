package com.crypto.croytowallet.CoinTransfer;

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

import com.crypto.croytowallet.Activity.Graph_layout;
import com.crypto.croytowallet.Activity.WalletReceive;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Received_Coin extends AppCompatActivity {
    TextView barcodeAddress,toolbar_title;
    ImageView qrImage,imageView;
    CardView barCodeshare;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received__coin);
        imageView =findViewById(R.id.back);
        barcodeAddress=findViewById(R.id.barCodeAddress);
        qrImage = findViewById(R.id.qrPlaceHolder);
        barCodeshare=findViewById(R.id.barCodeshare);
        toolbar_title=findViewById(R.id.toolbar_title);
        Bundle bundle = getIntent().getExtras();
        position=bundle.getInt("position");

        back();

        UserData userData= SharedPrefManager.getInstance(this).getUser();

        switch (position){
            case 0:

                String id=userData.getBTC();
                barcodeAddress.setText(id);
                toolbar_title.setText("Receive BTC");
                QRGEncoder qrgEncoder = new QRGEncoder(id,null, QRGContents.Type.TEXT,500);
                try {
                    Bitmap qrBits = qrgEncoder.encodeAsBitmap();
                    qrImage.setImageBitmap(qrBits);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                barcodeAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(id);
                        Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();

                    }
                });

                barCodeshare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, id);
                        startActivity(Intent.createChooser(i, "Share With"));
                    }
                });
                break;

            case 1:
                String id1=userData.getETH();
                barcodeAddress.setText(id1);
                toolbar_title.setText("Receive ETH");
                QRGEncoder qrgEncoder1 = new QRGEncoder(id1,null, QRGContents.Type.TEXT,500);
                try {
                    Bitmap qrBits = qrgEncoder1.encodeAsBitmap();
                    qrImage.setImageBitmap(qrBits);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                barcodeAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(id1);
                        Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();

                    }
                });

                barCodeshare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, id1);
                        startActivity(Intent.createChooser(i, "Share With"));
                    }
                });
                break;

            case 2:
              //  barcodeAddress.setText("id2");
                toolbar_title.setText("Receive Tether");
                QRGEncoder qrgEncoder2 = new QRGEncoder("Bar code Address",null, QRGContents.Type.TEXT,500);
                try {
                    Bitmap qrBits = qrgEncoder2.encodeAsBitmap();
                    qrImage.setImageBitmap(qrBits);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                barcodeAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText("Bar code Address");
                        Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();

                    }
                });

                barCodeshare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, "bar code Address");
                        startActivity(Intent.createChooser(i, "Share With"));
                    }
                });
                break;

            case 3:
                String id3=userData.getXRP();
                barcodeAddress.setText(id3);
                toolbar_title.setText("Receive XRP");
                QRGEncoder qrgEncoder3 = new QRGEncoder(id3,null, QRGContents.Type.TEXT,500);
                try {
                    Bitmap qrBits = qrgEncoder3.encodeAsBitmap();
                    qrImage.setImageBitmap(qrBits);
                } catch (WriterException e) {
                    e.printStackTrace();
                }

                barcodeAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(id3);
                        Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();

                    }
                });

                barCodeshare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, id3);
                        startActivity(Intent.createChooser(i, "Share With"));
                    }
                });
                break;
                case 4:
                String id4=userData.getLITE();
                barcodeAddress.setText(id4);
                toolbar_title.setText("Receive LITE");
                QRGEncoder qrgEncoder5 = new QRGEncoder(id4,null, QRGContents.Type.TEXT,500);
                try {
                    Bitmap qrBits = qrgEncoder5.encodeAsBitmap();
                    qrImage.setImageBitmap(qrBits);
                } catch (WriterException e) {
                    e.printStackTrace();
                }


                barcodeAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager cm = (ClipboardManager)getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        cm.setText(id4);
                        Toast.makeText(getApplicationContext(), "Copied ", Toast.LENGTH_SHORT).show();

                    }
                });


                barCodeshare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, id4);
                        startActivity(Intent.createChooser(i, "Share With"));
                    }
                });
                break;
        }






    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent intent = new Intent(getApplicationContext(), Graph_layout.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
       */ onSaveInstanceState(new Bundle());


    }

    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

    }
}