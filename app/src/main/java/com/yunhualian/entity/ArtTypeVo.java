package com.yunhualian.entity;

import java.io.Serializable;

public class ArtTypeVo implements Serializable {

    public ArtTypeVo(String title, String desc, int id) {
        this.title = title;
        this.desc = desc;
        this.id = id;
    }
    /**
     * title : 摄影
     * desc : 6
     * id : 8
     */

    private String title;
    private String desc;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
