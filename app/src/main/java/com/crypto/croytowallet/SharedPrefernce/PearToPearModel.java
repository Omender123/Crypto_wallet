package com.crypto.croytowallet.SharedPrefernce;

public class PearToPearModel {
    private String id;
    private String userPublicKey, status, amtOfCrypto, withdrawlFees, transactionHash, cryptoCurrency, userId, createdAt,updatedAt;

    public PearToPearModel(String id, String userPublicKey, String status, String amtOfCrypto, String withdrawlFees, String transactionHash, String cryptoCurrency, String userId, String createdAt, String updatedAt) {
        this.id = id;
        this.userPublicKey = userPublicKey;
        this.status = status;
        this.amtOfCrypto = amtOfCrypto;
        this.withdrawlFees = withdrawlFees;
        this.transactionHash = transactionHash;
        this.cryptoCurrency = cryptoCurrency;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public PearToPearModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserPublicKey() {
        return userPublicKey;
    }

    public void setUserPublicKey(String userPublicKey) {
        this.userPublicKey = userPublicKey;
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

    public String getWithdrawlFees() {
        return withdrawlFees;
    }

    public void setWithdrawlFees(String withdrawlFees) {
        this.withdrawlFees = withdrawlFees;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getCryptoCurrency() {
        return cryptoCurrency;
    }

    public void setCryptoCurrency(String cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
