package com.example.nerdeyesem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.nerdeyesem.livedata.LocationLiveData;
import com.example.nerdeyesem.livedata.SingleLiveEvent;

public class LocationViewModel extends AndroidViewModel {
    private final LocationLiveData locationLiveData;
    private SingleLiveEvent<Boolean> isGPSEnable;

    public LocationViewModel(@NonNull Application application) {
        super(application);
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
