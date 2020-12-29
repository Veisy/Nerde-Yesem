package com.example.nerdeyesem.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NearbyRestaurantsModel {

    @SerializedName("restaurants")
    private List<SingleRestaurantModel> singleRestaurantModelList;

    public List<SingleRestaurantModel> getSingleRestaurantModelList() {
        return singleRestaurantModelList;
    }
}
