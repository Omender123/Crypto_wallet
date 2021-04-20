package com.crypto.croytowallet.VolleyDatabase;

public class URLs {
    //  http://13.233.136.56:8080/api

  // private static final String ROOT_URL = "https://api.imx.global/api/";

  private static final String ROOT_URL = " http://13.233.136.56:8080/api/";

    public static final String URL_LOGIN = ROOT_URL + "login";
    public static final String URL_CHANGE_PASSWORD = ROOT_URL + "user/forgot-password";
    public static final String URL_LOGOUT = ROOT_URL + "user/removeCurrentlyActiveDevices";
    public static final String URL_AIRDROP_BALANCE = ROOT_URL + "user/totalAirDrop/";
    public static final String URL_SET_TRANSACTIONPin = ROOT_URL + "user/transactionPin";

    // peer to peer api
    public static final String URL_PEER_TO_PEER = ROOT_URL + "transaction/peerToPeer";

    // all transaction history api
    public static final String URL_TRANSACTION_HISTORY = ROOT_URL + "transaction/allPeerTransactiones1";
    public static final String URL_TRANSACTION_HISTORY_FULL = ROOT_URL + "transaction/allPeerTransactiones1";

    // google or email 2fa
    public static final String URL_2FA = ROOT_URL + "user/twoFaEnableOrDisable";


    //2fa api
    public static final String URL_USER_DETAILS = ROOT_URL + "user/userInfromation";


    //Active Device
    public static final String URL_ACTIVE_DEVICE = ROOT_URL + "user/findCurrentlyActiveDevices";

    //ADD TICKET
    public static final String URL_ADD_TIKECT = ROOT_URL + "ticket/generateTicket";

    //GET TICKET
    public static final String URL_GET_TIKECT = ROOT_URL + "ticket/myTickets";

    //GET TICKET
    public static final String URL_GET_2FA = ROOT_URL + "user/getTwoFaEnableOrDisable";

    //GET ALL COIN
    public static final String URL_GET_COIN = ROOT_URL + "currency";

    //GET imt COIN
    public static final String URL_GET_COIN_IMT = ROOT_URL + "currency/IMT";

    //GET ALL Currency
    public static final String URL_GET_CURRENCY = ROOT_URL + "user/allCurrencyData";

    //GET GAS FEES
    public static final String URL_GET_GAS_FEES = ROOT_URL + "transaction/getGasFee";

    //GET GAS FEES
    public static final String URL_GET_COIN_HISTORY = ROOT_URL + "transaction/history/user";

    //GET GAS FEES
    public static final String URL_GET_OFFER = ROOT_URL + "offers";

    //GET GAS FEES
    public static final String URL_GET_AMOUNT = ROOT_URL + "settings/Amount";

    //GET GAS FEES
    public static final String URL_DELETE_MESSAGE = ROOT_URL + "chat/removeMessage?id=";


 //Image Urls
 public static final String URL_Image = "https://imxtest.s3.ap-south-1.amazonaws.com/";


}
