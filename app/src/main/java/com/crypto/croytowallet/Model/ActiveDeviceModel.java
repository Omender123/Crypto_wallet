package com.crypto.croytowallet.Model;

public class ActiveDeviceModel {
    String OS_Name,IP_Address,Location;

    public ActiveDeviceModel(String OS_Name, String IP_Address, String location) {
        this.OS_Name = OS_Name;
        this.IP_Address = IP_Address;
        Location = location;
    }

    public ActiveDeviceModel() {
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
