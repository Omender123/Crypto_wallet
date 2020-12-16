package com.crypto.croytowallet.SharedPrefernce;

public class SignUpData {
    private String username,mnemonic;

    public SignUpData(String username, String mnemonic) {
        this.username = username;
        this.mnemonic = mnemonic;
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
}
