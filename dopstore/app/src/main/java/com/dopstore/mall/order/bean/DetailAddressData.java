package com.dopstore.mall.order.bean;

import java.io.Serializable;

/**
 * 作者：xicheng on 16/10/9 21:25
 * 类别：
 */

public class DetailAddressData implements Serializable {
    private String address_id;
    private String shipping_user;
    private String mobile;
    private String detail;

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getShipping_user() {
        return shipping_user;
    }

    public void setShipping_user(String shipping_user) {
        this.shipping_user = shipping_user;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
