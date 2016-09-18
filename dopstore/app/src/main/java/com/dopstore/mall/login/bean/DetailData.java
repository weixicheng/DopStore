package com.dopstore.mall.login.bean;

import java.io.Serializable;

/**
 * Created by 喜成 on 16/9/6.
 * name
 */
public class DetailData implements Serializable {
    private String id;
    private String image;
    private String name;
    private String isSelect;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(String isSelect) {
        this.isSelect = isSelect;
    }
}
