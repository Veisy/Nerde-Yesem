package com.example.nerdeyesem.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nerdeyesem.model.NearbyRestaurantsModel;
import com.example.nerdeyesem.repository.ZomatoRestaurantsRepository;
import com.example.nerdeyesem.utils.Resource;

public class RestaurantViewModel extends ViewModel {

    private final ZomatoRestaurantsRepository zomatoRestaurantsRepository;

    public RestaurantViewModel () {
        zomatoRestaurantsRepository = new ZomatoRestaurantsRepository();
    }

    public LiveData<Resource<NearbyRestaurantsModel>> getRestaurants(Double latitude,
                                                                     Double longitude) {
        return zomatoRestaurantsRepository.getRestaurants(latitude, longitude);

    }
}
