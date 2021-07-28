package com.yunhualian.entity;


import java.io.Serializable;

public class ArtPriceVo implements Serializable {
    /**
     * title : 从低到高
     * id : 1
     */
    public ArtPriceVo(String title, int id) {
        this.title = title;
        this.id = id;
    }

    private String title;
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
