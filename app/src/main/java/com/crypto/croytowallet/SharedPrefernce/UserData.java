package com.crypto.croytowallet.SharedPrefernce;

public class UserData {
    private int id;
    private String name, email, mobile,username,mnemonic,token;


    public UserData(int id, String name, String email, String mobile,String username,String mnemonic, String token ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.username=username;
        this.mnemonic=mnemonic;
        this.token=token;
    }

    public UserData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
