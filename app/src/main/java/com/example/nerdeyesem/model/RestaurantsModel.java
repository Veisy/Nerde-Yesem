package com.example.nerdeyesem.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

//Model class of requested data from Zomato API.
//There are inner classes. Because available JSONs of the Zomato API are not flat.
public class RestaurantsModel {
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
        @SerializedName("cuisines")
        private String cuisines;
        @SerializedName("timings")
        private String timings;
        @SerializedName("highlights")
        private List<String> highlights;
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

        public String getTimings() {
            return timings;
        }

        public List<String> getHighlights() {
            return highlights;
        }

        public String getCuisines() {
            return cuisines;
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
        @SerializedName("locality_verbose")
        private String localityVerbose;

        public String getAddress() {
            return address;
        }

        public String getLocalityVerbose() {
            return localityVerbose;
        }
    }

    public class RestaurantRatingModel {

        @SerializedName("aggregate_rating")
        private Double aggregateRating;
        @SerializedName("rating_text")
        private String ratingText;
        @SerializedName("rating_color")
        private String ratingColor;
        @SerializedName("votes")
        private Integer votes;

        public Double getAggregateRating() {
        return aggregateRating;
        }

        public String getRatingText() {
            return ratingText;
        }

        public String getRatingColor() {
            return ratingColor;
        }

        public Integer getVotes() {
            return votes;
        }
    }
}
