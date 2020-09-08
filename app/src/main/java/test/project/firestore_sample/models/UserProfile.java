package test.project.firestore_sample.models;

import android.text.TextUtils;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

// Created by ioBirdOussama on 07/04/2017.

@SuppressWarnings("unused")
public class UserProfile implements Serializable {

    private String uid;
    private String email;
    private String name;
    private String phone;
    private Image  photo;
    private Date birthDate;
    private String gender;
    private String address;
    private String lastLocation;
    private String ordersShareScope;
    private HashMap<String, Integer> ordersCount;
    private String lastPaymentType;
    //not structure
    private Coordinate userDeliveryLocation;

    public UserProfile(){

    }

    public UserProfile(UserProfile user){
        if (user != null) {
            name = (TextUtils.isEmpty(user.getName())) ? null : user.getName();
            phone = (TextUtils.isEmpty(user.getPhone())) ? null : user.getPhone();
            uid = (TextUtils.isEmpty(user.getUid())) ? null : user.getUid();
            email = user.getEmail();
            photo = user.getPhoto();
            birthDate = user.getBirthDate();
            gender = user.getGender();
            address = user.getAddress();
            ordersCount = user.getOrdersCount();
            ordersShareScope = user.getOrdersShareScope();
            lastLocation = user.getLastLocation();
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }
    @com.google.firebase.database.PropertyName("birthdate")
    @PropertyName("birthdate")
    public Date getBirthDate() {
        return birthDate;
    }
    @com.google.firebase.database.PropertyName("birthdate")
    @PropertyName("birthdate")
    public void setBirthDate(Object birthDate) {
        if (birthDate instanceof Long) {
            this.birthDate = new Date((Long)birthDate);
        } else if (birthDate instanceof Timestamp) {
            this.birthDate = ((Timestamp)birthDate).toDate();
        } else {
            this.birthDate = null;
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    @com.google.firebase.database.PropertyName("last_location")
    @PropertyName("last_location")
    public String getLastLocation() {
        return lastLocation;
    }
    @com.google.firebase.database.PropertyName("last_location")
    @PropertyName("last_location")
    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }
    @com.google.firebase.database.PropertyName("orders_count")
    @PropertyName("orders_count")
    public HashMap<String, Integer> getOrdersCount() {
        return ordersCount;
    }
    @com.google.firebase.database.PropertyName("orders_count")
    @PropertyName("orders_count")
    public void setOrdersCount(HashMap<String, Integer> ordersCount) {
        this.ordersCount = ordersCount;
    }
    @com.google.firebase.database.PropertyName("orders_share_scope")
    @PropertyName("orders_share_scope")
    public String getOrdersShareScope() {
        return ordersShareScope;
    }
    @com.google.firebase.database.PropertyName("orders_share_scope")
    @PropertyName("orders_share_scope")
    public void setOrdersShareScope(String ordersShareScope) {
        this.ordersShareScope = ordersShareScope;
    }
    @com.google.firebase.database.PropertyName("last_payment_type")
    @PropertyName("last_payment_type")
    public String getLastPaymentType() {
        return lastPaymentType;
    }
    @com.google.firebase.database.PropertyName("last_payment_type")
    @PropertyName("last_payment_type")
    public void setLastPaymentType(String lastPaymentType) {
        this.lastPaymentType = lastPaymentType;
    }

    @Exclude
    public Coordinate getUserDeliveryLocation() {
        return userDeliveryLocation;
    }
    @Exclude
    public void setUserDeliveryLocation(Coordinate userDeliveryLocation) {
        this.userDeliveryLocation = userDeliveryLocation;
    }
}
