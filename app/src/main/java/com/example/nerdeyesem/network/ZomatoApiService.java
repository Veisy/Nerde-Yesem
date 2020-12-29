package com.example.nerdeyesem.network;

import com.example.nerdeyesem.model.NearbyRestaurantsModel;
import com.example.nerdeyesem.model.RestaurantModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ZomatoApiService {

    @GET("search")
    Call<NearbyRestaurantsModel> getRestaurants(@Header("user-key") String userKey,
                                                @Query("count") Integer count,
                                                @Query("lat") Double latitude,
                                                @Query("lon") Double longitude,
                                                @Query("sort") String sort);
}
