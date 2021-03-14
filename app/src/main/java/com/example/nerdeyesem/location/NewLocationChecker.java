package com.example.nerdeyesem.location;

import android.content.Context;
import android.widget.Toast;

// This static class is for to check if new location request needed based on distance travelled.
public final class NewLocationChecker {

    //Save previous location.
    public static LocationModel previousLocation = null;

    // Threshold of requesting data from the API --> 1 km.
    public static final int DISTANCE_THRESHOLD = 1000;

    // Private constructor to prevent instantiation
    private NewLocationChecker() {
        throw new UnsupportedOperationException();
    }

    public static boolean isNewLocationNeeded (Context context, LocationModel previousLocation,
                                               LocationModel currentLocation) {
        // Check if new location needed when UI recreated.
        // If distance travelled is more then 1km, then request new data from API.
        // Distance calculation function is belong to  RestaurantModel class.
        boolean needNewLocation = false;
        if (previousLocation != null) {
            int distance = previousLocation.getDistance(currentLocation);
            //if distance travelled is more then 1 km.
            if (distance > DISTANCE_THRESHOLD) {
                needNewLocation = true;
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
