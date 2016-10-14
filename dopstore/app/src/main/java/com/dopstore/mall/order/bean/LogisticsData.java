package com.dopstore.mall.order.bean;

import java.io.Serializable;

/**
 * 作者：xicheng on 16/10/9 20:50
 * 类别：
 */

public class LogisticsData implements Serializable {
    private String logistics_id;
    private String logistics_no;
    private String logistics_company;
    private String logistics_zone;
    private String logistics_remark;
    private String logistics_time;

    public String getLogistics_id() {
        return logistics_id;
    }

    public void setLogistics_id(String logistics_id) {
        this.logistics_id = logistics_id;
    }

    public String getLogistics_no() {
        return logistics_no;
    }

    public void setLogistics_no(String logistics_no) {
        this.logistics_no = logistics_no;
    }

    public String getLogistics_company() {
        return logistics_company;
    }

    public void setLogistics_company(String logistics_company) {
        this.logistics_company = logistics_company;
    }

    public String getLogistics_zone() {
        return logistics_zone;
    }

    public void setLogistics_zone(String logistics_zone) {
        this.logistics_zone = logistics_zone;
    }

    public String getLogistics_remark() {
        return logistics_remark;
    }

    public void setLogistics_remark(String logistics_remark) {
        this.logistics_remark = logistics_remark;
    }

    public String getLogistics_time() {
        return logistics_time;
    }

    public void setLogistics_time(String logistics_time) {
        this.logistics_time = logistics_time;
    }
}
