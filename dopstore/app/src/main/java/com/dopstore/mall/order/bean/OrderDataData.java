package com.dopstore.mall.order.bean;

import java.io.Serializable;

/**
 * 作者：xicheng on 16/10/9 18:48
 * 类别：
 */

public class OrderDataData implements Serializable{
    private String goods_id;
    private String goods_name;
    private String goods_cover;
    private String goods_price;
    private String goods_num;
    private String goods_sku_str;

    public String getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(String goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_name() {
        return goods_name;
    }

    public void setGoods_name(String goods_name) {
        this.goods_name = goods_name;
    }

    public String getGoods_cover() {
        return goods_cover;
    }

    public void setGoods_cover(String goods_cover) {
        this.goods_cover = goods_cover;
    }

    public String getGoods_price() {
        return goods_price;
    }

    public void setGoods_price(String goods_price) {
        this.goods_price = goods_price;
    }

    public String getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(String goods_num) {
        this.goods_num = goods_num;
    }

    public String getGoods_sku_str() {
        return goods_sku_str;
    }

    public void setGoods_sku_str(String goods_sku_str) {
        this.goods_sku_str = goods_sku_str;
    }
}
