package com.dopstore.mall.activity.bean;

import java.util.List;

/**
 * Created by 喜成 on 16/9/5.
 * name 首页中间数据
 */
public class MainMiddleData {
    private String id;
    private String image;
    private List<MainMiddleListData> dataList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<MainMiddleListData> getDataList() {
        return dataList;
    }

    public void setDataList(List<MainMiddleListData> dataList) {
        this.dataList = dataList;
    }
}
