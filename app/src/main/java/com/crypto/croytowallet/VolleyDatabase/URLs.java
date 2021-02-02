package com.crypto.croytowallet.VolleyDatabase;

public class URLs {
  //  http://13.233.136.56:8080/api

   private static final String ROOT_URL = "https://api.imx.global/api/";

 //private static final String ROOT_URL = " http://13.233.136.56:8080/api";

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
    public static final String URL_TRANSACTION_HISTORY= ROOT_URL + "transaction/allPeerTransactiones1";
    public static final String URL_TRANSACTION_HISTORY_FULL= ROOT_URL + "transaction/allPeerTransactiones1";

    // google or email 2fa
    public static final String URL_2FA= ROOT_URL + "user/twoFaEnableOrDisable";

    //coinTransfer
    public static final String URL_COIN_TRANSFER= ROOT_URL + "transaction/transfer";

    //2fa api
    public static final String URL_USER_DETAILS= ROOT_URL + "user/userInfromation";

    //Banner api
    public static final String URL_BANNER= ROOT_URL + "banner";

    //Active Device
    public static final String URL_ACTIVE_DEVICE= ROOT_URL + "user/findCurrentlyActiveDevices";

    //ADD TICKET
    public static final String URL_ADD_TIKECT= ROOT_URL + "ticket/generateTicket";

    //GET TICKET
    public static final String URL_GET_TIKECT= ROOT_URL + "ticket/myTickets";

    //GET TICKET
    public static final String URL_GET_2FA= ROOT_URL + "user/getTwoFaEnableOrDisable";

 //GET ALL COIN
 public static final String URL_GET_COIN= ROOT_URL + "currency";


}
