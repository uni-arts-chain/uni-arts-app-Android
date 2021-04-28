package com.yunhualian.entity;

import android.text.TextUtils;

import java.io.Serializable;
import java.math.BigDecimal;

public class MarketVo implements Serializable {

    /**
     * "id":"btccnst",
     * "name":"BTC/CNST",
     * "logo":"https://rfinex.com/icons/btc.png",
     * "coinmarketcap_url":"",
     * "code":82,
     * "base_unit":"btc",
     * "quote_unit":"cnst",
     * "price_group_fixed":"8",
     * "sort_order":1,
     * "bid":{
     * "fee":"0.001",
     * "currency":"cnst",
     * "fixed":8
     * },
     * "ask":{
     * "fee":"0.001",
     * "currency":"btc",
     * "fixed":8
     * },
     * "visible":true,
     * "tradable":true,
     * "scalp_market":true,
     * "watt_market":false,
     * "rise_limit":"0",
     * "decline_limit":"0"
     * "rise_limit_price": "2.645",
     * "decline_limit_price": "0",
     * "rise_limit_price_tip": "",
     * "rise_limit_price_warning": "",
     * "decline_limit_price_tip": "",
     * "decline_limit_price_warning": ""
     */

    private String id;

    private String name;

    private String logo;

    private String coinmarketcap_url;

    private int code;

    private String base_unit;

    private String quote_unit;

    private String price_group_fixed;

    private int sort_order;

    private TradeBean bid;

    private TradeBean ask;

    private boolean visible;

    private boolean tradable;

    private boolean scalp_market;

    private boolean watt_market;

    private BigDecimal rise_limit;

    private BigDecimal decline_limit;

    private BigDecimal rise_limit_price;

    private BigDecimal decline_limit_price;

    private String rise_limit_price_tip;

    private String rise_limit_price_warning;

    private String decline_limit_price_tip;

    private String decline_limit_price_warning;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        if (name.contains("/"))
            name = name.replace("/", "");

        return name.toLowerCase();
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

    public String getCoinmarketcap_url() {
        return coinmarketcap_url;
    }

    public void setCoinmarketcap_url(String coinmarketcap_url) {
        this.coinmarketcap_url = coinmarketcap_url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getBase_unit() {
        return base_unit;
    }

    public void setBase_unit(String base_unit) {
        this.base_unit = base_unit;
    }

    public String getQuote_unit() {
        return quote_unit;
    }

    public void setQuote_unit(String quote_unit) {
        this.quote_unit = quote_unit;
    }

    public int getPrice_group_fixed() {
        if (TextUtils.isEmpty(price_group_fixed))
            return 8;

        return Integer.parseInt(price_group_fixed);
    }

    public void setPrice_group_fixed(String price_group_fixed) {
        this.price_group_fixed = price_group_fixed;
    }

    public int getSort_order() {
        return sort_order;
    }

    public void setSort_order(int sort_order) {
        this.sort_order = sort_order;
    }

    public TradeBean getBid() {
        return bid;
    }

    public void setBid(TradeBean bid) {
        this.bid = bid;
    }

    public TradeBean getAsk() {
        return ask;
    }

    public void setAsk(TradeBean ask) {
        this.ask = ask;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isTradable() {
        return tradable;
    }

    public void setTradable(boolean tradable) {
        this.tradable = tradable;
    }

    public boolean isScalp_market() {
        return scalp_market;
    }

    public void setScalp_market(boolean scalp_market) {
        this.scalp_market = scalp_market;
    }

    public boolean isWatt_market() {
        return watt_market;
    }

    public void setWatt_market(boolean watt_market) {
        this.watt_market = watt_market;
    }

    public BigDecimal getRise_limit() {
        return rise_limit;
    }

    public void setRise_limit(BigDecimal rise_limit) {
        this.rise_limit = rise_limit;
    }

    public BigDecimal getDecline_limit() {
        return decline_limit;
    }

    public void setDecline_limit(BigDecimal decline_limit) {
        this.decline_limit = decline_limit;
    }

    public BigDecimal getRise_limit_price() {
        return rise_limit_price;
    }

    public void setRise_limit_price(BigDecimal rise_limit_price) {
        this.rise_limit_price = rise_limit_price;
    }

    public BigDecimal getDecline_limit_price() {
        return decline_limit_price;
    }

    public void setDecline_limit_price(BigDecimal decline_limit_price) {
        this.decline_limit_price = decline_limit_price;
    }

    public String getRise_limit_price_tip() {
        return rise_limit_price_tip;
    }

    public void setRise_limit_price_tip(String rise_limit_price_tip) {
        this.rise_limit_price_tip = rise_limit_price_tip;
    }

    public String getRise_limit_price_warning() {
        return rise_limit_price_warning;
    }

    public void setRise_limit_price_warning(String rise_limit_price_warning) {
        this.rise_limit_price_warning = rise_limit_price_warning;
    }

    public String getDecline_limit_price_tip() {
        return decline_limit_price_tip;
    }

    public void setDecline_limit_price_tip(String decline_limit_price_tip) {
        this.decline_limit_price_tip = decline_limit_price_tip;
    }

    public String getDecline_limit_price_warning() {
        return decline_limit_price_warning;
    }

    public void setDecline_limit_price_warning(String decline_limit_price_warning) {
        this.decline_limit_price_warning = decline_limit_price_warning;
    }

    public static class TradeBean implements Serializable {

        private BigDecimal fee;

        private String currency;

        private int fixed;

        public BigDecimal getFee() {
            return fee;
        }

        public void setFee(BigDecimal fee) {
            this.fee = fee;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public int getFixed() {
            return fixed;
        }

        public void setFixed(int fixed) {
            this.fixed = fixed;
        }
    }

}