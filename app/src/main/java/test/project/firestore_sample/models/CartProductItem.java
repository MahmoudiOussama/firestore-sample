package test.project.firestore_sample.models;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import test.project.firestore_sample.controls.Constants;
import test.project.firestore_sample.controls.Utils;

@SuppressWarnings("SameParameterValue")
public class CartProductItem implements Serializable {

    private String id;

    private Map<String, Extra> extras;
    private Map<String, Option> options;
    private Map<String, Ingredient> ingredients;
    private double itemPrice;
    private int quantity;
    private double totalItemPrice;
    private CartProductItemVariant variant;

    @SuppressWarnings("unused")
    public CartProductItem() {
    }

    public CartProductItem(String productItemId,
                           double itemPrice,
                           int quantity,
                           Map<String, Extra> selectedExtras,
                           Map<String, Extra> selectedSubExtras,
                           Map<String, Option> selectedOptions,
                           Map<String, Ingredient> unselectedIngredients,
                           CartProductItemVariant productVariant) {
        this.id = productItemId;
        this.extras = selectedExtras;
        if (!Utils.isMapEmpty(selectedSubExtras)) {
            if (Utils.isMapEmpty(extras)) {
                extras = selectedSubExtras;
            } else {
                extras.putAll(selectedSubExtras);
                /*for (String subExtraID : selectedSubExtras.keySet()) {
                    extras.put(subExtraID, selectedSubExtras.get(subExtraID));
                }*/
            }
        }
        this.options = selectedOptions;
        this.ingredients = unselectedIngredients;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.totalItemPrice = itemPrice * quantity;
        this.variant = productVariant;
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Extra> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Extra> extras) {
        this.extras = extras;
    }

    public Map<String, Option> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Option> options) {
        this.options = options;
    }

    public Map<String, Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @PropertyName("item_price")
    public double getItemPrice() {
        return itemPrice;
    }

    @PropertyName("item_price")
    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @PropertyName("total_item_price")
    public double getTotalItemPrice() {
        return totalItemPrice;
    }

    @PropertyName("total_item_price")
    public void setTotalItemPrice(double totalItemPrice) {
        this.totalItemPrice = totalItemPrice;
    }

    public CartProductItemVariant getVariant() {
        return variant;
    }

    public void setVariant(CartProductItemVariant variant) {
        this.variant = variant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CartProductItem that = (CartProductItem) o;

        return !(id.equals(that.id) || quantity != that.quantity || Double.compare(that.totalItemPrice, totalItemPrice) != 0) && !(variant != null ? !variant.equals(that.variant) : that.variant != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + quantity;
        temp = Double.doubleToLongBits(totalItemPrice);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (variant != null ? variant.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "\n extras: "+ extras.toString() +
                "\n quantity: " + quantity +
                "\n item_price: "+ itemPrice+
                "\n total_item_price: " + totalItemPrice +
                "\n variant: " + variant +
                '}';
    }

    //Used to calculate extras total price
    public double extrasTotalPrice() {
        double total = 0;
        for (Extra extra : extras.values()) {
            total+= extra.getQuantity() > Constants.ZERO ? (extra.getPrice() * extra.getQuantity()) : extra.getPrice();
        }
        return total;
    }

    //Method used to eliminate elements ids
    // --- For each Option, we need to set one Element, not elements Map ---> DEPRECATED
    // +++ Now every option should contain an elements Map instead of a single Element +++
    @Deprecated
    public void extractOptionsForOrder(Map<String, Option> options) {
        try {
            if (options != null && options.size() > 0) {
                Map<String, Option> newOptionsMap = new HashMap<>();

                for (String optionId : options.keySet()) {
                    Option newOption = new Option();
                    Option mOption = options.get(optionId);

                    newOption.setName(mOption.getName());
                    newOption.setElement(mOption.getElements().entrySet().iterator().next().getValue());
                    newOption.setElements(null);
                    newOptionsMap.put(optionId, newOption);
                }
                this.options = newOptionsMap;
            }
        } catch (Exception e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
    }

    //method used to search for "is_sub_extra" key inside of extras, and clean it
    public void cleanExtrasForOrder() {
        if (!Utils.isMapEmpty(extras)) {
            try {
                for (String extraId : extras.keySet()) {
                    if (Objects.requireNonNull(extras.get(extraId)).isSubExtra() != null) {
                        Objects.requireNonNull(extras.get(extraId)).setSubExtra(null);
                    }
                }
            } catch (Exception ignored){}
        }
    }
}
