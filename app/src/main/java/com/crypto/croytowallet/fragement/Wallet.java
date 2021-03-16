
package com.crypto.croytowallet.fragement;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Activity.Add_Currency;
import com.crypto.croytowallet.Activity.Graph_layout;
import com.crypto.croytowallet.Adapter.Crypto_currencyInfo;
import com.crypto.croytowallet.Adapter.OverViewAdapter;
import com.crypto.croytowallet.Interface.CryptoClickListner;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.Model.OverViewModel;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.Updated_data;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Wallet extends Fragment implements  CryptoClickListner{
    ArrayList<CrptoInfoModel> crptoInfoModels;
    ArrayList<OverViewModel> overViewModels;
    RecyclerView WalletRecyclerView,overviewRecycler;
    RequestQueue requestQueue;
    Crypto_currencyInfo crypto_currencyInfo;
    OverViewAdapter overViewAdapter;
    SharedPreferences sharedPreferences;
    KProgressHUD progressDialog;
    String currency2;
    TextView add_currency;
    public Wallet() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view= inflater.inflate(R.layout.fragment_mywallet, container, false);

        WalletRecyclerView =view.findViewById(R.id.walletRecyclerView);
        overviewRecycler = view.findViewById(R.id.overviewRecycler);
        add_currency = view.findViewById(R.id.Add_more_Currency);
        crptoInfoModels=new ArrayList<CrptoInfoModel>();
        overViewModels=new ArrayList<OverViewModel>();

        sharedPreferences =getActivity().getSharedPreferences("currency",0);
        currency2 =sharedPreferences.getString("currency1","usd");

        add_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Add_Currency.class));
                getActivity().finish();
            }
        });
        CryptoInfoRecyclerView();
        overViewData();
    return view;
    }
    public void CryptoInfoRecyclerView(){
        progressDialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();
        String url="https://api.coingecko.com/api/v3/coins/markets?vs_currency="+currency2+"&ids=bitcoin%2Cethereum%2Ctether%2Cripple%2Clitecoin%2Cusd-coin&order=market_cap_desc&sparkline=false&price_change_percentage=24h";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hidepDialog();
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<=jsonArray.length();i++){
                        CrptoInfoModel  crptoInfoModel1= new CrptoInfoModel();
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id=jsonObject1.getString("id");
                        String symbol =jsonObject1.getString("symbol");
                        String image=jsonObject1.getString("image");
                        String name=jsonObject1.getString("name");
                        String rate=jsonObject1.getString("price_change_percentage_24h");
                        String  price=jsonObject1.getString("current_price");

                        crptoInfoModel1.setId(id);
                        crptoInfoModel1.setImage(image);
                        crptoInfoModel1.setName(name);
                        crptoInfoModel1.setCurrencyRate(rate);
                        crptoInfoModel1.setCurrentPrice(price);
                        crptoInfoModel1.setSymbol(symbol);
                        crptoInfoModels.add(crptoInfoModel1);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                crypto_currencyInfo = new Crypto_currencyInfo(crptoInfoModels,getContext(), Wallet.this::onCryptoItemClickListener);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
                WalletRecyclerView.setLayoutManager(mLayoutManager);
                WalletRecyclerView.setItemAnimator(new DefaultItemAnimator());
                WalletRecyclerView.setAdapter(crypto_currencyInfo);
                //  Toast.makeText(getContext(), ""+response.toString(), Toast.LENGTH_SHORT).show();

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                hidepDialog();
                Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    @Override
    public void onCryptoItemClickListener(int position) {
        Intent intent = new Intent(getContext(), Graph_layout.class);
      //  intent.putExtra("position",position);
        startActivity(intent);
        String CoinID=crptoInfoModels.get(position).getId();
        String result=crptoInfoModels.get(position).getSymbol();
       String price=crptoInfoModels.get(position).getCurrentPrice();
        String image=crptoInfoModels.get(position).getImage();
        String coinName=crptoInfoModels.get(position).getName();
        String change=crptoInfoModels.get(position).getCurrencyRate();

        Updated_data.getInstans(getContext()).userLogin(position,coinName,result,image,change,price,CoinID);

       /* SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("symbol1",result);
        editor.putInt("position",position);
        editor.putInt("price",price);
        editor.putString("image",image);
        editor.putString("coinName",coinName);
        editor.putString("change",change);

        editor.commit();*/

    }
    private void showpDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hidepDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }
    public void overViewData(){
        String url="https://api.coingecko.com/api/v3/coins/markets?vs_currency="+currency2+"&ids=bitcoin%2Cethereum&order=market_cap_desc&sparkline=false&price_change_percentage=24h";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //   hidepDialog();
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for (int i=0;i<=jsonArray.length();i++){
                        OverViewModel  overViewModel1= new OverViewModel();
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        String id=jsonObject1.getString("id");
                        String symbol =jsonObject1.getString("symbol");
                        String image=jsonObject1.getString("image");
                        String name=jsonObject1.getString("name");
                        String rate=jsonObject1.getString("price_change_percentage_24h");
                        String price=jsonObject1.getString("current_price");
                        String high_price=jsonObject1.getString("high_24h");
                        String low_price=jsonObject1.getString("low_24h");

                        // Log.d("data",id+symbol+image+name+rate+price);

                        overViewModel1.setId(id);
                        overViewModel1.setImage(image);
                        overViewModel1.setName(name);
                        overViewModel1.setCurrencyRate(rate);
                        overViewModel1.setCurrentPrice(price);
                        overViewModel1.setHigh_price(high_price);
                        overViewModel1.setLow_price(low_price);
                        overViewModel1.setSymbol(symbol);
                        overViewModels.add(overViewModel1);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                overViewAdapter = new OverViewAdapter(overViewModels,getContext());
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
                overviewRecycler.setLayoutManager(mLayoutManager);
                overviewRecycler.setItemAnimator(new DefaultItemAnimator());
                overviewRecycler.setAdapter(overViewAdapter);
                //  Toast.makeText(getContext(), ""+response.toString(), Toast.LENGTH_SHORT).show();

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // hidepDialog();
                // Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

}
