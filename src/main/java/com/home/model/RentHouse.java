package com.home.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Collection;

@Document(collection = "hourse")
public class RentHouse extends BaseHourse {

    @Id
    private String id;

    private String rentPrice;

    private String rentMethod;

    @Field("images")
    private Collection<String> images = new ArrayList<>();

    public Collection<String> getImages() {
        return images;
    }

    public void setImages(Collection<String> images) {
        this.images = images;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getRentMethod() {
        return rentMethod;
    }

    public void setRentMethod(String rentMethod) {
        this.rentMethod = rentMethod;
    }
}
