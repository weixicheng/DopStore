package com.dopstore.mall.shop.bean;

import com.dopstore.mall.activity.bean.ShopData;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xicheng on 16/9/23 10:31
 * 类别：
 */
public class ActivityDetailBean implements Serializable{
    private String id;
    private String name;
    private String picture;
    private String age;
    private String merchant;
    private String phone;
    private String start_time;
    private String end_time;
    private String limit;
    private String price;
    private String address;
    private String content;
    private String category;
    private List<ShopData> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ShopData> getItems() {
        return items;
    }

    public void setItems(List<ShopData> items) {
        this.items = items;
    }
}
