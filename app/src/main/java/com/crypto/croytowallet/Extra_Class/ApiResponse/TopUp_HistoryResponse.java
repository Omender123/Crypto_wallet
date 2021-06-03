package com.crypto.croytowallet.Extra_Class.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TopUp_HistoryResponse {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("accountName")
    @Expose
    private String accountName;
    @SerializedName("accountNo")
    @Expose
    private String accountNo;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("amount")
    @Expose
    private Integer amount;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("attachement")
    @Expose
    private String attachement;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("updatedAt")
    @Expose
    private String updatedAt;
    @SerializedName("utility")
    @Expose
    private String utility;

    /**
     * No args constructor for use in serialization
     *
     */
    public TopUp_HistoryResponse() {
    }

    public TopUp_HistoryResponse(String id, String status, String accountName, String accountNo, String customerName, String transactionId, Integer amount, String currency, String attachement, String userId, String createdAt, String updatedAt, String utility) {
        super();
        this.id = id;
        this.status = status;
        this.accountName = accountName;
        this.accountNo = accountNo;
        this.customerName = customerName;
        this.transactionId = transactionId;
        this.amount = amount;
        this.currency = currency;
        this.attachement = attachement;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.utility = utility;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getAttachement() {
        return attachement;
    }

    public void setAttachement(String attachement) {
        this.attachement = attachement;
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

    public String getUtility() {
        return utility;
    }

    public void setUtility(String utility) {
        this.utility = utility;
    }
}
