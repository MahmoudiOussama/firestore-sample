package test.project.firestore_sample.models;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class FireStoreOrder {

    private Date createdAT;
    private Date updatedAT;
    private String Status;
    private String fieldA;
    private String fieldB;

    public FireStoreOrder() {}
    @PropertyName("created_at")
    public Date getCreatedAT() {
        return createdAT;
    }
    @PropertyName("created_at")
    public void setCreatedAT(Date createdAT) {
        this.createdAT = createdAT;
    }
    @PropertyName("updated_at")
    public Date getUpdatedAT() {
        return updatedAT;
    }
    @PropertyName("updated_at")
    public void setUpdatedAT(Date updatedAT) {
        this.updatedAT = updatedAT;
    }
    @PropertyName("status")
    public String getStatus() {
        return Status;
    }
    @PropertyName("status")
    public void setStatus(String status) {
        Status = status;
    }
    @PropertyName("field_a")
    public String getFieldA() {
        return fieldA;
    }
    @PropertyName("field_a")
    public void setFieldA(String fieldA) {
        this.fieldA = fieldA;
    }
    @PropertyName("field_b")
    public String getFieldB() {
        return fieldB;
    }
    @PropertyName("field_b")
    public void setFieldB(String fieldB) {
        this.fieldB = fieldB;
    }
}
