package com.example.nerdeyesem.model;

import com.google.gson.annotations.SerializedName;

public class RestaurantModel {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("featured_image")
    private String featuredImage;

    @SerializedName("location")
    private RestaurantAddressModel restaurantAddress;

    @SerializedName("user_rating")
    private RestaurantRatingModel restaurantRating;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public RestaurantAddressModel getRestaurantAddress() {
        return restaurantAddress;
    }

    public RestaurantRatingModel getRestaurantRating() {
        return restaurantRating;
    }
}
