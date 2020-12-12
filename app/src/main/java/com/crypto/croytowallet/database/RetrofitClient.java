package com.crypto.croytowallet.database;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL="http://13.233.136.56:8080/api/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    public RetrofitClient() {
        retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public  static synchronized RetrofitClient getInstance()
    {
        if (mInstance==null)
        {
            mInstance=new RetrofitClient();

        }
        return mInstance;
    }
    public Api getApi()
    {
        return retrofit.create(Api.class);

    }

}
