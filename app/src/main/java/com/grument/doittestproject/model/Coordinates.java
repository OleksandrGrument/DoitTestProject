package com.grument.doittestproject.model;


public class Coordinates {

    public Coordinates(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private float latitude;

    private float longitude;

    public String getStringLatitude() {
        return String.valueOf(latitude);
    }

    public String getStringLongitude() {
        return String.valueOf(longitude);
    }

}
