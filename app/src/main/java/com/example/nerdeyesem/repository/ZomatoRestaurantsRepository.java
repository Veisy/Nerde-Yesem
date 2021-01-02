package com.example.nerdeyesem.repository;

import androidx.lifecycle.LiveData;

import com.example.nerdeyesem.MainActivity;
import com.example.nerdeyesem.livedata.SingleLiveEvent;
import com.example.nerdeyesem.model.RestaurantsModel;
import com.example.nerdeyesem.network.ZomatoApiClient;
import com.example.nerdeyesem.network.ZomatoApiService;
import com.example.nerdeyesem.utils.Resource;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZomatoRestaurantsRepository {

    private static final Integer COUNT = 20;
    private static final String SORT_BY_REAL_DISTANCE = "real_distance";

    private final ZomatoApiService zomatoApiService;

    //Extended LiveData class as SingleLiveEvent(in the livedata package) that will only send an update once.
    //We needed it because in some scenarios livedata was updated more than once without updating location.
    //More details and explanations available on class declaration.
    private final SingleLiveEvent<Resource<RestaurantsModel>> resourceSingleLiveEvent;

    public ZomatoRestaurantsRepository() {
        zomatoApiService = ZomatoApiClient.getRetrofit().create(ZomatoApiService.class);
        resourceSingleLiveEvent = new SingleLiveEvent<>();
    }

    public void findRestaurants(Double latitude, Double longitude) {
        zomatoApiService.getRestaurants(MainActivity.API_KEY, COUNT, latitude, longitude, SORT_BY_REAL_DISTANCE)
                .enqueue(new Callback<RestaurantsModel>() {
                    @Override
                    public void onResponse(@NotNull Call<RestaurantsModel> call,
                                           @NotNull Response<RestaurantsModel> response) {
                        if (!response.isSuccessful()) {
                            resourceSingleLiveEvent.setValue(Resource
                                    .error(String.valueOf(response.code()), null));
                            return;
                        }

                        if (response.body() != null) {
                            // Response successful.
                            resourceSingleLiveEvent.setValue(Resource.success(response.body()));
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<RestaurantsModel> call,
                                          @NotNull Throwable t) {
                        resourceSingleLiveEvent.setValue(Resource.error(t.getMessage(), null));
                    }
                });
    }

    public LiveData<Resource<RestaurantsModel>> getRestaurants() {
        return resourceSingleLiveEvent;
    }

}
