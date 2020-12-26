package com.example.nerdeyesem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.nerdeyesem.livedata.LocationLiveData;

public class LocationViewModel extends AndroidViewModel {
    private final LocationLiveData locationLiveData;
    private MutableLiveData<Boolean> isGPSEnable;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationLiveData = new LocationLiveData(application);
    }

    public LocationLiveData getLocationLiveData() {
        return locationLiveData;
    }

    public MutableLiveData<Boolean> getIsGPSEnable() {
        if(isGPSEnable == null) {
            isGPSEnable = new MutableLiveData<>();
        }
        return isGPSEnable;
    }
}
