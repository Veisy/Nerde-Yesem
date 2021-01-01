package com.example.nerdeyesem.livedata;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import androidx.annotation.MainThread;

import com.example.nerdeyesem.model.LocationModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;


//Extended LiveData class as SingleLiveEvent (in the livedata package) that will only send an update once.
//We needed it because in some scenarios livedata was updated more than once without updating location.
public class LocationLiveData extends SingleLiveEvent<LocationModel> {

    //Location request is a config file for all settings related to FusedLocationProviderClient.
    public static LocationRequest locationRequest = LocationRequest.create();

    // Don't be afraid of the frequency values of update intervals below,
    // we will remove location updates with removeLocationUpdates after we get one valid location.
    // Even though we setNumUpdates(1), Android docs says don't trust this method
    // because numbers of things can go wrong, and remove updates anyway.
    static {
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setNumUpdates(1);
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

    @MainThread
    private void setLocationData(Location location) {
        if (location != null) {
            //We got a valid location, remove updates.
            fusedLocationClient.removeLocationUpdates(locationCallback);
            setValue(new LocationModel(location.getLongitude(), location.getLatitude()));
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    @SuppressLint("MissingPermission")
    @MainThread
    @Override
    protected void onActive() {
        super.onActive();
        fusedLocationClient.getLastLocation().addOnSuccessListener(this::setLocationData);
        startLocationUpdates();
    }

    @MainThread
    @Override
    protected void onInactive() {
        super.onInactive();
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
