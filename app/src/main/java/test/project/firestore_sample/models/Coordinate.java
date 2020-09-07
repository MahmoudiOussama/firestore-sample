package test.project.firestore_sample.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

// Created by Zied on 19/01/2017.

public class Coordinate implements Serializable {
    private double latitude=0;
    private double longitude=0;

    @SuppressWarnings("unused")
    public Coordinate() {}

    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinate(String coordinates) {
        try {
            String[] coordinatesArray = coordinates.split(",");
            latitude = Double.parseDouble(coordinatesArray[0]);
            longitude = Double.parseDouble(coordinatesArray[1]);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean locationAvailable() {
        return latitude !=0 && longitude !=0 ;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }
}
