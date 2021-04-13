package com.crypto.croytowallet.ImtSmart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.crypto.croytowallet.Adapter.TickectChatAdapter;
import com.crypto.croytowallet.AppUtils;
import com.crypto.croytowallet.Chat.TicketChat;
import com.crypto.croytowallet.MainActivity;
import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.Model.SwapRespoinseModel;
import com.crypto.croytowallet.Model.TicketChatModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.SwapResponsePrefernce;
import com.crypto.croytowallet.SharedPrefernce.SwapSharedPrefernce;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.database.RetrofitClient;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;

import de.mateware.snacky.Snacky;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SwapEnterPin extends AppCompatActivity {
    PinView pinView;
    CardView pay_money;
    KProgressHUD progressDialog;
    SwapModel swapModel;
    UserData userData;
    String sendData,receivedData,coinAmount,Token,ethAddress;
    String transIDs,statuss;
    int value;

    private Socket mSocket;
    {
        try {
           mSocket = IO.socket("https://api.imx.global");

           // mSocket = IO.socket("http://13.233.136.56:8080");

        } catch (URISyntaxException e) {}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swap_enter_pin);
        pinView = findViewById(R.id.enter_pin);
        pay_money = findViewById(R.id.pay_money);
        userData= SharedPrefManager.getInstance(getApplicationContext()).getUser();
        String trans =userData.getTransaction_Pin();

        swapModel = SwapSharedPrefernce.getInstance(getApplicationContext()).getSwapData();
        userData = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        Token = userData.getToken();
        ethAddress = userData.getETH();

        sendData = swapModel.getSendData();
        receivedData = swapModel.getReceivedData();
       coinAmount = swapModel.getCoinAmount();
        value = swapModel.getValue();





        pay_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enterPin=pinView.getText().toString();

                if (enterPin.isEmpty()){
                    Snacky.builder()
                            .setActivity(SwapEnterPin.this)
                            .setText("Please enter transaction pin")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();

                }else if (enterPin.equals(trans)){
                    pinView.setLineColor(getResources().getColor(R.color.green));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            pinView.setLineColor(getResources().getColor(R.color.light_gray));
                        }
                    }, 200);
                  done(enterPin);
                }else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            pinView.setLineColor(getResources().getColor(R.color.light_gray));
                        }
                    }, 200);
                    pinView.setLineColor(getResources().getColor(R.color.red));
                }


            }
        });

        mSocket.connect();
       // mSocket.on("hello", onNewMessage);
      mSocket.on("pendingReport",PendingReport);
        mSocket.on("cryptoError",CryptoError);
       // mSocket.on("finalReport",FinalReport);
    }

    private Emitter.Listener PendingReport = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        transIDs = data.getString("transactionHash");
                         statuss = data.getString("status");



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    };

    private Emitter.Listener CryptoError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];


                       AppUtils.showMessageOKCancel(data.toString(), SwapEnterPin.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);

                    }
                });

            }
        });


        }
    };
    private Emitter.Listener FinalReport = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    /*try {
                        String transID = data.getString("transactionHash");
                        String status = data.getString("status");

                        SwapRespoinseModel swapRespoinseModel = new SwapRespoinseModel(transID,status);
                        SwapResponsePrefernce.getInstance(getApplicationContext()).SetData(swapRespoinseModel);

                         Intent intent = new Intent(SwapEnterPin.this,SwapAcknowledgement.class);
                            startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }*/
                    Log.d("hello",data.toString());
                    Toast.makeText(SwapEnterPin.this, ""+data.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    };
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];

                    Log.d("hello111",data.toString());


                    Toast.makeText(SwapEnterPin.this, ""+data.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    };


    private void done(String pin) {

        progressDialog = KProgressHUD.create(SwapEnterPin.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();




        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().IMT_SWAP(Token, sendData, receivedData, value, coinAmount, pin, ethAddress);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                hidepDialog();
                if (response.code() == 200) {

                    try {
                        s = response.body().string();

                        JSONObject object = new JSONObject(s);
                        String responses = object.getString("response");

                        JSONObject  object1 = new JSONObject(responses);

                        String transId = object1.getString("transactionHash");
                        String status = object1.getString("status");

                        SwapRespoinseModel swapRespoinseModel = new SwapRespoinseModel(transId,status);
                        SwapResponsePrefernce.getInstance(getApplicationContext()).SetData(swapRespoinseModel);

                        startActivity(new Intent(getApplicationContext(),SwapAcknowledgement.class));

                       // Toast.makeText(SwapEnterPin.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 400) {
                    try {
                        s = response.errorBody().string();
                        JSONObject jsonObject1 = new JSONObject(s);
                        String error = jsonObject1.getString("error");


                        Snacky.builder()
                                .setActivity(SwapEnterPin.this)
                                .setText(error)
                                .setDuration(Snacky.LENGTH_SHORT)
                                .setActionText(android.R.string.ok)
                                .error()
                                .show();


                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 401) {
                    Snacky.builder()
                            .setActivity(SwapEnterPin.this)
                            .setText("unAuthorization Request")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                } else if (response.code() == 504) {
                    Snacky.builder()
                            .setActivity(SwapEnterPin.this)
                            .setText("Gate Way Time Down")
                            .setDuration(Snacky.LENGTH_SHORT)
                            .setActionText(android.R.string.ok)
                            .error()
                            .show();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
              //  mSocket.on("pendingReport",PendingReport);

                SwapRespoinseModel swapRespoinseModel = new SwapRespoinseModel(transIDs,statuss);
                SwapResponsePrefernce.getInstance(getApplicationContext()).SetData(swapRespoinseModel);

                Intent intent = new Intent(SwapEnterPin.this,SwapAcknowledgement.class);
                startActivity(intent);

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

    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}