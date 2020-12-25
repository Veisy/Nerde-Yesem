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
}
