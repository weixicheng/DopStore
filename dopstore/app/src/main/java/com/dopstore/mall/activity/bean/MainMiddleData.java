package com.dopstore.mall.activity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name 首页中间数据
 */
public class MainMiddleData implements Serializable {
    private String id;
    private String picture;
    private String title;
    private String url;
    private String is_collect;
    private List<ShopData> related_goods;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<ShopData> getRelated_goods() {
        return related_goods;
    }

    public void setRelated_goods(List<ShopData> related_goods) {
        this.related_goods = related_goods;
    }

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }
}
