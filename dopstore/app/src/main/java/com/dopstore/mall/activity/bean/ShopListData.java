package com.dopstore.mall.activity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xicheng on 2016/10/19 14:27
 * 类别：
 */

public class ShopListData implements Serializable {
    private String error_code;
    private List<ShopData> items;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<ShopData> getItems() {
        return items;
    }

    public void setItems(List<ShopData> items) {
        this.items = items;
    }
}
