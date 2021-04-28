package com.yunhualian.entity;

public class OrderAmountVo {

    /**
     * sn : g6aPt541wvQCPiwF1m1f9yEKGCFSwHHG
     * is_mine : true
     * address : 5GNLQHb56SDF6ASRPoNHSVKQxP47cvxhXL5DaRKTLSJJqF1c
     * price : 0.1
     * amount : 3
     * total_amount : 6
     */

    private String sn;
    private boolean is_mine;
    private String address;
    private String price;
    private int amount;
    private int total_amount;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public boolean isIs_mine() {
        return is_mine;
    }

    public void setIs_mine(boolean is_mine) {
        this.is_mine = is_mine;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(int total_amount) {
        this.total_amount = total_amount;
    }
}
