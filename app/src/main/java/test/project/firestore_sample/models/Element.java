package test.project.firestore_sample.models;

/*Created by ioBirdOussama on 18/12/2017.*/

import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

import project.iobird.menutiumandroid.controls.Constants;

@SuppressWarnings("SameParameterValue")
public class Element implements Serializable {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private double price;
    @Expose
    @SerializedName("extras_title")
    private String extrasTitle;
    @Expose
    private Map<String, Extra> extras;
    @Expose
    private Integer row = (int)Constants.ZERO;
    @Expose
    private Integer max;
    @Expose
    @SerializedName("min_extras")
    private Integer minExtras;
    @Expose
    @SerializedName("max_extras")
    private Integer maxExtras;
    @Expose
    private Integer quantity;

    //noinspection UnusedParameters
    public Element() { }

    Element(Element tempElement) {
        id = tempElement.id;
        name = tempElement.name;
        price = tempElement.price;
        extrasTitle = tempElement.extrasTitle;
        extras = tempElement.extras;
        row = tempElement.row;
        max = tempElement.max;
        minExtras = tempElement.minExtras;
        maxExtras = tempElement.maxExtras;
        quantity = tempElement.quantity;
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

    @PropertyName("extras_title")
    public String getExtrasTitle() {
        return extrasTitle;
    }

    @PropertyName("extras_title")
    public void setExtrasTitle(String extrasTitle) {
        this.extrasTitle = extrasTitle;
    }

    public Map<String, Extra> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Extra> extras) {
        this.extras = extras;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    @PropertyName("min_extras")
    public Integer getMinExtras() {
        return minExtras;
    }

    @PropertyName("min_extras")
    public void setMinExtras(Integer minExtras) {
        this.minExtras = minExtras;
    }

    @PropertyName("max_extras")
    public Integer getMaxExtras() {
        return maxExtras;
    }

    @PropertyName("max_extras")
    public void setMaxExtras(Integer maxExtras) {
        this.maxExtras = maxExtras;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void increaseQuantity() {
        if (quantity == null) {
            quantity = (int)Constants.ONE;
        } else {
            quantity++;
        }
    }

    public void decreaseQuantity() {
        if (quantity != null) {
            quantity--;
        }
    }
}