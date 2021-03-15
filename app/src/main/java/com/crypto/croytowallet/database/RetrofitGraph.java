package com.crypto.croytowallet.database;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitGraph {

    private static final String BASE_URL="https://api.coingecko.com/api/v3/";
    private static RetrofitGraph mInstance;
    private Retrofit retrofit;
    private Context context;



    public RetrofitGraph() {
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public  static synchronized RetrofitGraph getInstance()
    {
        if (mInstance==null)
        {
            mInstance=new RetrofitGraph();

        }
        return mInstance;
    }
    public Api getApi()
    {
        return retrofit.create(Api.class);

    }

}
