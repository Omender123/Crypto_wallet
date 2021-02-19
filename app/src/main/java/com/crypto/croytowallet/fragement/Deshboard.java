package com.crypto.croytowallet.fragement;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DecimalFormat;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.crypto.croytowallet.Activity.Add_Currency;
import com.crypto.croytowallet.Activity.Graph_layout;
import com.crypto.croytowallet.Activity.WalletBalance;
import com.crypto.croytowallet.Activity.WalletReceive;
import com.crypto.croytowallet.Activity.WalletScan;
import com.crypto.croytowallet.Adapter.Crypto_currencyInfo;
import com.crypto.croytowallet.Adapter.OverViewAdapter;
import com.crypto.croytowallet.ImtSmart.ImtSmartGraphLayout;
import com.crypto.croytowallet.Interface.CryptoClickListner;
import com.crypto.croytowallet.Model.CrptoInfoModel;
import com.crypto.croytowallet.Model.OverViewModel;
import com.crypto.croytowallet.Payment.Complate_payment;
import com.crypto.croytowallet.Payment.Enter_transaction_pin;
import com.crypto.croytowallet.Payment.Top_up_Money;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.SharedPrefernce.SharedPrefManager;
import com.crypto.croytowallet.SharedPrefernce.Updated_data;
import com.crypto.croytowallet.SharedPrefernce.UserData;
import com.crypto.croytowallet.TransactionHistory.Full_Transaction_History;
import com.crypto.croytowallet.TransactionHistory.Transaction_history;
import com.crypto.croytowallet.VolleyDatabase.URLs;
import com.crypto.croytowallet.login.Login;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class
Deshboard extends Fragment implements View.OnClickListener, CryptoClickListner {
    ArrayList<CrptoInfoModel> crptoInfoModels;
    ArrayList<OverViewModel> overViewModels;
    RecyclerView cryptoInfoRecyclerView,overviewRecycler;
    RequestQueue requestQueue;
    Crypto_currencyInfo crypto_currencyInfo;
    OverViewAdapter overViewAdapter;
    LinearLayout lytscan,lytPay,lytWalletBalance,lytaddMoney;
    SharedPreferences sharedPreferences,sharedPreferences1;
    TextView textView,textView1;
    CardView imtsmart;
    TextView add_currency,increaseRate,null1,imtPrice;
    String imtPrices,increaseRate1;
    KProgressHUD progressDialog;
    String currency2,CurrencySymbols;

    public Deshboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view= inflater.inflate(R.layout.fragment_deshboard, container, false);
        cryptoInfoRecyclerView = view.findViewById(R.id.deshboardRecyclerView);

        lytscan=view.findViewById(R.id.lytScan);
        lytPay=view.findViewById(R.id.lytPay);
        lytWalletBalance=view.findViewById(R.id.lytwallet);
        lytaddMoney=view.findViewById(R.id.lytaddMoney);
        imtsmart = view.findViewById(R.id.ImtSmart);
        add_currency = view.findViewById(R.id.Add_more_Currency);
        overviewRecycler = view.findViewById(R.id.overviewRecycler);
        lytscan.setOnClickListener(this);
        lytPay.setOnClickListener(this);
        lytWalletBalance.setOnClickListener(this);
        lytaddMoney.setOnClickListener(this);

        crptoInfoModels=new ArrayList<CrptoInfoModel>();
        overViewModels=new ArrayList<OverViewModel>();

        textView  =view.findViewById(R.id.balance);
        textView1  =view.findViewById(R.id.balance1);
        increaseRate  =view.findViewById(R.id.increaseRate);
        null1  =view.findViewById(R.id.null1);
        imtPrice  =view.findViewById(R.id.coinrate);

      //  sharedPreferences=getActivity().getSharedPreferences("symbols", Context.MODE_PRIVATE);
        sharedPreferences1=getActivity().getSharedPreferences("imtInfo", Context.MODE_PRIVATE);

       sharedPreferences =getActivity().getSharedPreferences("currency",0);
         currency2 =sharedPreferences.getString("currency1","usd");
         CurrencySymbols =sharedPreferences.getString("Currency_Symbols","$");


        CryptoInfoRecyclerView();
     checkBalance();

        imtsmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ImtSmartGraphLayout.class);

               /* intent.putExtra("price",imtPrices);
                intent.putExtra("chanage",increaseRate1);
*/
                startActivity(intent);

                SharedPreferences.Editor editor=sharedPreferences1.edit();
                editor.putString("price",imtPrices);
                editor.putString("chanage",increaseRate1);
                editor.commit();
            }
        });

        add_currency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Add_Currency.class));
                getActivity().finish();
            }
        });
        getImtDetails();

        overViewData();

        return view;
    }

    public void CryptoInfoRecyclerView(){
       /* progressDialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait.....")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        showpDialog();
*/
        String url="https://api.coingecko.com/api/v3/coins/markets?vs_currency="+currency2+"&ids=bitcoin%2Cethereum%2Ctether%2Cripple%2Clitecoin%2Cusd-coin&order=market_cap_desc&sparkline=false&price_change_percentage=24h";
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
             //   hidepDialog();
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
                       String price=jsonObject1.getString("current_price");
                        String high_price=jsonObject1.getString("high_24h");
                        String low_price=jsonObject1.getString("low_24h");



                        crptoInfoModel1.setId(id);
                        crptoInfoModel1.setImage(image);
                        crptoInfoModel1.setName(name);
                        crptoInfoModel1.setCurrencyRate(rate);
                        crptoInfoModel1.setCurrentPrice(price);
                        crptoInfoModel1.setHigh_price(high_price);
                        crptoInfoModel1.setLow_price(low_price);
                        crptoInfoModel1.setSymbol(symbol);
                        crptoInfoModels.add(crptoInfoModel1);


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                crypto_currencyInfo = new Crypto_currencyInfo(crptoInfoModels,getContext(),Deshboard.this::onCryptoItemClickListener );
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
                cryptoInfoRecyclerView.setLayoutManager(mLayoutManager);
                cryptoInfoRecyclerView.setItemAnimator(new DefaultItemAnimator());
                cryptoInfoRecyclerView.setAdapter(crypto_currencyInfo);
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

 public void checkBalance(){
     UserData user = SharedPrefManager.getInstance(getContext()).getUser();
     String id=user.getId();
     String url1= URLs.URL_AIRDROP_BALANCE+""+id;
    // String url="http://13.233.136.56:8080/api/user/totalAirDrop/"+id;

//     balance=getView().findViewById(R.id.balance);



     StringRequest stringRequest =new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
         @RequiresApi(api = Build.VERSION_CODES.N)
         @Override
         public void onResponse(String response) {
          //   Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();


             try {
                 JSONObject object=new JSONObject(response);
                String   checkBalance=object.getString("airDrop");



               // textView.setText(checkBalance+".00");
                 double balance2 = Double.parseDouble(checkBalance);

              //   String s = "";
                 double balance1 = Double.parseDouble("0.09");

                Double balance = balance2*balance1;
                 DecimalFormat df = new DecimalFormat();
                 df.setMaximumFractionDigits(2);
                 textView.setText(""+df.format(balance2));
                 textView1.setText(CurrencySymbols+df.format(balance));
              //   Toast.makeText(getContext(), ""+checkBalance, Toast.LENGTH_SHORT).show();
             } catch (JSONException e) {
                 e.printStackTrace();
             }
         }
     }, new Response.ErrorListener() {
         @Override
         public void onErrorResponse(VolleyError error) {


         }
     }) {
         @Override
         protected Map<String, String> getParams() throws AuthFailureError {
             Map<String, String> params = new HashMap<>();
            /* params.put("_id", id);*/

                return params;
         }

         @Override
         public Map<String, String> getHeaders() throws AuthFailureError {
             Map<String, String> headers = new HashMap<String, String>();

             // headers.put("Authorization", "Bearer "+Token);

             return headers;
         }
     };

     requestQueue = Volley.newRequestQueue(getContext());
     requestQueue.add(stringRequest);
 }

    @Override
    public void onResume() {
        super.onResume();
        deepChangeTextColor(1);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

   /* switch (id){
        case R.id.lytScan:

            break;
    }*/

      if (id == R.id.lytScan) {
            deepChangeTextColor(1);
            startActivity(new Intent(getContext(), WalletScan.class));
            getActivity().finish();
        } else if (id == R.id.lytPay) {
            deepChangeTextColor(2);
            startActivity(new Intent(getContext(), WalletReceive.class));
            getActivity().finish();

        } else if (id == R.id.lytwallet) {
            deepChangeTextColor(3);
            startActivity(new Intent(getContext(), WalletBalance.class));
            getActivity().finish();

        }else if (id == R.id.lytaddMoney) {
          deepChangeTextColor(4);
         startActivity(new Intent(getContext(), Top_up_Money.class));
          getActivity().finish();

      }

    }

    public void deepChangeTextColor(int changeId) {
        for (int i = 1; i <= 4; i++) {
            int img = getResources().getIdentifier("img" + i, "id", getActivity().getPackageName());
            int txt = getResources().getIdentifier("txt" + i, "id", getActivity().getPackageName());

            TextView textView = getView().findViewById(txt);
            ImageView imageView = getView().findViewById(img);

            if (changeId == i) {
                imageView.setColorFilter(getResources().getColor(R.color.purple_500));
                textView.setTextColor(getResources().getColor(R.color.purple_500));
                //  textView.setVisibility(View.VISIBLE);
            } else {
                imageView.setColorFilter(getResources().getColor(R.color.toolbar_text_color));
                textView.setTextColor(getResources().getColor(R.color.toolbar_text_color));
                //  textView.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onCryptoItemClickListener(int position) {

        String result=crptoInfoModels.get(position).getSymbol();
        String  price=crptoInfoModels.get(position).getCurrentPrice();
        String image=crptoInfoModels.get(position).getImage();
        String coinName=crptoInfoModels.get(position).getName();
        String change=crptoInfoModels.get(position).getCurrencyRate();
        Updated_data.getInstans(getContext()).userLogin(position,coinName,result,image,change,price);

        Intent intent = new Intent(getContext(), Graph_layout.class);
        startActivity(intent);




    }


    public  void getImtDetails(){

        UserData userData = SharedPrefManager.getInstance(getContext()).getUser();

        String Token =userData.getToken();

      StringRequest stringRequest=new StringRequest(Request.Method.GET, URLs.URL_GET_COIN_IMT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i=0; i<=jsonArray.length();i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        imtPrices =object.getString("price");
                        increaseRate1=object.getString("percent_change_24h");

                        System.out.println("value"+increaseRate1);

                      imtPrice.setText("$"+imtPrices);
                      increaseRate.setText(increaseRate1);

                     try {
                         increaseRate.setTextColor(increaseRate1.contains("-")?
                                 getContext().getResources().getColor(R.color.red): getContext().getResources().getColor(R.color.green)  );

                         null1.setTextColor(increaseRate1.contains("-")?
                                 getContext().getResources().getColor(R.color.red): getContext().getResources().getColor(R.color.green)  );
                         if(increaseRate1.contains("-")){
                             increaseRate.setText(increaseRate1);
                         }else{
                             increaseRate.setText("+"+increaseRate1);
                         }
                     }catch (Exception e){

                     }
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
              //  Toast.makeText(getContext(), ""+response, Toast.LENGTH_SHORT).show();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
              //  hidepDialog();
               // Toast.makeText(getContext(), ""+error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
          @Override
          public Map<String, String> getHeaders() throws AuthFailureError {
              Map<String, String> headers = new HashMap<String, String>();

               headers.put("Authorization", Token);

              return headers;
          }
      };

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

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

