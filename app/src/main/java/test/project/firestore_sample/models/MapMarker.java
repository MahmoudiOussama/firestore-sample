package test.project.firestore_sample.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

// Created by ioBirdOussama on 04/10/2017.

public class MapMarker implements ClusterItem {
    private final LatLng mPosition;
    private String tag;
    private final String storeId;
    private String restaurantName;

    public MapMarker(LatLng position, String storeId) {
        mPosition = position;
        this.storeId = storeId;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}