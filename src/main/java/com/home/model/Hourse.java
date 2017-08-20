package com.home.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * Created by Administrator on 2017/8/19.
 */
@Document(collection = "hourse")
public class Hourse {
    @Id
    private String id;

    private String title;

    private Integer price;

    private Integer acreage;//面积

    private Integer status;//0出租，1出售

    private String houseOwnerName;

    private String houseOwnerPhone;

    private String address;

    private String infomation;

    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    private Integer state;//0公开，1私有

    @Field("images")
    private Collection<Image> images = new LinkedHashSet<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getAcreage() {
        return acreage;
    }

    public void setAcreage(Integer acreage) {
        this.acreage = acreage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHouseOwnerName() {
        return houseOwnerName;
    }

    public void setHouseOwnerName(String houseOwnerName) {
        this.houseOwnerName = houseOwnerName;
    }

    public String getHouseOwnerPhone() {
        return houseOwnerPhone;
    }

    public void setHouseOwnerPhone(String houseOwnerPhone) {
        this.houseOwnerPhone = houseOwnerPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInfomation() {
        return infomation;
    }

    public void setInfomation(String infomation) {
        this.infomation = infomation;
    }

    public Collection<Image> getImages() {
        return images;
    }

    public void setImages(Collection<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Hourse{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", acreage=" + acreage +
                ", status=" + status +
                ", houseOwnerName='" + houseOwnerName + '\'' +
                ", houseOwnerPhone='" + houseOwnerPhone + '\'' +
                ", address='" + address + '\'' +
                ", infomation='" + infomation + '\'' +
                ", userId='" + userId + '\'' +
                ", state=" + state +
                ", images=" + images +
                '}';
    }
}
