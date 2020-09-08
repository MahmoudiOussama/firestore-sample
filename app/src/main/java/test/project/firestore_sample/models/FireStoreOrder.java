package test.project.firestore_sample.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//Created by ioBirdOussama on 23/08/2017.

@SuppressWarnings("unused")
public class FireStoreOrder implements Serializable{

    //under users_orders
    @Expose
    @SerializedName("created_at")
    private Date createdAt;
    @Expose
    @SerializedName("updated_at")
    private Date updatedAt;
    @Expose
    @SerializedName("store_id")
    private String storeId;
    @Expose
    @SerializedName("store_name")
    private String storeName;
    @Expose
    @SerializedName("total_price")
    private Double totalPrice;
    //under orders
    @Expose
    @SerializedName("total_discount")
    private Double totalDiscount;
    @Expose
    @SerializedName("items")
    private Map<String, OrderItem> items;
    @Expose
    @SerializedName("order_type")
    private String orderType;
    @Expose
    @SerializedName("status")
    private String status;
    @Expose
    @SerializedName("user_address")
    private String userAddress;
    @Expose
    @SerializedName("user_coordinates")
    private String userCoordinates;
    @Expose
    @SerializedName("user_name")
    private String userName;
    @Expose
    @SerializedName("user_phone")
    private String userPhone;
    @Expose
    @SerializedName("user_uid")
    private String userUid;
    @Expose
    @SerializedName("note")
    private String note;
    @Expose
    @SerializedName("delivery_partner_id")
    private String deliveryPartnerId;
    @Expose
    @SerializedName("delivery_partner_name")
    private String deliveryPartnerName;
    @Expose
    @SerializedName("delivery_agent_id")
    private String deliveryAgentId;
    @Expose
    @SerializedName("delivery_assignment_status")
    private String deliveryAssignmentStatus;
    @Expose
    @SerializedName("order_id")
    private String orderId;

    private List<OrderItem> mMealsList;
    private int itemsCount;

    @Expose
    @SerializedName("currency")
    private String currency;
    @Expose
    @SerializedName("paid_with_loyalty")
    private Double paidWithLoyalty;
    @Expose
    @SerializedName("delivery_fee")
    private Double deliveryFee;
    @Expose
    @SerializedName("table_number")
    private Integer tableNumber;
    @Expose
    @SerializedName("order_number")
    private Integer orderNumber;
    @Expose
    @SerializedName("distance")
    private Double distance;
    @Expose
    @SerializedName("app_name")
    private String appName;
    @Expose
    @SerializedName("app_version")
    private String appVersion;
    @Expose
    @SerializedName("platform")
    private String platform;
    @Expose
    @SerializedName("in_progress_at")
    private Date inProgressAt;
    @Expose
    @SerializedName("validated_at")
    private Date validatedAt;
    @Expose
    @SerializedName("picked_at")
    private Date pickedAt;
    @Expose
    @SerializedName("delivered_at")
    private Date deliveredAt;
    @Expose
    @SerializedName("canceled_at")
    private Date canceledAt;
    @Expose
    @SerializedName("returned_at")
    private Date returnedAt;
    @Expose
    @SerializedName("audience")
    private List<String> audience;
    @Expose
    @SerializedName("comments_count")
    private Integer commentsCount;
    @Expose
    @SerializedName("hearts_count")
    private Integer heartsCount;
    @Expose
    @SerializedName("hearts_ids")
    private List<String> heartsIds;
    @Expose
    @SerializedName("is_public")
    private Boolean isPublic;
    @Expose
    @SerializedName("is_friends")
    private Boolean isFriends;
    @Expose
    @SerializedName("is_private")
    private Boolean isPrivate;
    @Expose
    @SerializedName("is_visible")
    private Boolean isVisible;
    @Expose
    @SerializedName("user_photo")
    private String userPhoto;
    @Expose
    @SerializedName("restaurant_photo")
    private String restaurantPhoto;
    @Expose
    @SerializedName("delivery_photo")
    private String deliveryPhoto;
    @Expose
    @SerializedName("paid")
    private Boolean paid;
    @Expose
    @SerializedName("level_one_zone_id")
    private String levelOneZoneID;
    @Expose
    @SerializedName("level_two_zone_id")
    private String levelTwoZoneID;

