package test.project.firestore_sample.models;

import com.google.firebase.database.Exclude;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

import test.project.firestore_sample.controls.Constants;

public class Ingredient implements Serializable, Comparable<Ingredient> {
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private int row;

    public Ingredient() {
    }

    @Exclude
    public String getId() {
        return id;
    }
    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(Ingredient o) {
        int result = Integer.compare(row, o.row);

        if (result == Constants.ZERO) {
            return name.compareTo(o.name);
        } else {
            return result;
        }
    }
}
