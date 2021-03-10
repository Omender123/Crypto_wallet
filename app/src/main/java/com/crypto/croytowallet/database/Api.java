package com.crypto.croytowallet.database;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
            @Field("password") String password,
            @Field("otp") String otp,
            @Field("location") String location,
            @Field("os") String OsName,
            @Field("ip") String IpAddress
            );

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

    @FormUrlEncoded
    @POST("transaction/peerToPeer")
    Call<ResponseBody>P2P(
            @Header("Authorization")String token,
            @Field("amount") String amount,
            @Field("transactionPin") String transactionPin,
            @Field("toUserId") String userId,
            @Field("toUsername") String username
    );

    @FormUrlEncoded
    @POST("transaction/transfer")
    Call<ResponseBody>coinTransfer(
            @Header("Authorization")String Authtoken,
            @Field("cryptoCurrency") String crptoCurency,
            @Field("deliveryRate") String deliveryRate,
            @Field("token") String token,
            @Field("otp") String otp,
            @Field("cryptoAmt") String amount,
            @Field("transactionPin") String transactionPin,
          /*  @Field("userAddress") String userAddress,*/
            @Field("receiverAddress") String receiverAddress
    );

    @FormUrlEncoded
    @POST("user/balance")
    Call<ResponseBody>Balance(
            @Header("Authorization")String Authtoken,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("user/balance")
    Call<ResponseBody>AirDropBalance(
            @Header("Authorization")String Authtoken,
            @Field("type") String type,
            @Field("currency") String currency
    );

    @FormUrlEncoded
    @POST("transaction/swapCurrency")
    Call<ResponseBody>IMT_SWAP(
            @Header("Authorization")String Authtoken,
            @Field("sendCurrency") String sendCurrency,
            @Field("receiveCurrency") String receiveCurrency,
            @Field("deliveryRate") int rate,
            @Field("sendAmount") String sendAmount,
            @Field("transactionPin") String transactionPin,
            @Field("userAddress") String userAddress
    );

    @FormUrlEncoded
    @POST("user/appCrashed/transactionPin")
    Call<ResponseBody> setTransactionPin(
            @Field("username") String username,
             @Field("transactionPin") String transactionPin,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("user/removeCurrentlyActiveDevices")
    Call<ResponseBody>remove_JWT(
            @Header("Authorization")String Authtoken,
            @Field("username") String username,
            @Field("jwt") String jwt
    );

    @FormUrlEncoded
    @POST("user/sendEmailAfterLogin")
    Call<ResponseBody>Send_Email(
            @Field("userId") String userId
    );

    @FormUrlEncoded
    @POST("user/viewMnemonic")
    Call<ResponseBody>get_Mnenonic(
            @Header("Authorization")String Authtoken,
            @Field("userId") String userId,
            @Field("transactionPin") String transactionPin,
            @Field("otp") String otp
    );

    @FormUrlEncoded
    @POST("transaction/send/user")
    Call<ResponseBody>get_SendHistory(
            @Header("Authorization")String Authtoken,
            @Field("crypto") String type
    );

    @FormUrlEncoded
    @POST("transaction/receive/user")
    Call<ResponseBody>get_ReceivedHistory(
            @Header("Authorization")String Authtoken,
            @Field("crypto") String type
    );

    @GET("banner")
    Call<ResponseBody> getStory(
            @Header("Authorization") String token
    );

   // @FormUrlEncoded
   // @Multipart
    @POST("chat/chatToAdmin")
    Call<ResponseBody> SendMessageApi( @Header("Authorization") String auth_token,
                              @Body JsonObject object);


  //  @FormUrlEncoded
    @POST("chat/insertUser")
    Call<ResponseBody>ChatActive(
            @Header("Authorization")String Authtoken
    );

    @POST("chat/newChatDocument")
    Call<ResponseBody>newChatSeason(
            @Header("Authorization")String Authtoken
    );
    @POST("chat/ejectUser")
    Call<ResponseBody>Chat_Un_Active(
            @Header("Authorization")String Authtoken
    );
    @GET("chat/getMessages")
    Call<ResponseBody> getChat(
            @Header("Authorization") String Authorization
    );

    @DELETE("chat/deletAllMessage")
    Call<ResponseBody> deleteAllMessage(@Header("Authorization") String Authorization);

    @DELETE("api/chat/removeMessage/{id}")
    Call<ResponseBody> deleteMessage(@Header("Authorization") String Authorization,@Path("id") String messageId);


    @FormUrlEncoded
    @POST("user/threatMode")
    Call<ResponseBody>Threat_mode_Api(
            @Header("Authorization")String Authtoken,
            @Field("password") String password,
            @Field("otp") String otp,
            @Field("transactionPin") String transactionPin
    );

    @FormUrlEncoded
    @POST("user/unlockAccount")
    Call<ResponseBody>Unlock_Account_Api(
             @Field("username") String username,
            @Field("transactionPin") String transactionPin,
             @Field("otp") String otp,
             @Field("password") String new_password,
             @Field("mnemonic") String Mnemonic

    );

    @GET("user/countryCodeList")
    Call<ResponseBody> getCountryCode();

    @GET("chat/oldChats")
    Call<ResponseBody> getChatHistory(
            @Header("Authorization") String Authorization
    );

}
