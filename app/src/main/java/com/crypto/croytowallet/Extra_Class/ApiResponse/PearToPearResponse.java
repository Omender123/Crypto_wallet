package com.crypto.croytowallet.Extra_Class.ApiResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class PearToPearResponse {
    @SerializedName("result")
    @Expose
    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result {

        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("_id")
        @Expose
        private String id;
        @SerializedName("senderId")
        @Expose
        private String senderId;
        @SerializedName("receiverId")
        @Expose
        private String receiverId;
        @SerializedName("closingBalance")
        @Expose
        private Integer closingBalance;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("amount")
        @Expose
        private Integer amount;
        @SerializedName("withdrawlFees")
        @Expose
        private Integer withdrawlFees;
        @SerializedName("senderName")
        @Expose
        private String senderName;
        @SerializedName("receiverName")
        @Expose
        private String receiverName;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("updatedAt")
        @Expose
        private String updatedAt;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public Integer getClosingBalance() {
            return closingBalance;
        }

        public void setClosingBalance(Integer closingBalance) {
            this.closingBalance = closingBalance;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        public Integer getWithdrawlFees() {
            return withdrawlFees;
        }

        public void setWithdrawlFees(Integer withdrawlFees) {
            this.withdrawlFees = withdrawlFees;
        }

        public String getSenderName() {
            return senderName;
        }

        public void setSenderName(String senderName) {
            this.senderName = senderName;
        }

        public String getReceiverName() {
            return receiverName;
        }

        public void setReceiverName(String receiverName) {
            this.receiverName = receiverName;
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
}
