package test.project.firestore_sample.models;

/*Created by ioBirdOussama on 18/12/2017.*/

import com.google.firebase.database.Exclude;
import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import test.project.firestore_sample.controls.Constants;

@SuppressWarnings("SameParameterValue")
public class Option implements Serializable, Comparable<Option>{
    @Expose
    private String name;
    @Expose
    private Map<String, Element> elements;
    @Expose
    private Element element;
    @Expose
    private Integer row;
    @Expose
    @SerializedName("min_quantity")
    private Integer minQuantity;
    @Expose
    @SerializedName("max_quantity")
    private Integer maxQuantity;
    @Expose
    @SerializedName("option_id")
    private String optionID;

    public Option() {
    }

    public Option (String name) {
        this.name = name;
        elements = new HashMap<>();
    }

    public Option (Option newOption) {
        this.name = newOption.name;
        this.elements = new HashMap<>();
        this.row = newOption.row;
        this.minQuantity = newOption.minQuantity;
        this.maxQuantity = newOption.maxQuantity;
        this.optionID = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Element> getElements() {
        return elements;
    }

    public void setElements(Map<String, Element> elements) {
        this.elements = elements;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }
    @com.google.firebase.database.PropertyName("min_quantity")
    @PropertyName("min_quantity")
    public Integer getMinQuantity() {
        return minQuantity;
    }
    @com.google.firebase.database.PropertyName("min_quantity")
    @PropertyName("min_quantity")
    public void setMinQuantity(Integer minQuantity) {
        this.minQuantity = minQuantity;
    }
    @com.google.firebase.database.PropertyName("max_quantity")
    @PropertyName("max_quantity")
    public Integer getMaxQuantity() {
        return maxQuantity;
    }
    @com.google.firebase.database.PropertyName("max_quantity")
    @PropertyName("max_quantity")
    public void setMaxQuantity(Integer maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    @Exclude
    public String getOptionID() {
        return optionID;
    }

    @Exclude
    public void setOptionID(String optionID) {
        this.optionID = optionID;
    }

    @Exclude
    public void setSelectedElement(String elementID, Element element) {
        Element tempElement = new Element(element);
        tempElement.setExtras(null);
        //elements.clear();
        elements.put(elementID, tempElement);
    }

    @Exclude
    public void removeElementFromSelection(String elementID) {
        if (elements != null) {
            elements.remove(elementID);
        }
    }

    @Override
    public int compareTo(Option option) {
        if (row == null && option.getRow() == null) {
            return name.compareTo(option.getName());
        } else if(row != null && option.getRow() == null) {
            return Constants.MINUS_ONE;
        } else if (row == null && option.getRow() != null) {
            return Constants.ONE;
        } else {
            //means both rows are not null
            return row.compareTo(option.getRow());
        }
    }
}
