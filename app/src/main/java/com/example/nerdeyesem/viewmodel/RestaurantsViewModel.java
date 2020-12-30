package com.example.nerdeyesem.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nerdeyesem.model.RestaurantsModel;
import com.example.nerdeyesem.repository.ZomatoRestaurantsRepository;
import com.example.nerdeyesem.utils.Resource;

public class RestaurantsViewModel extends ViewModel {

    private final ZomatoRestaurantsRepository zomatoRestaurantsRepository;

    public RestaurantsViewModel() {
        zomatoRestaurantsRepository = new ZomatoRestaurantsRepository();
    }

    public void findRestaurants (Double latitude,
                                 Double longitude) {
        zomatoRestaurantsRepository.findRestaurants(latitude, longitude);
    }

    public LiveData<Resource<RestaurantsModel>> getRestaurants() {
        return zomatoRestaurantsRepository.getRestaurants();

    }
}
