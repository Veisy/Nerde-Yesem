package com.example.nerdeyesem.network;

import com.example.nerdeyesem.BuildConfig;
import com.example.nerdeyesem.ui.restaurantsmaster.RestaurantsModel;
import com.example.nerdeyesem.ui.restaurantsdetail.ReviewsModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ZomatoApiService {

    // Enter your valid Zomato Api Key here.
    // Delete BuildConfig.ApiKey, and replace "yourApiKey"
    String API_KEY = BuildConfig.ApiKey;
    String BASE_URL = "https://developers.zomato.com/api/v2.1/";

    @GET("search")
    Call<RestaurantsModel> getRestaurants(@Header("user-key") String userKey,
                                          @Query("count") Integer count,
                                          @Query("lat") Double latitude,
                                          @Query("lon") Double longitude,
                                          @Query("sort") String sort);

    @GET("reviews")
    Call<ReviewsModel> getReviews(@Header("user-key") String userKey,
                                  @Query("res_id") Integer resId);

}
