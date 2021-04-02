package com.crypto.croytowallet.Model;

public class SwapModel {
    String sendData, receivedData, coinPrice, currencyType,currencySymbol,coinAmount,enterAmount;
    int value;

    public SwapModel(String sendData, String receivedData, String coinPrice, String currencyType, String currencySymbol, String coinAmount, String enterAmount, int value) {
        this.sendData = sendData;
        this.receivedData = receivedData;
        this.coinPrice = coinPrice;
        this.currencyType = currencyType;
        this.currencySymbol = currencySymbol;
        this.coinAmount = coinAmount;
        this.enterAmount = enterAmount;
        this.value = value;
    }

    public SwapModel() {
    }

    public String getSendData() {
        return sendData;
    }

    public void setSendData(String sendData) {
        this.sendData = sendData;
    }

    public String getReceivedData() {
        return receivedData;
    }

    public void setReceivedData(String receivedData) {
        this.receivedData = receivedData;
    }

    public String getCoinPrice() {
        return coinPrice;
    }

    public void setCoinPrice(String coinPrice) {
        this.coinPrice = coinPrice;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(String coinAmount) {
        this.coinAmount = coinAmount;
    }

    public String getEnterAmount() {
        return enterAmount;
    }

    public void setEnterAmount(String enterAmount) {
        this.enterAmount = enterAmount;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
