package com.crypto.croytowallet.SharedPrefernce;

public class SignUpData {
    private String username,mnemonic,googleAuthKey;

    public SignUpData(String username, String mnemonic,String googleAuthKey) {
        this.username = username;
        this.mnemonic = mnemonic;
        this.googleAuthKey = googleAuthKey;
    }

    public SignUpData() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getGoogleAuthKey() {
        return googleAuthKey;
    }

    public void setGoogleAuthKey(String googleAuthKey) {
        this.googleAuthKey = googleAuthKey;
    }
}
