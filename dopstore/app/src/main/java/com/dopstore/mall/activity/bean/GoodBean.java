package com.dopstore.mall.activity.bean;

import java.io.Serializable;

public class GoodBean implements Serializable{
    private int id;
    private int carNum;
    private String content;
    private String cover;
    private String goods_sku_id;
    private String goods_sku_str;
    private Double price;
    private boolean isChoose;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarNum() {
        return carNum;
    }

    public void setCarNum(int carNum) {
        this.carNum = carNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public String getGoods_sku_id() {
        return goods_sku_id;
    }

    public void setGoods_sku_id(String goods_sku_id) {
        this.goods_sku_id = goods_sku_id;
    }

    public String getGoods_sku_str() {
        return goods_sku_str;
    }

    public void setGoods_sku_str(String goods_sku_str) {
        this.goods_sku_str = goods_sku_str;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
