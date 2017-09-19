package com.home.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.Collection;

@Document(collection = "hourse")
public class DealHourse extends BaseHourse{

    private String totalPrice;

    private String refPrice;

    private String tradingRight;

    private String equityYear;

    private String buildYear;

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRefPrice() {
        return refPrice;
    }

    public void setRefPrice(String refPrice) {
        this.refPrice = refPrice;
    }

    public String getTradingRight() {
        return tradingRight;
    }

    public void setTradingRight(String tradingRight) {
        this.tradingRight = tradingRight;
    }

    public String getEquityYear() {
        return equityYear;
    }

    public void setEquityYear(String equityYear) {
        this.equityYear = equityYear;
    }

    public String getBuildYear() {
        return buildYear;
    }

    public void setBuildYear(String buildYear) {
        this.buildYear = buildYear;
    }
}
