package com.dopstore.mall.order.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xicheng on 16/10/9 18:53
 * 类别：
 */

public class OrderOtherData implements Serializable {
    private String error_code;
    private List<OrderData> orders;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<OrderData> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderData> orders) {
        this.orders = orders;
    }
}
