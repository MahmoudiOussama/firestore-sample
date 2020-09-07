package test.project.firestore_sample.models;

/* Created by ioBirdOussama on 01/02/2018. */

import com.google.firebase.database.PropertyName;

public class DeliveryAgent {

    private String name;
    private String phone;
    private Image  image;
    private String employerId;

    public DeliveryAgent() {
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @PropertyName("employer_id")
    public String getEmployerId() {
        return employerId;
    }

    @PropertyName("employer_id")
    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }
}
