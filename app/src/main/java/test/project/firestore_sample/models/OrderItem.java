package test.project.firestore_sample.models;

import android.text.TextUtils;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

import test.project.firestore_sample.controls.Constants;
import test.project.firestore_sample.controls.Utils;

@SuppressWarnings("unused")
public class OrderItem implements Serializable {
    @Expose
    @SerializedName("product_id")
    private String productId;
    @Expose
    @SerializedName("discount_price")
    private double discountPrice = Constants.ONE;
    @Expose
    @SerializedName("discount")
    private Integer discount;
    @Expose
    @SerializedName("main_image")
    private Image mainImage;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("price")
    private double price = Constants.ZERO;
    @Expose
    @SerializedName("quantity")
    private Integer quantity = (int)Constants.ONE;
    @Expose
    @SerializedName("category_id")
    private String categoryId;
    @Expose
    @SerializedName("sub_category_id")
    private String subCategoryId;
    @Expose
    @SerializedName("extras")
    private Map<String, Extra> extras;
    @Expose
    @SerializedName("options")
    private Map<String, Option> options;
    @Expose
    @SerializedName("extras_title")
    private String extrasTitle;
    @Expose
    @SerializedName("max_extras")
    private Integer maxExtras;
    @Expose
    @SerializedName("ingredients")
    private Map<String, Ingredient> ingredients;
    @Expose
    @SerializedName("ingredients_title")
    private String ingredientsTitle;

    //local attributes
    private double itemPrice;
    private String subExtrasTitle;

    public OrderItem() {}

    public OrderItem(CartProductItem item) {
        //cart item
        extras = item.getExtras();
        options = item.getOptions();
        quantity = item.getQuantity();
        ingredients = item.getIngredients();
        //variant
        productId = item.getVariant().getProductId();
        price = item.getVariant().getPrice();
        discountPrice = item.getVariant().getDiscountPrice();
        discount = item.getVariant().getDiscount();
        name = item.getVariant().getName();
        mainImage = item.getVariant().getMainImage();
        categoryId = item.getVariant().getCategory();
        subCategoryId = item.getVariant().getSubCategory();
        extrasTitle = item.getVariant().getExtrasTitle();
        maxExtras = item.getVariant().getMaxExtras();
        ingredientsTitle = item.getVariant().getIngredientsTitle();
        /*
        * below code needed if we want to save the final price after discount for each product

        //calculate and save the price and discount price for this order item
        if (selectedStore.getInformation() != null && selectedStore.getInformation().getGlobalDiscount() > 0) {
            price = item.getItemPrice();
            discountPrice = (item.getItemPrice() * selectedStore.getInformation().getGlobalDiscount() ) / Constants.ONE_HUNDRED;
        } else {
            price = item.getItemPrice();
            //calculate discount for each product
            double tempItemPrice = item.getItemPrice();
            for (Extra extra : extras.values()) {
                if (!extra.isSubExtra()) {
                    tempItemPrice -= extra.getPrice();
                }
            }
            discountPrice = (tempItemPrice * selectedStore.getInformation().getGlobalDiscount() ) / Constants.ONE_HUNDRED;
        }
        */
    }

    @com.google.firebase.firestore.PropertyName("product_id")
    @PropertyName("product_id")
    public String getProductId() {
        return productId;
    }

    @com.google.firebase.firestore.PropertyName("product_id")
    @PropertyName("product_id")
    public void setProductId(String productId) {
        this.productId = productId;
    }

    @com.google.firebase.firestore.PropertyName("discount_price")
    @PropertyName("discount_price")
    public double getDiscountPrice() {
        return discountPrice;
    }

    @com.google.firebase.firestore.PropertyName("discount_price")
    @PropertyName("discount_price")
    public void setDiscountPrice(double discountPrice) {
        this.discountPrice = discountPrice;
    }

    @com.google.firebase.firestore.PropertyName("main_image")
    @PropertyName("main_image")
    public Image getMainImage() {
        return mainImage;
    }

    @com.google.firebase.firestore.PropertyName("main_image")
    @PropertyName("main_image")
    public void setMainImage(Image mainImage) {
        this.mainImage = mainImage;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @com.google.firebase.firestore.PropertyName("category_id")
    @PropertyName("category_id")
    public String getCategoryId() {
        return categoryId;
    }

    @com.google.firebase.firestore.PropertyName("category_id")
    @PropertyName("category_id")
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @com.google.firebase.firestore.PropertyName("sub_category_id")
    @PropertyName("sub_category_id")
    public String getSubCategoryId() {
        return subCategoryId;
    }

    @com.google.firebase.firestore.PropertyName("sub_category_id")
    @PropertyName("sub_category_id")
    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public Map<String, Extra> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Extra> extras) {
        this.extras = extras;
    }

    @Exclude
    @com.google.firebase.firestore.PropertyName("sub_extras_title")
    @PropertyName("sub_extras_title")
    public String getSubExtrasTitle() {
        return subExtrasTitle;
    }

    @com.google.firebase.firestore.PropertyName("extras_title")
    @PropertyName("extras_title")
    public String getExtrasTitle() {
        return extrasTitle;
    }

    @com.google.firebase.firestore.PropertyName("extras_title")
    @PropertyName("extras_title")
    public void setExtrasTitle(String extrasTitle) {
        this.extrasTitle = extrasTitle;
    }

    @com.google.firebase.firestore.PropertyName("max_extras")
    @PropertyName("max_extras")
    public Integer getMaxExtras() {
        return maxExtras;
    }

    @com.google.firebase.firestore.PropertyName("max_extras")
    @PropertyName("max_extras")
    public void setMaxExtras(Integer maxExtras) {
        this.maxExtras = maxExtras;
    }

    public Map<String, Option> getOptions() {
        return options;
    }

    public void setOptions(Map<String, Option> options) {
        this.options = options;
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

    @com.google.firebase.firestore.PropertyName("ingredients_title")
    @PropertyName("ingredients_title")
    public String getIngredientsTitle() {
        return ingredientsTitle;
    }

    @com.google.firebase.firestore.PropertyName("ingredients_title")
    @PropertyName("ingredients_title")
    public void setIngredientsTitle(String ingredientsTitle) {
        this.ingredientsTitle = ingredientsTitle;
    }

    @Exclude
    public double getItemPrice() {
        return itemPrice;
    }

    public void calculateItemPrice() {
        double finalPrice = discountPrice;
        if (!Utils.isMapEmpty(options)) {
            for (Option option : options.values()) {
                if (option.getElement() != null) {
                    finalPrice += option.getElement().getPrice();
                } else if (option.getElements() != null) {
                    for (Element element : option.getElements().values()) {
                        //check element quantity not null
                        finalPrice += element.getQuantity() != null ? (element.getPrice() * element.getQuantity()) : element.getPrice();

                        if (!TextUtils.isEmpty(element.getExtrasTitle())) {
                            subExtrasTitle = element.getExtrasTitle();
                        }
                    }
                }
            }
        }
        if (!Utils.isMapEmpty(extras)) {
            for (Extra extra : extras.values()) {
                finalPrice += extra.getQuantity() > Constants.ZERO ? (extra.getPrice() * extra.getQuantity()) : extra.getPrice();
            }
        }

        this.itemPrice = finalPrice;
    }
}