    private String countryID;
    //to prevent "No setter/field" logs
    private Object validatedBy;
    private Object processedAt;

    public FireStoreOrder() { }

    public FireStoreOrder(String uid) {
        this.orderId = uid;
    }

    /** ....... Getters and Setters Order structure ....... */
    @PropertyName("created_at")
    public Date getCreatedAt() {
        return createdAt;
    }
    @PropertyName("created_at")
    public void setCreatedAt(@SuppressWarnings("SameParameterValue") Date createdAt) {
        this.createdAt = createdAt;
    }
    @PropertyName("updated_at")
    public Date getUpdatedAt() {
        return updatedAt;
    }
    @PropertyName("updated_at")
    public void setUpdatedAt(Date updateAt) {
        this.updatedAt = updateAt;
    }
    @PropertyName("store_id")
    public String getStoreId() {
        return storeId;
    }
    @PropertyName("store_id")
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }
    @PropertyName("store_name")
    public String getStoreName() {
        return storeName;
    }
    @PropertyName("store_name")
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }
    @PropertyName("total_price")
    public Double getTotalPrice() {
        return totalPrice;
    }
    @PropertyName("total_price")
    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Map<String, OrderItem> getItems() {
        return items;
    }
    public void setItems(Map<String, OrderItem> items) {
        this.items = items;
    }

    @PropertyName("order_type")
    public String getOrderType() {
        return orderType;
    }
    @PropertyName("order_type")
    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(@SuppressWarnings("SameParameterValue") String status) {
        this.status = status;
    }

    @PropertyName("user_address")
    public String getUserAddress() {
        return userAddress;
    }
    @PropertyName("user_address")
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }
    @PropertyName("user_coordinates")
    public String getUserCoordinates() {
        return userCoordinates;
    }
    @PropertyName("user_coordinates")
    public void setUserCoordinates(String userCoordinates) {
        this.userCoordinates = userCoordinates;
    }
    @PropertyName("user_name")
    public String getUserName() {
        return userName;
    }
    @PropertyName("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @PropertyName("user_phone")
    public String getUserPhone() {
        return userPhone;
    }
    @PropertyName("user_phone")
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }
    @PropertyName("user_uid")
    public String getUserUid() {
        return userUid;
    }
    @PropertyName("user_uid")
    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @PropertyName("delivery_partner_id")
    public String getDeliveryPartnerId() {
        return deliveryPartnerId;
    }

    @PropertyName("delivery_partner_id")
    public void setDeliveryPartnerId(String deliveryPartnerId) {
        this.deliveryPartnerId = deliveryPartnerId;
    }

    @PropertyName("delivery_partner_name")
    public String getDeliveryPartnerName() {
        return deliveryPartnerName;
    }

    @PropertyName("delivery_partner_name")
    public void setDeliveryPartnerName(String deliveryPartnerName) {
        this.deliveryPartnerName = deliveryPartnerName;
    }

    @PropertyName("delivery_agent_id")
    public String getDeliveryAgentId() {
        return deliveryAgentId;
    }

    @PropertyName("delivery_agent_id")
    public void setDeliveryAgentId(String deliveryAgentId) {
        this.deliveryAgentId = deliveryAgentId;
    }

    @PropertyName("delivery_assignment_status")
    public String getDeliveryAssignmentStatus() {
        return deliveryAssignmentStatus;
    }

    @PropertyName("delivery_assignment_status")
    public void setDeliveryAssignmentStatus(String deliveryAssignmentStatus) {
        this.deliveryAssignmentStatus = deliveryAssignmentStatus;
    }

    @PropertyName("paid_with_loyalty")
    public Double getPaidWithLoyalty() {
        return paidWithLoyalty;
    }

    @PropertyName("paid_with_loyalty")
    public void setPaidWithLoyalty(Double paidWithLoyalty) {
        this.paidWithLoyalty = paidWithLoyalty;
    }

    @PropertyName("delivery_fee")
    public Double getDeliveryFee() {
        return deliveryFee;
    }

    @PropertyName("delivery_fee")
    public void setDeliveryFee(Double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    @PropertyName("app_name")
    public String getAppName() {
        return appName;
    }

    @PropertyName("app_name")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @PropertyName("app_version")
    public String getAppVersion() {
        return appVersion;
    }

    @PropertyName("app_version")
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @PropertyName("total_discount")
    public Double getTotalDiscount() {
        return totalDiscount;
    }

    @PropertyName("total_discount")
    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    @PropertyName("order_number")
    public Integer getOrderNumber() {
        return orderNumber;
    }

    @PropertyName("order_number")
    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    @PropertyName("table_number")
    public Integer getTableNumber() {
        return tableNumber;
    }

    @PropertyName("table_number")
    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @PropertyName("order_id")
    public String getOrderId() {
        return orderId;
    }

    @PropertyName("order_id")
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Exclude
    public int getItemsCount() {
        return itemsCount;
    }

    @Exclude
    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    /** Utility functions*/
    @Override
    public boolean equals(Object obj) {
        if (orderId == null) {
            return false;
        }
        return obj instanceof FireStoreOrder && orderId.equals(((FireStoreOrder) obj).getOrderId());
    }


    public List<String> getAudience() {
        return audience;
    }

    public void setAudience(List<String> audience) {
        this.audience = audience;
    }

    @PropertyName("comments_count")
    public Integer getCommentsCount() {
        return commentsCount;
    }

    @PropertyName("comments_count")
    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    @PropertyName("hearts_count")
    public Integer getHeartsCount() {
        return heartsCount;
    }

    @PropertyName("hearts_count")
    public void setHeartsCount(Integer heartsCount) {
        this.heartsCount = heartsCount;
    }

    @PropertyName("hearts_ids")
    public List<String> getHeartsIds() {
        return heartsIds;
    }

    @PropertyName("hearts_ids")
    public void setHeartsIds(List<String> heartsIds) {
        this.heartsIds = heartsIds;
    }

    @PropertyName("is_public")
    public Boolean isPublic() {
        return isPublic;
    }

    @PropertyName("is_public")
    public void setPublic(Boolean aPublic) {
        isPublic = aPublic;
    }

    @PropertyName("is_friends")
    public Boolean isFriends() {
        return isFriends;
    }

    @PropertyName("is_friends")
    public void setFriends(Boolean friends) {
        isFriends = friends;
    }

    @PropertyName("is_private")
    public Boolean isPrivate() {
        return isPrivate;
    }

    @PropertyName("is_private")
    public void setPrivate(Boolean aPrivate) {
        isPrivate = aPrivate;
    }

    @PropertyName("is_visible")
    public Boolean isVisible() {
        return isVisible;
    }

    @PropertyName("is_visible")
    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    @PropertyName("user_photo")
    public String getUserPhoto() {
        return userPhoto;
    }

    @PropertyName("user_photo")
    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    @PropertyName("restaurant_photo")
    public String getRestaurantPhoto() {
        return restaurantPhoto;
    }

    @PropertyName("restaurant_photo")
    public void setRestaurantPhoto(String restaurantPhoto) {
        this.restaurantPhoto = restaurantPhoto;
    }

    @PropertyName("delivery_photo")
    public String getDeliveryPhoto() {
        return deliveryPhoto;
    }

    @PropertyName("delivery_photo")
    public void setDeliveryPhoto(String deliveryPhoto) {
        this.deliveryPhoto = deliveryPhoto;
    }

    @PropertyName("in_progress_at")
    @com.google.firebase.database.PropertyName("in_progress_at")
    public Date getInProgressAt() {
        return inProgressAt;
    }

    @PropertyName("in_progress_at")
    @com.google.firebase.database.PropertyName("in_progress_at")
    public void setInProgressAt(Date inProgressAt) {
        this.inProgressAt = inProgressAt;
    }

    @PropertyName("validated_at")
    @com.google.firebase.database.PropertyName("validated_at")
    public Date getValidatedAt() {
        return validatedAt;
    }

    @PropertyName("validated_at")
    @com.google.firebase.database.PropertyName("validated_at")
    public void setValidatedAt(Date validatedAt) {
        this.validatedAt = validatedAt;
    }

    @PropertyName("picked_at")
    @com.google.firebase.database.PropertyName("picked_at")
    public Date getPickedAt() {
        return pickedAt;
    }

    @PropertyName("picked_at")
    @com.google.firebase.database.PropertyName("picked_at")
    public void setPickedAt(Date pickedAt) {
        this.pickedAt = pickedAt;
    }

    @PropertyName("delivered_at")
    @com.google.firebase.database.PropertyName("delivered_at")
    public Date getDeliveredAt() {
        return deliveredAt;
    }

    @PropertyName("delivered_at")
    @com.google.firebase.database.PropertyName("delivered_at")
    public void setDeliveredAt(Date deliveredAt) {
        this.deliveredAt = deliveredAt;
    }

    @PropertyName("canceled_at")
    @com.google.firebase.database.PropertyName("canceled_at")
    public Date getCanceledAt() {
        return canceledAt;
    }

    @PropertyName("canceled_at")
    @com.google.firebase.database.PropertyName("canceled_at")
    public void setCanceledAt(Date canceledAt) {
        this.canceledAt = canceledAt;
    }

    @PropertyName("returned_at")
    @com.google.firebase.database.PropertyName("returned_at")
    public Date getReturnedAt() {
        return returnedAt;
    }

    @PropertyName("returned_at")
    @com.google.firebase.database.PropertyName("returned_at")
    public void setReturnedAt(Date returnedAt) {
        this.returnedAt = returnedAt;
    }

    public Boolean isPaid() {
        return paid != null ? ((Boolean) paid).booleanValue() : false;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    @PropertyName("level_one_zone_id")
    public String getLevelOneZoneID() {
        return levelOneZoneID;
    }
    @PropertyName("level_one_zone_id")
    public void setLevelOneZoneID(String levelOneZoneID) {
        this.levelOneZoneID = levelOneZoneID;
    }
    @PropertyName("level_two_zone_id")
    public String getLevelTwoZoneID() {
        return levelTwoZoneID;
    }
    @PropertyName("level_two_zone_id")
    public void setLevelTwoZoneID(String levelTwoZoneID) {
        this.levelTwoZoneID = levelTwoZoneID;
    }

    @PropertyName("country_id")
    @com.google.firebase.database.PropertyName("country_id")
    public String getCountryID() {
        return countryID;
    }
    @PropertyName("country_id")
    @com.google.firebase.database.PropertyName("country_id")
    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    @PropertyName("validated_by")
    public Object getValidatedBy() {
        return validatedBy;
    }
    @PropertyName("validated_by")
    public void setValidatedBy(Object validatedBy) {
        this.validatedBy = validatedBy;
    }
    @PropertyName("processed_at")
    public Object getProcessedAt() {
        return processedAt;
    }
    @PropertyName("processed_at")
    public void setProcessedAt(Object processedAt) {
        this.processedAt = processedAt;
    }

    public List<OrderItem> extractMealsList () {
        if (mMealsList == null) {
            mMealsList = new ArrayList<>();
            for (OrderItem meal : items.values()) {
                mMealsList.add(meal);
                itemsCount += meal.getQuantity();
            }
        }
        return mMealsList;
    }
}
