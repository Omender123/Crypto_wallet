package com.crypto.croytowallet.SharedPrefernce;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.crypto.croytowallet.login.Login;

public class PearToPearSharedPrefManager {
    private static final String SHARED_PREF_NAME = "Prebhat";
    private static final String KEY_ID = "keyid";
    private static final String KEY_USER_PUBLIC_KEY = "keypubilc";
    private static final String KEY_STATUS = "keystatus";
    private static final String KEY_AMTOFCRYPTO = "keyamount";
    private static final String KEY_WITHDRAWLFEES= "keywithdraw";
    private static final String KEY_TRANSACTION = "keytransaction";
    private static final String KEY_CRYTOCURRENCY= "keycrytoCurreny";
    private static final String KEY_USER_ID = "keyuserId";
    private static final String KEY_CREATE_AT = "keycreateAt";
    private static final String KEY_UPDATE = "keyUpdate";
    private static PearToPearSharedPrefManager mInstance;
    private static Context mCtx;

    private PearToPearSharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized PearToPearSharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PearToPearSharedPrefManager(context);
        }
        return mInstance;
    }

    //method to let the user login
    //this method will store the user data in shared preferences
    public void pearToPearData(PearToPearModel user) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_ID, user.getId());
        editor.putString(KEY_USER_PUBLIC_KEY, user.getUserPublicKey());
        editor.putString(KEY_STATUS, user.getStatus());
        editor.putString(KEY_AMTOFCRYPTO, user.getAmtOfCrypto());
        editor.putString(KEY_WITHDRAWLFEES, user.getWithdrawlFees());
        editor.putString(KEY_CRYTOCURRENCY, user.getCryptoCurrency());
        editor.putString(KEY_USER_ID, user.getUserId());
        editor.putString(KEY_CREATE_AT, user.getCreatedAt());
        editor.putString(KEY_UPDATE, user.getUpdatedAt());
        editor.apply();
    }


    //this method will give the logged in user
    public PearToPearModel getPearToPear() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        return new PearToPearModel(
                sharedPreferences.getString(KEY_ID, null),
                sharedPreferences.getString(KEY_USER_PUBLIC_KEY, null),
                sharedPreferences.getString(KEY_STATUS, null),
                sharedPreferences.getString(KEY_AMTOFCRYPTO, null),
                sharedPreferences.getString(KEY_WITHDRAWLFEES, null),
                sharedPreferences.getString(KEY_TRANSACTION, null),
                sharedPreferences.getString(KEY_CRYTOCURRENCY, null),
                sharedPreferences.getString(KEY_USER_ID, null),
                sharedPreferences.getString(KEY_CREATE_AT, null),
                sharedPreferences.getString(KEY_UPDATE, null)
        );
    }

    //this method will logout the user
    public void clearPearData() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


/*        mCtx.startActivity(new Intent(mCtx, Login_Activity.class));
        mCtx.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
 */
    }
}
