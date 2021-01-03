package com.example.nerdeyesem.model;

public class LocationModel {
    private final Double longitude;
    private final Double latitude;

    public LocationModel(Double longitude, Double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    // Calculate distance with another coordinate.
    // Used to calculate distance with previous location in this app, but can be used for any other reason.
    // This calculation estimates earth surface as flat. Good approx for small distances, and fast.
    public int getDistance (LocationModel locationModel) {
        final int radiusOfEarth = 6371000;
        final double oneDegree = Math.PI / 180;
        double a = (this.longitude - locationModel.longitude)*oneDegree;
        double b = Math.cos((this.latitude + locationModel.latitude)/2);
        a = a*b;
        double c = (this.latitude - locationModel.latitude)*oneDegree;

        return (int)Math.round(radiusOfEarth*Math.sqrt(a*a + c*c));
    }
}
