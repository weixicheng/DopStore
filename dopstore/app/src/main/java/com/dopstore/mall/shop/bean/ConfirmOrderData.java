package com.dopstore.mall.shop.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xicheng on 2016/10/21 10:50
 * 类别：
 */

public class ConfirmOrderData implements Serializable {
    private String error_code;
    private List<ResultData> result;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<ResultData> getResult() {
        return result;
    }

    public void setResult(List<ResultData> result) {
        this.result = result;
    }

    public class ResultData implements Serializable {
        private String id;
        private String name;
        private String cover;
        private double price;
        private String goods_sku_str;
        private int number;

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

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getGoods_sku_str() {
            return goods_sku_str;
        }

        public void setGoods_sku_str(String goods_sku_str) {
            this.goods_sku_str = goods_sku_str;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }
}
