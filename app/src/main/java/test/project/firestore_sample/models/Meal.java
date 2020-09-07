package test.project.firestore_sample.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

// Created by ioBirdOussama on 10/04/2017.

@SuppressWarnings("unused")
public class Meal implements Serializable, Comparable<Meal> {

    private boolean available;
    private String category_id;
    private String description;
    private double discount_price=0;
    private String extrasTitle;
    private Integer maxExtras;
    private Map<String, Extra> extras;
    private String id;
    private boolean isPriceHidden;
    private Image  mainImage;
    private String mainOptionId;
    private String name;
    private Map<String, Option> options;
    private double price=0;
    private int    quantity=1;
    private int    row;
    private String sub_category_id;
    private long   updated_at;
    private Map<String, Image> images;
    private Integer discount;
    private Map<String, Ingredient> ingredients;
    private String ingredientsTitle;

    //local attributes
    private double discountValue;

    public Meal() {
        images = new HashMap<>();
    }

    public Meal(Meal meal) {
        available = meal.isAvailable();
        category_id = meal.getCategory_id();
        description = meal.getDescription();
        discount_price = meal.getDiscount_price();
        extrasTitle = meal.getExtrasTitle();
        maxExtras = meal.getMaxExtras();
        extras = meal.getExtras();
        options = meal.getOptions();
        id = meal.getId();
        isPriceHidden = meal.isPriceHidden();
        mainImage = meal.getMainImage();
        mainOptionId = meal.getMainOptionId();
        name = meal.getName();
        price = meal.getPrice();
        quantity = 1;
        sub_category_id = meal.getSub_category_id();
        updated_at = meal.getUpdated_at();
        row = meal.getRow();
        images = meal.getImages();
        discount = meal.getDiscount();
        ingredients = meal.getIngredients();
        ingredientsTitle = meal.getIngredientsTitle();
    }

    public Meal(CartProductItem item) {
        price = item.getVariant().getPrice();
        discount_price = item.getVariant().getDiscountPrice();
        name = item.getVariant().getName();
        mainImage = item.getVariant().getMainImage();
        quantity = item.getQuantity();
        category_id = item.getVariant().getCategory();
        sub_category_id = item.getVariant().getSubCategory();
        discount = item.getVariant().getDiscount();
    }

    public void insertMeal(Meal meal) {
        available = meal.isAvailable();
        category_id = meal.getCategory_id();
        description = meal.getDescription();
        discount_price = meal.getDiscount_price();
        extrasTitle = meal.getExtrasTitle();
        maxExtras = meal.getMaxExtras();
        extras = meal.getExtras();
        options = meal.getOptions();
        id = meal.getId();
        isPriceHidden = meal.isPriceHidden();
        mainImage = meal.getMainImage();
        mainOptionId = meal.getMainOptionId();
        name = meal.getName();
        price = meal.getPrice();
        quantity = 1;
        sub_category_id = meal.getSub_category_id();
        updated_at = meal.getUpdated_at();
        discount = meal.getDiscount();
        ingredients = meal.getIngredients();
        ingredientsTitle = meal.getIngredientsTitle();
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(double discount_price) {
        this.discount_price = discount_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("is_price_hidden")
    public boolean isPriceHidden() {
        return isPriceHidden;
    }

    @PropertyName("is_price_hidden")
    public void setPriceHidden(boolean priceHidden) {
        isPriceHidden = priceHidden;
    }

    @PropertyName("main_image")
    public Image getMainImage() {
        return mainImage;
    }

    @PropertyName("main_image")
    public void setMainImage(Image mainImage) {
        this.mainImage = mainImage;
    }

    @PropertyName("main_option_id")
    public String getMainOptionId() {
        return mainOptionId;
    }

    @PropertyName("main_option_id")
    public void setMainOptionId(String mainOptionId) {
        this.mainOptionId = mainOptionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Option> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Option> options) {
        this.options = options;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public Map<String, Image> getImages() {
        return images;
    }

    public void setImages(Map<String, Image> images) {
        this.images = images;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @PropertyName("max_extras")
    public Integer getMaxExtras() {
        return maxExtras;
    }

    @PropertyName("max_extras")
    public void setMaxExtras(Integer maxExtras) {
        this.maxExtras = maxExtras;
    }

    public Map<String, Extra> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Extra> extras) {
        this.extras = extras;
    }

    @PropertyName("extras_title")
    public String getExtrasTitle() {
        return extrasTitle;
    }

    @PropertyName("extras_title")
    public void setExtrasTitle(String extrasTitle) {
        this.extrasTitle = extrasTitle;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Map<String, Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @PropertyName("ingredients_title")
    public String getIngredientsTitle() {
        return ingredientsTitle;
    }

    @PropertyName("ingredients_title")
    public void setIngredientsTitle(String ingredientsTitle) {
        this.ingredientsTitle = ingredientsTitle;
    }

    @Exclude
    public double getDiscountValue() {
        return discountValue;
    }

    @Exclude
    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    @Override
    public int compareTo(@NonNull Meal myMeal) {
        //return name.compareTo(myMeal.getName());
        //means the two values are equals
        return Integer.compare(row, myMeal.getRow());
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Meal && id.equals(((Meal) obj).getId());
    }
}
