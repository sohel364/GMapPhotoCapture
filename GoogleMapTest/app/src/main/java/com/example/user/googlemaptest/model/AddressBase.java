package com.example.user.googlemaptest.model;

/**
 * Created by ASUS on 7/13/2015.
 */
public class AddressBase {
    private Long id;
    private String name;

    public AddressBase(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
