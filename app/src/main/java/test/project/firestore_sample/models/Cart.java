package test.project.firestore_sample.models;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import test.project.firestore_sample.controls.Constants;
import test.project.firestore_sample.controls.Utils;


public class Cart implements Serializable {

    private int productCount;
    private double totalPrice;
    private Map<String, CartProductItem> items;
    private String storeId;
    private String userNote;
    //
    private List<CartProductItem> itemsList;

    public Cart() {
        totalPrice = 0;
        productCount = 0;
    }

    public Cart(Cart cart) {
        this.productCount = cart.productCount;
        this.totalPrice = cart.totalPrice;
        this.storeId = cart.storeId;
        this.userNote = cart.userNote;
        this.items = new HashMap<>();
        this.items.putAll(cart.items);
        this.itemsList = new ArrayList<>();
        this.itemsList.addAll(cart.itemsList);
    }

    public void updateCart(Cart cart) {
        productCount = cart.productCount;
        totalPrice = cart.totalPrice;
        if (items != null) {
            items.clear();
            items.putAll(cart.items);
        } else {
            items = cart.items;
        }

        itemsList = null;
        fillList();
    }

    public void resetValues() {
        productCount = 0;
        totalPrice = 0;
        try {
            items.clear();
        } catch (Exception ee) {
            items = null;
        }
        try {
            itemsList.clear();
        } catch (Exception e) {
            itemsList = null;
        }
        storeId = null;
    }

    public void fillList() {
        if (itemsList == null) {
            itemsList = new ArrayList<>();
        } else {
            itemsList.clear();
        }
        if (items != null) {
            for (String key : items.keySet()) {
                CartProductItem item = items.get(key);
                item.setId(key);
                itemsList.add(item);
            }
        }
        if (itemsList == null || itemsList.size() == 0) {
            productCount = 0;
        }
    }

    @PropertyName("products_count")
    public int getProductCount() {
        return productCount;
    }

    @PropertyName("products_count")
    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    @PropertyName("total_price")
    public double getTotalPrice() {
        return totalPrice;
    }

    @PropertyName("total_price")
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @PropertyName("items")
    public Map<String, CartProductItem> getItems() {
        return items;
    }

    @SuppressWarnings("unused")
    @PropertyName("items")
    public void setItems(Map<String, CartProductItem> items) {
        this.items = items;
    }

    @Exclude
    public List<CartProductItem> getItemsList() {
        return itemsList;
    }

    @Exclude
    public String getStoreId() {
        return storeId;
    }

    @Exclude
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @Exclude
    public String getUserNote() {
        return userNote;
    }

    @Exclude
    public void setUserNote(String userNote) {
        this.userNote = userNote;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Cart cart = (Cart) o;
        return productCount == cart.productCount && Double.compare(cart.totalPrice, totalPrice) == 0 && Objects.equals(itemsList, cart.itemsList);
    }

    @NonNull
    @Override
    public String toString() {
        return "Cart{" +
                "\n products_count: " + productCount +
                "\n total_price: " + totalPrice +
                "\n itemsList: " + items.toString() +
                '}';
    }

    public void putCartProductItem(CartProductItem item) {
        if (items ==null) {
            items = new HashMap<>();
        }
        items.put(item.getId(), item);
        productsCountAndTotalPrice();
    }

    public void removeCartProductItem(CartProductItem item) {
        if (items != null && items.containsKey(item.getId())) {
            if (item.getQuantity() < 1) {
                item.setQuantity(1);
            }
            productCount-= item.getQuantity();
            totalPrice-= item.getTotalItemPrice();
            items.remove(item.getId());
        }
    }

    private void productsCountAndTotalPrice() {
        double totalPrice = 0;
        int count = 0;
        for (CartProductItem item : items.values()) {
            totalPrice += item.getTotalItemPrice();
            count += item.getQuantity();
        }
        this.totalPrice = totalPrice;
        this.productCount = count;
    }

    public boolean containsRequiredData() {
        return productCount > Constants.ZERO
                && totalPrice >= Constants.ZERO
                && !TextUtils.isEmpty(storeId)
                && !Utils.isMapEmpty(items);
    }
}
