package com.dopstore.mall.activity.bean;

import java.io.Serializable;

/**
 * Created by 喜成 on 16/9/5.
 * name 首页底部数据
 */
public class ShopData implements Serializable {
    private String id;
    private String name;
    private String price;
    private String cover;
    private String number;
    private String stock_number;

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }


    public String getStock_number() {
        return stock_number;
    }

    public void setStock_number(String stock_number) {
        this.stock_number = stock_number;
    }

}
