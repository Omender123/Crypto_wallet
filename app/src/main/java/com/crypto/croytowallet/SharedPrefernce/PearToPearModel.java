package com.crypto.croytowallet.SharedPrefernce;

public class PearToPearModel {
    private String id;
    private String status, amtOfCrypto;

    public PearToPearModel(String id, String status, String amtOfCrypto) {
        this.id = id;
        this.status = status;
        this.amtOfCrypto = amtOfCrypto;
    }

    public PearToPearModel() {
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
}
