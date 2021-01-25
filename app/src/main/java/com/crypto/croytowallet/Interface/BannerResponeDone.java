package com.crypto.croytowallet.Interface;

import com.crypto.croytowallet.Model.BannerModel;

import java.util.List;

public interface BannerResponeDone {

    void onResponseLoadSuccess(List<BannerModel> bannerModels);
    void onErrorLoadFaild(String message);

}
