package com.example.nerdeyesem.location;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.example.nerdeyesem.livedata.LocationLiveData;
import com.example.nerdeyesem.livedata.SingleLiveEvent;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LocationViewModel extends ViewModel {
    private final LocationLiveData locationLiveData;
    private SingleLiveEvent<Boolean> isGPSEnable;

    @Inject
    public LocationViewModel(Application application) {
        locationLiveData = new LocationLiveData(application);
    }

    public LocationLiveData getLocationLiveData() {
        return locationLiveData;
    }

    public SingleLiveEvent<Boolean> getIsGPSEnable() {
        if(isGPSEnable == null) {
            isGPSEnable = new SingleLiveEvent<>();
        }
        return isGPSEnable;
    }
}
