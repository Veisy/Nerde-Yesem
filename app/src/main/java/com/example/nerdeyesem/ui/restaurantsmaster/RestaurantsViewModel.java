package com.example.nerdeyesem.ui.restaurantsmaster;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.nerdeyesem.utils.Resource;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class RestaurantsViewModel extends ViewModel {

    private final ZomatoRestaurantsRepository zomatoRestaurantsRepository;

    @Inject
    public RestaurantsViewModel(ZomatoRestaurantsRepository zomatoRestaurantsRepository) {
        this.zomatoRestaurantsRepository = zomatoRestaurantsRepository;
    }

    public void findRestaurants (Double latitude,
                                 Double longitude) {
        zomatoRestaurantsRepository.findRestaurants(latitude, longitude);
    }

    public LiveData<Resource<RestaurantsModel>> getRestaurants() {
        return zomatoRestaurantsRepository.getRestaurants();

    }
}
