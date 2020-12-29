package com.example.nerdeyesem.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//Model class of requested data from Zomato API.
//There are inner classes. Because the available JSONs of the Zomato API are not flat.
public class NearbyRestaurantsModel {
    @SerializedName("restaurants")
    private List<SingleRestaurantModel> singleRestaurantModelList;

    public List<SingleRestaurantModel> getSingleRestaurantModelList() {
        return singleRestaurantModelList;
    }

    public class SingleRestaurantModel {
        @SerializedName("restaurant")
        private RestaurantModel restaurantModel;

        public   RestaurantModel getRestaurantModel() {
            return restaurantModel;
        }
    }

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

    public class RestaurantAddressModel {
        @SerializedName("address")
        private String address;

        public String getAddress() {
            return address;
        }
    }

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
}
