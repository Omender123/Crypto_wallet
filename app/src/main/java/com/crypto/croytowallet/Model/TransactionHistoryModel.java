package com.crypto.croytowallet.Model;

public class TransactionHistoryModel {
    String id;
   String username,status,amountTrans,date;

    public TransactionHistoryModel(String username, String status, String amountTrans, String date,String id) {
        this.username = username;
        this.status = status;
        this.amountTrans = amountTrans;
        this.date = date;
    }

    public TransactionHistoryModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmountTrans() {
        return amountTrans;
    }

    public void setAmountTrans(String amountTrans) {
        this.amountTrans = amountTrans;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
