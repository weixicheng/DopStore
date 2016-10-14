package com.dopstore.mall.order.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xicheng on 16/10/9 21:21
 * 类别：
 */

public class OrderDetailData implements Serializable {
    private String error_code;
    private LogisticsData logistics;
    private DetailOrderData order;
    private List<DetailOrderListData> goods_relateds;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public LogisticsData getLogistics() {
        return logistics;
    }

    public void setLogistics(LogisticsData logistics) {
        this.logistics = logistics;
    }

    public DetailOrderData getOrder() {
        return order;
    }

    public void setOrder(DetailOrderData order) {
        this.order = order;
    }

    public List<DetailOrderListData> getGoods_relateds() {
        return goods_relateds;
    }

    public void setGoods_relateds(List<DetailOrderListData> goods_relateds) {
        this.goods_relateds = goods_relateds;
    }
}
