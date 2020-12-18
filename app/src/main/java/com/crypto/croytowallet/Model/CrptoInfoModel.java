package com.crypto.croytowallet.Model;

public class CrptoInfoModel {
    String id,Name,Image,CurrentPrice,currencyRate;

    public CrptoInfoModel(String id, String name, String image, String currentPrice, String currencyRate) {
        this.id = id;
        Name = name;
        Image = image;
        CurrentPrice = currentPrice;
        this.currencyRate = currencyRate;
    }

    public CrptoInfoModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getCurrentPrice() {
        return CurrentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        CurrentPrice = currentPrice;
    }

    public String getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(String currencyRate) {
        this.currencyRate = currencyRate;
    }
}
