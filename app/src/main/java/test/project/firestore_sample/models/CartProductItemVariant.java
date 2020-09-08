package test.project.firestore_sample.models;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class CartProductItemVariant implements Serializable {

    private String  productId;
    private String  name;
    private String  category;
    private String  subCategory;
    private double  price;
    private double  discountPrice;
    private Image   mainImage;
    private long    updatedAt;
    private Integer discount;
    private String  extrasTitle;
    private Integer maxExtras;
    private String  ingredientsTitle;

    public CartProductItemVariant() {
    }

    public CartProductItemVariant(Meal meal) {
        this.productId = meal.getId();
        this.name = meal.getName();
        this.category = meal.getCategory_id();
        this.subCategory = meal.getSub_category_id();
        this.price = meal.getPrice();
        this.discountPrice = meal.getDiscount_price();
        this.mainImage = meal.getMainImage();
        this.updatedAt = meal.getUpdated_at();
        this.discount = meal.getDiscount();
        this.extrasTitle = meal.getExtrasTitle();
        this.maxExtras = meal.getMaxExtras();
        this.ingredientsTitle = meal.getIngredientsTitle();
        //Log.d("discount", "meal discount : "+meal.getDiscount() + " / discount : "+discount);
    }
    @com.google.firebase.firestore.PropertyName("product_id")
    @PropertyName("product_id")
    public String getProductId() {
        return productId;
    }
    @com.google.firebase.firestore.PropertyName("product_id")
    @PropertyName("product_id")
    public void setProductId(String id) {
        this.productId = id;
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
    @com.google.firebase.firestore.PropertyName("category_id")
    @PropertyName("category_id")
    public String getCategory() {
        return category;
    }
    @com.google.firebase.firestore.PropertyName("category_id")
    @PropertyName("category_id")
    public void setCategory(String category) {
        this.category = category;
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
    @com.google.firebase.firestore.PropertyName("sub_category_id")
    @PropertyName("sub_category_id")
    public String getSubCategory() {
        return subCategory;
    }
    @com.google.firebase.firestore.PropertyName("sub_category_id")
    @PropertyName("sub_category_id")
    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }
    @com.google.firebase.firestore.PropertyName("updated_at")
    @PropertyName("updated_at")
    public long getUpdatedAt() {
        return updatedAt;
    }
    @com.google.firebase.firestore.PropertyName("updated_at")
    @PropertyName("updated_at")
    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartProductItemVariant that = (CartProductItemVariant) o;
        return !productId.equals(that.productId) && Double.compare(that.price, price) == 0 && !category.equals(that.category) && Double.compare(that.discountPrice, discountPrice) == 0 && (name != null ? name.equals(that.name) : that.name == null && (mainImage != null ? mainImage.equals(that.mainImage) : that.mainImage == null));
    }

    @Override
    public String toString() {
        return "CartProductItemVariant{" +
                "\n productId: "+productId+
                "\n name: "+name+
                "\n category_id: "+category+
                "\n sub_category_id: "+subCategory+
                "\n price: "+price+
                "\n discount_price: "+discountPrice+
                "\n discount: "+discount+
                "\n main_image: "+mainImage+
                "\n updated_at: "+updatedAt+
                '}';
    }
}
