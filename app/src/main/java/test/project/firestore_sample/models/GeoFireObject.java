package test.project.firestore_sample.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;
import java.util.List;

// Created by ioBirdOussama on 28/09/2017.

@SuppressWarnings("unused")
public class GeoFireObject {

    private String id;
    private List<Object> coordinates = new ArrayList<>();

    public GeoFireObject() {
    }

    @PropertyName("g")
    public String getId() {
        return id;
    }
    @PropertyName("g")
    public void setId(String id) {
        this.id = id;
    }
    @PropertyName("l")
    public List<Object> getCoordinates() {
        return coordinates;
    }
    @PropertyName("l")
    public void setCoordinates(List<Object> list) {
        coordinates = list;
    }

    @Exclude
    public double getLatitude() {
        return (double)coordinates.get(0);
    }
    @Exclude
    public double getLongitude() {
        return (double)coordinates.get(1);
    }
}