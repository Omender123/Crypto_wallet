package com.crypto.croytowallet.database;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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
    @POST("user/login")
    Call<ResponseBody> Login(
            @Field("username") String username,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("user/transactionPin")
    Call<ResponseBody> Transaction(

            @Field("mnemonic") String mnemonic,
            @Field("transactionPin") String TransactionPin,
            @Field("username") String username);


    @FormUrlEncoded
    @POST("user/sendOTP")
    Call<ResponseBody>sendOtp(
            @Field("username") String username
    );


    @FormUrlEncoded
    @POST("user/otpExpiry")
    Call<ResponseBody>expireOtp(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("user/emailVerify")
    Call<ResponseBody>OtpVerify(
            @Field("username") String username,
            @Field("otp") String otp

            );

    @FormUrlEncoded
    @POST("user/checkGoogleAuthentication")
    Call<ResponseBody>GoogleAuthVerify(
            @Field("username") String username,
            @Field("token") String token

    );

    @FormUrlEncoded
    @PUT("user/forgot-password")
    Call<ResponseBody>ChanagePassword(
            @Field("username") String username,
            @Field("mnemonic") String mnemonic,
            @Field("password") String password,
            @Field("otp")  String Otp
    );

}
