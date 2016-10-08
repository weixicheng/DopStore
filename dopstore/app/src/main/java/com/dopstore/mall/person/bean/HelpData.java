package com.dopstore.mall.person.bean;

import java.io.Serializable;

/**
 * Created by 喜成 on 16/9/12.
 * name
 */
public class HelpData implements Serializable {
    private String id;
    private String title;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
