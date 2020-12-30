package com.example.nerdeyesem.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.nerdeyesem.model.RestaurantsModel;
import com.example.nerdeyesem.network.ZomatoApiClient;
import com.example.nerdeyesem.network.ZomatoApiService;
import com.example.nerdeyesem.utils.Resource;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZomatoRestaurantsRepository {

    //For now, I am sharing the API key here, for you to test the application.
    // And that's why I had to hide my repo.
    // Then I will keep this api key as environment variable and make the repo public.
    private static final String API_KEY = "17361d6eadaae515bd66272977b5e85f";
    private static final Integer COUNT = 100;
    private static final String SORT_BY_REAL_DISTANCE = "real_distance";

    private final ZomatoApiService zomatoApiService;
    private final MutableLiveData<Resource<RestaurantsModel>> resourceMutableLiveData;

    public ZomatoRestaurantsRepository() {
        zomatoApiService = ZomatoApiClient.getRetrofit().create(ZomatoApiService.class);
        resourceMutableLiveData = new MutableLiveData<>();
    }

    public void findRestaurants(Double latitude, Double longitude) {
        zomatoApiService.getRestaurants(API_KEY, COUNT, latitude, longitude, SORT_BY_REAL_DISTANCE)
                .enqueue(new Callback<RestaurantsModel>() {
                    @Override
                    public void onResponse(@NotNull Call<RestaurantsModel> call,
                                           @NotNull Response<RestaurantsModel> response) {
                        if (!response.isSuccessful()) {
                            resourceMutableLiveData.postValue(Resource
                                    .error(String.valueOf(response.code()), null));
                            return;
                        }

                        if (response.body() != null) {
                            // Response successful.
                            resourceMutableLiveData.postValue(Resource.success(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RestaurantsModel> call,
                                          @NotNull Throwable t) {
                        resourceMutableLiveData.postValue(Resource.error(t.getMessage(), null));
                    }
                });
    }

    public LiveData<Resource<RestaurantsModel>> getRestaurants() {
        return resourceMutableLiveData;
    }

}
