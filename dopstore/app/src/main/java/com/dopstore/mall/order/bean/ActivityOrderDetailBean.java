package com.dopstore.mall.order.bean;

/**
 * 作者：xicheng on 16/9/23 17:19
 * 类别：
 */
public class ActivityOrderDetailBean {
    private String  order_num;
    private int  status;
    private String  pic;
    private String  name;
    private String  address;
    private long  start_time;
    private String  category;
    private float  price;
    private float  total_fee;
    private String  code;
    private String  q_code;

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(float total_fee) {
        this.total_fee = total_fee;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQ_code() {
        return q_code;
    }

    public void setQ_code(String q_code) {
        this.q_code = q_code;
    }
}