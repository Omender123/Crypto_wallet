package com.crypto.croytowallet.Model;

public class BannerModel {
    String imageurl;

    public BannerModel(String imageurl) {
        this.imageurl = imageurl;
    }

    public BannerModel() {
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
