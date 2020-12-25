package com.example.nerdeyesem.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.nerdeyesem.livedata.LocationLiveData;

public class LocationViewModel extends AndroidViewModel {
    private final LocationLiveData locationLiveData;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        locationLiveData = new LocationLiveData(application);
    }

    public LocationLiveData getLocationLiveData() {
        return locationLiveData;
    }
}
