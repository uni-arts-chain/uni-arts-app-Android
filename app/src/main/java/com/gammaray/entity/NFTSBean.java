package com.gammaray.entity;

import java.io.Serializable;

public class NFTSBean implements Serializable {

    private String name;

    private String logo;

    private String address;

    private String total_size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal_size() {
        return total_size;
    }

    public void setTotal_size(String total_size) {
        this.total_size = total_size;
    }
}
