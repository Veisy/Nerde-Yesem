package com.example.nerdeyesem.location;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.LocationManager;
import android.util.Log;

import com.example.nerdeyesem.livedata.LocationLiveData;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import static android.content.ContentValues.TAG;

//Static utility class to check Location GPS settings.
//We could not extend LiveData class and use it in ViewModel
//because passing activity context to ViewModel and LiveDate classes would be very very bad idea.
public final class GpsUtils {
    public static final int GPS_REQUEST = 101;
    public static Boolean isGPSEnabled = false;

    // Private constructor to prevent instantiation
    private GpsUtils() {
        throw new UnsupportedOperationException();
    }

    public static void checkIfGPSEnabled(Context context) {
        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        LocationManager locationManager =
                (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationSettingsRequest locationSettingsRequest =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(LocationLiveData.locationRequest)
                        .build();

        // Check if enabled
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isGPSEnabled = true;
        } else {
            settingsClient.checkLocationSettings(locationSettingsRequest)
                    .addOnSuccessListener((Activity) context,
                            locationSettingsResponse -> {
                                //  GPS is already enable
                                isGPSEnabled = true;
                            })
                   .addOnFailureListener((Activity) context, e -> {
                       //Not enabled
                       isGPSEnabled = false;
                       if ( e instanceof ResolvableApiException) {
                           try {
                               // Show the dialog by calling startResolutionForResult(),
                               // and check the result
                               ((ResolvableApiException) e)
                                       .startResolutionForResult((Activity) context, GPS_REQUEST);
                           } catch (IntentSender.SendIntentException sendIntentException) {
                               Log.i(TAG, "PendingIntent unable to execute request.");
                           }
                       }
                   });
        }
    }
}
