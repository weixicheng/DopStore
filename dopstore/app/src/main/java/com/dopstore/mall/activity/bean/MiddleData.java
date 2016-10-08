package com.dopstore.mall.activity.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 作者：xicheng on 16/9/20 15:30
 * 类别：
 */
public class MiddleData implements Serializable {
    private String error_code;
    private List<MainMiddleData> themes;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<MainMiddleData> getThemes() {
        return themes;
    }

    public void setThemes(List<MainMiddleData> themes) {
        this.themes = themes;
    }
}
