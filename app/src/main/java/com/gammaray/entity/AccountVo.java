package com.gammaray.entity;

import java.io.Serializable;

/**
 * @Date:2021/8/5
 * @Author:Created by peter_ben
 */
public class AccountVo implements Serializable {

    private int id;

    private String currency_code;

    private String balance;

    private String locked;

    private String awards;

    private String total_price;

    private int currency_awards_fee;

    private int default_withdraw_fund_source_id;

    private int default_withdraw_bank_source_id;

    private String logo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getAwards() {
        return awards;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public int getCurrency_awards_fee() {
        return currency_awards_fee;
    }

    public void setCurrency_awards_fee(int currency_awards_fee) {
        this.currency_awards_fee = currency_awards_fee;
    }

    public int getDefault_withdraw_fund_source_id() {
        return default_withdraw_fund_source_id;
    }

    public void setDefault_withdraw_fund_source_id(int default_withdraw_fund_source_id) {
        this.default_withdraw_fund_source_id = default_withdraw_fund_source_id;
    }

    public int getDefault_withdraw_bank_source_id() {
        return default_withdraw_bank_source_id;
    }

    public void setDefault_withdraw_bank_source_id(int default_withdraw_bank_source_id) {
        this.default_withdraw_bank_source_id = default_withdraw_bank_source_id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
