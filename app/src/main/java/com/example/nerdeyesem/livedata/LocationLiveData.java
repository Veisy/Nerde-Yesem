package com.example.nerdeyesem.livedata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.lifecycle.LiveData;

import com.example.nerdeyesem.model.LocationModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationLiveData extends LiveData<LocationModel> {
    private static final int DEFAULT_UPDATE_INTERVAL = 60;
    private static final int FAST_UPDATE_INTERVAL = 30;

    //Location request is a config file for all settings related to FusedLocationProviderClient.
    public static LocationRequest locationRequest = LocationRequest.create();

    static {
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private final FusedLocationProviderClient fusedLocationClient;
    private final LocationCallback locationCallback;

    public LocationLiveData(Context context) {
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    setLocationData(location);
                }
            }
        };
    }

    private void setLocationData(Location location) {
        if (location != null) {
            postValue(new LocationModel(location.getLongitude(), location.getLatitude()));
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onActive() {
        super.onActive();
        fusedLocationClient.getLastLocation().addOnSuccessListener(this::setLocationData);
        startLocationUpdates();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

}
