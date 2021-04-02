package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.crypto.croytowallet.Model.SwapModel;
import com.crypto.croytowallet.login.Login;

public class SwapSharedPrefernce {
    private static SwapSharedPrefernce mInstance;
    private static Context mCtx;
    private static final String SHARED_PREF_NAME = "swap";
    private static final String KEY_SEND = "keysend";
    private static final String KEY_RECEIVED= "keyreceived";
    private static final String KEY_COINPRICE = "keycoinPrice";
    private static final String KEY_CURRENCY_TYPE = "keycurrency_type";
    private static final String KEY_CURRENCY_SYMBOLS = "keycurrency_Symbol";
    private static final String KEY_COIN_AMOUNT = "keycoinAmount";
    private static final String KEY_ENTER_AMOUNT = "keyenterAmount";
    private static final String KEY_VALUE = "keyValue";



    private SwapSharedPrefernce(Context context) {
        mCtx = context;
    }

    public static synchronized SwapSharedPrefernce getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SwapSharedPrefernce(context);
        }
        return mInstance;
    }

    public void SetData(SwapModel user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SEND, user.getSendData());
        editor.putString(KEY_RECEIVED, user.getReceivedData());
        editor.putString(KEY_COINPRICE, user.getCoinPrice());
        editor.putString(KEY_CURRENCY_TYPE, user.getCurrencyType());
        editor.putString(KEY_CURRENCY_SYMBOLS, user.getCurrencySymbol());
        editor.putString(KEY_COIN_AMOUNT, user.getCoinAmount());
        editor.putString(KEY_ENTER_AMOUNT, user.getEnterAmount());
        editor.putInt(KEY_VALUE, user.getValue());


        editor.apply();
    }


    public SwapModel getSwapData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new SwapModel(
                sharedPreferences.getString(KEY_SEND, null),
                sharedPreferences.getString(KEY_RECEIVED, null),
                sharedPreferences.getString(KEY_COINPRICE, null),
                sharedPreferences.getString(KEY_CURRENCY_TYPE, null),
                sharedPreferences.getString(KEY_CURRENCY_SYMBOLS, null),
                sharedPreferences.getString(KEY_COIN_AMOUNT, null),
                sharedPreferences.getString(KEY_ENTER_AMOUNT, null),
                sharedPreferences.getInt(KEY_VALUE, 1)
        );
    }
    public void ClearSwapData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }

}
