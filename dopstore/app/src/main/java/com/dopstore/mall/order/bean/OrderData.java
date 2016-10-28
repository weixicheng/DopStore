package com.dopstore.mall.order.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 喜成 on 16/9/8.
 * name
 */
public class OrderData implements Serializable {
    private String id;
    private String order_num;
    private String status;
    private String real_fee;
    private List<OrderDataData> goods_relateds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReal_fee() {
        return real_fee;
    }

    public void setReal_fee(String real_fee) {
        this.real_fee = real_fee;
    }

    public List<OrderDataData> getGoods_relateds() {
        return goods_relateds;
    }

    public void setGoods_relateds(List<OrderDataData> goods_relateds) {
        this.goods_relateds = goods_relateds;
    }
}
