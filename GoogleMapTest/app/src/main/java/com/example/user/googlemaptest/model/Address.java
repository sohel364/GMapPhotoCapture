package com.example.user.googlemaptest.model;

/**
 * Created by user on 5/24/2015.
 */
public class Address {
    /*
    * {"0":"1","id":"1","1":"Rama Automobile","name":"Rama Automobile","2":" School Malviya, Sheikh Sarai 1|more...   B102, Panchsheel Vihar, Sheikh Sar","address":" School Malviya, Sheikh Sarai 1|more...   B102, Panchsheel Vihar, Sheikh Sar","3":" 28.535842000000","longitude":" 28.535842000000","4":" 77.222953000000","latitude":" 77.222953000000"}
    * */
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;

    public Address(Long id, String name, Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
