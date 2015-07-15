package com.example.user.googlemaptest.model;

/**
 * Created by ASUS on 7/13/2015.
 */
public class AddressPin extends AddressBase{
    private String pinCode;
    private String cityName;

    public AddressPin(Long id, String name, String pinCode, String cityName) {
        super(id, name);
        this.pinCode = pinCode;
        this.cityName = cityName;
    }

    public String getPinCode() {
        return pinCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
