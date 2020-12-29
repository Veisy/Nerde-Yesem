package com.example.nerdeyesem.model;

import com.google.gson.annotations.SerializedName;

public class RestaurantAddressModel {

    @SerializedName("address")
    private String address;

    public String getAddress() {
        return address;
    }
}
