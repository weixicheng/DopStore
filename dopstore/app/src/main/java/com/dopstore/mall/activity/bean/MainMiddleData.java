package com.dopstore.mall.activity.bean;

import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name 首页中间数据
 */
public class MainMiddleData {
    private String id;
    private String picture;
    private String title;
    private String url;
    private List<MainMiddleListData> dataList;

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

    public List<MainMiddleListData> getDataList() {
        return dataList;
    }

    public void setDataList(List<MainMiddleListData> dataList) {
        this.dataList = dataList;
    }
}
