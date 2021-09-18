package com.gammaray.entity;

import java.io.Serializable;

public class WalletLinkBean implements Serializable {

    private String shortName;

    private String name;

    private int imgId;

    public WalletLinkBean(String shortName, String name, int imgId) {
        this.shortName = shortName;
        this.name = name;
        this.imgId = imgId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}
