package com.crypto.croytowallet.VolleyDatabase;

public class URLs {


    private static final String ROOT_URL = "http://13.233.136.56:8080/api/";

    public static final String URL_LOGIN= ROOT_URL + "login";
    public static final String URL_CHANGE_PASSWORD= ROOT_URL + "user/forgot-password";
    public static final String URL_LOGOUT= ROOT_URL + "user/removeCurrentlyActiveDevices";
    public static final String URL_AIRDROP_BALANCE= ROOT_URL + "user/totalAirDrop/";
    public static final String URL_SET_TRANSACTIONPin= ROOT_URL + "user/transactionPin";

    // peer to peer api
    public static final String URL_PEER_TO_PEER= ROOT_URL + "transaction/peerToPeer";

    //add money to wallet api
    public static final String URL_CREATE_ORDER_ID= ROOT_URL + "razorpay/order";
    public static final String URL_CAPTURE_ORDER= ROOT_URL + "razorpay/capture";
    public static final String URL_SAVE_DATA= ROOT_URL + "razorpay/data?id=";

    // all transaction history api
    public static final String URL_TRANSACTION_HISTORY= ROOT_URL + "transaction/all?page=0&limit=10";
    public static final String URL_TRANSACTION_HISTORY_FULL= ROOT_URL + "transaction/all?page=0&limit=30";

    // google or email 2fa
    public static final String URL_2FA= ROOT_URL + "user/twoFaEnableOrDisable";

    //coinTransfer
    public static final String URL_COIN_TRANSFER= ROOT_URL + "transaction/transfer";

    //2fa api
    public static final String URL_EMAIL_VERIFY= ROOT_URL + "user/emailVerify";

}
