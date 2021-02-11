package com.crypto.croytowallet.Model;

public class ActiveDeviceModel {
    String OS_Name,IP_Address,Location,jwt;

    public ActiveDeviceModel(String OS_Name, String IP_Address, String location,String jwt) {
        this.OS_Name = OS_Name;
        this.IP_Address = IP_Address;
        this.Location = location;
        this.jwt = jwt;
    }

    public ActiveDeviceModel() {
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getOS_Name() {
        return OS_Name;
    }

    public void setOS_Name(String OS_Name) {
        this.OS_Name = OS_Name;
    }

    public String getIP_Address() {
        return IP_Address;
    }

    public void setIP_Address(String IP_Address) {
        this.IP_Address = IP_Address;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }
}
