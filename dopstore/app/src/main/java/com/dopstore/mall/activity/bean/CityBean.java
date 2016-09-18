package com.dopstore.mall.activity.bean;

import java.io.Serializable;

/**
 * Created by 喜成 on 16/9/9.
 * name
 */
public class CityBean implements Serializable {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
