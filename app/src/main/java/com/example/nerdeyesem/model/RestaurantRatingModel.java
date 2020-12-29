package com.example.nerdeyesem.model;

import com.google.gson.annotations.SerializedName;

public class RestaurantRatingModel {

    /*
    @SerializedName("aggregate_rating")
    private Integer aggregateRating;
     */

    @SerializedName("rating_text")
    private String ratingText;

    @SerializedName("rating_color")
    private String ratingColor;

    @SerializedName("votes")
    private int votes;

    /*
    public Integer getAggregateRating() {
        return aggregateRating;
    }
     */

    public String getRatingText() {
        return ratingText;
    }

    public String getRatingColor() {
        return ratingColor;
    }

    public int getVotes() {
        return votes;
    }
}
