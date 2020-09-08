package test.project.firestore_sample.models;

/*Created by ioBirdOussama on 06/12/2017.*/

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import test.project.firestore_sample.controls.Constants;

@SuppressWarnings("SameParameterValue")
public class Extra implements Serializable {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private double price;
    @Expose
    private Integer row = (int)Constants.ZERO;
    @Expose
    private Integer max;
    @Expose
    private Integer quantity;
    @Expose
    @SerializedName("is_sub_extra")
    private Boolean isSubExtra;

    public Extra() {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Exclude
    public void increaseQuantity() {
        quantity++;
    }

    @Exclude
    public void decreaseQuantity() {
        quantity--;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Extra && id != null && ((Extra) obj).getId() != null && id.equals(((Extra) obj).getId());
    }

    @com.google.firebase.firestore.PropertyName("is_sub_extra")
    @PropertyName("is_sub_extra")
    public Boolean isSubExtra() {
        return isSubExtra;
    }

    @com.google.firebase.firestore.PropertyName("is_sub_extra")
    @PropertyName("is_sub_extra")
    public void setSubExtra(Boolean subExtra) {
        isSubExtra = subExtra;
    }
}
