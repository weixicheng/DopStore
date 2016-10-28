package com.dopstore.mall.shop.bean;

import android.text.TextUtils;

import com.dopstore.mall.activity.bean.ShopData;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xicheng on 16/9/23 10:31
 * 类别：
 */
public class ActivityDetailBean implements Serializable {
    private String id;
    private String name;
    private List<String> picture;
    private String age;
    private String merchant;
    private String phone;
    private String start_time;
    private String end_time;
    private String limit;
    private String price;
    private String address;
    private String content;
    private String is_collect;
    private String category;
    private String cover;
    private List<ShopData> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (TextUtils.isEmpty(id)) {
            id = "";
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (TextUtils.isEmpty(name)) {
            name = "";
        }
        this.name = name;
    }

    public List<String> getPicture() {
        return picture;
    }

    public void setPicture(List<String> picture) {
        this.picture = picture;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        if (TextUtils.isEmpty(age)) {
            age = "";
        }
        this.age = age;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        if (TextUtils.isEmpty(merchant)) {
            merchant = "";
        }
        this.merchant = merchant;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            phone = "";
        }
        this.phone = phone;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        if (TextUtils.isEmpty(start_time)) {
            start_time = "";
        }
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        if (TextUtils.isEmpty(end_time)) {
            end_time = "";
        }
        this.end_time = end_time;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        if (TextUtils.isEmpty(limit)) {
            limit = "";
        }
        this.limit = limit;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        if (TextUtils.isEmpty(price)) {
            price = "";
        }
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (TextUtils.isEmpty(address)) {
            address = "";
        }
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (TextUtils.isEmpty(content)) {
            content = "";
        }
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        if (TextUtils.isEmpty(category)) {
            category = "";
        }
        this.category = category;
    }

    public List<ShopData> getItems() {
        return items;
    }

    public void setItems(List<ShopData> items) {
        this.items = items;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        if (TextUtils.isEmpty(is_collect)) {
            is_collect = "";
        }
        this.is_collect = is_collect;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        if (TextUtils.isEmpty(cover)) {
            cover = "";
        }
        this.cover = cover;
    }
}
