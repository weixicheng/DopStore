package com.dopstore.mall.order.bean;

import java.io.Serializable;

/**
 * 作者：xicheng on 16/10/9 21:23
 * 类别：
 */

public class DetailOrderData implements Serializable {
    private String order_num;
    private String total_fee;
    private String real_fee;
    private String status;
    private String note;
    private DetailAddressData address;

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(String total_fee) {
        this.total_fee = total_fee;
    }

    public String getReal_fee() {
        return real_fee;
    }

    public void setReal_fee(String real_fee) {
        this.real_fee = real_fee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public DetailAddressData getAddress() {
        return address;
    }

    public void setAddress(DetailAddressData address) {
        this.address = address;
    }
}
