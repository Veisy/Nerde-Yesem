package com.example.nerdeyesem.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewsModel {

    @SerializedName("reviews_count")
    private Integer reviewCount;
    @SerializedName("user_reviews")
    private List<Review> reviews;

    public Integer getReviewCount() {
        return reviewCount;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public class Review {
        @SerializedName("review")
        private ChildReview childReview;

        public ChildReview getChildReview() {
            return childReview;
        }
    }

    public class ChildReview {
        @SerializedName("rating")
        private Double rating;

        @SerializedName("rating_text")
        private String ratingText;
        @SerializedName("review_text")
        private String reviewText;
        @SerializedName("review_time_friendly")
        private String reviewTime;

        @SerializedName("rating_color")
        private String ratingColor;

        public Double getRating() {
            return rating;
        }

        public String getRatingText() {
            return ratingText;
        }

        public String getReviewText() {
            return reviewText;
        }

        public String getReviewTime() {
            return reviewTime;
        }

        public String getRatingColor() {
            return ratingColor;
        }


    }

}
