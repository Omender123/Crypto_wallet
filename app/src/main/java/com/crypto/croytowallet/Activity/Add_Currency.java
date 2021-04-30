package com.crypto.croytowallet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.crypto.croytowallet.Adapter.Add_Currency_Adapter;

import com.crypto.croytowallet.Model.Model_Class_Add_Currency;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.database.RetrofitGraph;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class Add_Currency extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<Model_Class_Add_Currency> item_data;
    EditText search;
    ImageView imageView;
    CharSequence search1 = "";
    KProgressHUD progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__currency);
        search = findViewById(R.id.search_currency);
        imageView =findViewById(R.id.back);
        item_data = new ArrayList<Model_Class_Add_Currency>();
        recyclerView=findViewById(R.id.recyclerView_add_currenecy);

        getAllCoins();

        back();

    }



    public void back(){
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       onSaveInstanceState(new Bundle());
    }

    public void getAllCoins(){
        progressDialog = KProgressHUD.create(Add_Currency.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();

        String coinid="ripple,ethereum,bitcoin,litecoin,tether,link,okb,cdai,binancecoin,usd-coin,wrapped-bitcoin,crypto-com-chain,leo-token,wrapped-filecoin";

        Call<ResponseBody> call = RetrofitGraph.getInstance().getApi().getAllCoin(coinid,"usd");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hidepDialog();
                String  s =null;
                item_data.clear();
                if (response.isSuccessful()){
                    try {
                        s=response.body().string();
                        JSONArray jsonArray = new JSONArray(s);
                        for (int i=0;i<=jsonArray.length();i++){
                            Model_Class_Add_Currency currency_model = new Model_Class_Add_Currency();
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);
                            String id=jsonObject1.getString("id");
                            String symbol =jsonObject1.getString("symbol");
                            String image=jsonObject1.getString("image");
                            String name=jsonObject1.getString("name");

                            currency_model.setCurrency_Title(name);
                            currency_model.setTitle_Des(symbol);
                            currency_model.setCoinId(id);
                            currency_model.setImage(image);

                            item_data.add(currency_model);

                        }

                      //  Toast.makeText(Add_Currency.this, ""+s, Toast.LENGTH_SHORT).show();
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    Add_Currency_Adapter adapter = new Add_Currency_Adapter(item_data,getApplicationContext());
                    RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(Add_Currency.this, LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(mLayoutManager1);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(adapter);

                    search.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            adapter.getFilter().filter(s);
                            search1 = s;
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });



                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hidepDialog();
            }
        });
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
