package com.example.user.googlemaptest.model;

/**
 * Created by user on 5/24/2015.
 */
public class AddressLatLon extends AddressBase{
    /*
    * {"0":"1","id":"1","1":"Rama Automobile","name":"Rama Automobile","2":" School Malviya, Sheikh Sarai 1|more...   B102, Panchsheel Vihar, Sheikh Sar","address":" School Malviya, Sheikh Sarai 1|more...   B102, Panchsheel Vihar, Sheikh Sar","3":" 28.535842000000","longitude":" 28.535842000000","4":" 77.222953000000","latitude":" 77.222953000000"}
    * */

    private Double latitude;
    private Double longitude;


    public AddressLatLon(Long id, String name, Double latitude, Double longitude) {
        super(id, name);
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
