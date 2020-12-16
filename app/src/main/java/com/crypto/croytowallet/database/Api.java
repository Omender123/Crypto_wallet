package com.crypto.croytowallet.database;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("user/register")
    Call<ResponseBody> register(

            @Field("name") String name,
            @Field("email") String email,
            @Field("username") String username,
            @Field("password") String password,
            @Field("referalCode")String referalCode,
            @Field("phone") String phone);

    @FormUrlEncoded
    @POST("user/transactionPin")
    Call<ResponseBody> Transaction(

            @Field("mnemonic") String mnemonic,
            @Field("transactionPin") String TransactionPin,
            @Field("username") String username);

}
