package com.example.nerdeyesem.utils;

import android.content.Context;
import android.widget.Toast;

import com.example.nerdeyesem.model.LocationModel;

// This static class is for to check if new location request needed based on distance travelled.
public class NewLocationChecker {

    // Private constructor to prevent instantiation
    private NewLocationChecker() {
        throw new UnsupportedOperationException();
    }

    public static boolean isNewLocationNeeded (Context context,
                                               LocationModel previousLocation, LocationModel currentLocation) {
        // Check if new location needed when UI recreated.
        // If distance travelled is more then 1km, then request new data from API.
        // Distance calculation function is belong to  RestaurantModel class.
        boolean needNewLocation = false;
        if (previousLocation != null) {
            int distance = previousLocation.getDistance(currentLocation);
            //if distance travelled is more then 1 km.
            if (distance > 1000) {
                needNewLocation = true;
            }
            if(distance > 100) {
                String distanceMessage = "Distance travelled: " + distance
                        + " meter";
                Toast.makeText(context, distanceMessage,
                        Toast.LENGTH_LONG).show();
            }

        } else {
            needNewLocation = true;
        }
        return needNewLocation;
    }
}
