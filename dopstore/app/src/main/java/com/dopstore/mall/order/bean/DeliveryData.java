package com.dopstore.mall.order.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xicheng on 16/10/10 09:26
 * 类别：
 */

public class DeliveryData implements Serializable{
    private String error_code;
    private String logistics_no;
    private String logistics_company_name;
    private List<DeliveryListData> logistics_list;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public String getLogistics_no() {
        return logistics_no;
    }

    public void setLogistics_no(String logistics_no) {
        this.logistics_no = logistics_no;
    }

    public String getLogistics_company_name() {
        return logistics_company_name;
    }

    public void setLogistics_company_name(String logistics_company_name) {
        this.logistics_company_name = logistics_company_name;
    }

    public List<DeliveryListData> getLogistics_list() {
        return logistics_list;
    }

    public void setLogistics_list(List<DeliveryListData> logistics_list) {
        this.logistics_list = logistics_list;
    }

   public class DeliveryListData implements Serializable{
        private String logistics_id;
        private String logistics_zone;
        private String logistics_remark;
        private String logistics_time;

        public String getLogistics_id() {
            return logistics_id;
        }

        public void setLogistics_id(String logistics_id) {
            this.logistics_id = logistics_id;
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
}
