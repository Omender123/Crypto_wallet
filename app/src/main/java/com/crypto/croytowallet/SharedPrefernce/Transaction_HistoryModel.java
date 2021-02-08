package com.crypto.croytowallet.SharedPrefernce;

public class Transaction_HistoryModel {
    private String id;
    private String status, amtOfCrypto,senderName,ReciverName,Time;


    public Transaction_HistoryModel(String id, String status, String amtOfCrypto, String senderName, String reciverName, String time) {
        this.id = id;
        this.status = status;
        this.amtOfCrypto = amtOfCrypto;
        this.senderName = senderName;
        ReciverName = reciverName;
        Time = time;
    }

    public Transaction_HistoryModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmtOfCrypto() {
        return amtOfCrypto;
    }

    public void setAmtOfCrypto(String amtOfCrypto) {
        this.amtOfCrypto = amtOfCrypto;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReciverName() {
        return ReciverName;
    }

    public void setReciverName(String reciverName) {
        ReciverName = reciverName;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
