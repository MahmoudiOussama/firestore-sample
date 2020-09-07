package test.project.firestore_sample.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

//Created by Zied on 18/08/2017.

@SuppressWarnings("unused")
public class Image implements Serializable{
    @Expose
    private String url;
    @Expose
    private String ref;

    public Image(){}

    public Image (String ref, String url) {
        this.ref = ref;
        this.url = url;
    }

    public Image(char empty){
        url = Character.toString(empty);
        ref = Character.toString(empty);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }
}
