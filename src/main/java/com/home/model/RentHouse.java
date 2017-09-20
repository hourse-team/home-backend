package com.home.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Collection;

@Document(collection = "hourse")
public class RentHouse extends BaseHourse {

    private String rentPrice;

    private String rentMethod;

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getRentMethod() {
        return rentMethod;
    }

    public void setRentMethod(String rentMethod) {
        this.rentMethod = rentMethod;
    }
}
