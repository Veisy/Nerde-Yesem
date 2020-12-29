package com.example.nerdeyesem.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleRestaurantModel {

    @SerializedName("restaurant")
    private RestaurantModel restaurantModel;

    public  RestaurantModel getRestaurantModel() {
        return restaurantModel;
    }
}
