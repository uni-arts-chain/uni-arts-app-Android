package com.yunhualian.entity;

import android.text.TextUtils;


import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by gengda on 2018/4/10.
 */
public class TickerVo implements Serializable {

    /**
     * at: 1535593734,
     * name: "eoseth",
     * code: "eos",
     * quote: "eth",
     * logo: "https://rfinex.com/icon-eos.png",
     * trade_flag: true,
     * price_group_fixed: "8",
     * ticker : {"buy":"0.0","sell":"0.0","low":"0.0","high":"0.0","last":"0.000008","vol":"0.0","logo":"https://rfinex.com/icon-atm.png","exchange_rate":8.0E-6,"usd_rate":726.3,"cny_rate":4599.57}
     * base_unit:"btc",
     * quote_unit:"cnst"
     * scalp_market:false,
     * watt_market:false
     * "rise_limit_price":"0",
     * "decline_limit_price":"0"
     */

    private long at;

    private String name;

    private String code;

    private String quote;

    private String logo;

    private String symbol;

    private boolean trade_flag;

    private String price_group_fixed;

    private TickerBean ticker;

    private MarketVo.TradeBean bid;    //买单

    private MarketVo.TradeBean ask;    //卖单

    private String base_unit;

    private String quote_unit;

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
    private String dollar;
    private String rmb;

    public String getDollar() {
        return dollar;
    }

    public void setDollar(String dollar) {
        this.dollar = dollar;
    }

    public String getRmb() {
        return rmb;
    }

    public void setRmb(String rmb) {
        this.rmb = rmb;
    }

    public long getAt() {
        return at;
    }

    public void setAt(long at) {
        this.at = at;
    }

    public String getName() {
        if (name.contains("/"))
            name = name.replace("/", "");

        return name.toLowerCase();
    }

    public String getName_() {
        return name.toLowerCase();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return base_unit;
    }

    public String getQuote() {
        return quote_unit;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public boolean isTrade_flag() {
        return trade_flag;
    }

    public void setTrade_flag(boolean trade_flag) {
        this.trade_flag = trade_flag;
    }

    public int getPrice_group_fixed() {
        if (TextUtils.isEmpty(price_group_fixed))
            return 8;

        return Integer.parseInt(price_group_fixed);
    }

    public void setPrice_group_fixed(int price_group_fixed) {
        this.price_group_fixed = String.valueOf(price_group_fixed);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setPrice_group_fixed(String price_group_fixed) {
        this.price_group_fixed = price_group_fixed;
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

    public TickerBean getTicker() {
        return ticker;
    }

    public void setTicker(TickerBean ticker) {
        this.ticker = ticker;
    }

    public MarketVo.TradeBean getBid() {
        return bid;
    }

    public void setBid(MarketVo.TradeBean bid) {
        this.bid = bid;
    }

    public MarketVo.TradeBean getAsk() {
        return ask;
    }

    public void setAsk(MarketVo.TradeBean ask) {
        this.ask = ask;
    }

    public static class TickerBean implements Serializable {

        /**
         * buy: "0.02145707",
         * sell: "0.0214905",
         * low: "0.019",
         * high: "0.0221176",
         * last: "0.02145793",
         * vol: "297410.1831",
         * open: "0.0216"
         */

        private BigDecimal buy;

        private BigDecimal sell;

        private BigDecimal low;

        private BigDecimal high;

        private BigDecimal last;

        private BigDecimal vol;

        private BigDecimal volume;

        private BigDecimal open;

        public BigDecimal getBuy() {
            return buy;
        }

        public void setBuy(BigDecimal buy) {
            this.buy = buy;
        }

        public BigDecimal getSell() {
            return sell;
        }

        public void setSell(BigDecimal sell) {
            this.sell = sell;
        }

        public BigDecimal getLow() {
            return low;
        }

        public void setLow(BigDecimal low) {
            this.low = low;
        }

        public BigDecimal getHigh() {
            return high;
        }

        public void setHigh(BigDecimal high) {
            this.high = high;
        }

        public BigDecimal getLast() {
            return last;
        }

        public void setLast(BigDecimal last) {
            this.last = last;
        }

        public BigDecimal getVol() {
            return null == volume ? vol : volume;
        }

        public void setVol(BigDecimal vol) {
            this.vol = vol;
        }

        public BigDecimal getVolume() {
            return volume;
        }

        public void setVolume(BigDecimal volume) {
            this.volume = volume;
        }

        public BigDecimal getOpen() {
            return open;
        }

        public void setOpen(BigDecimal open) {
            this.open = open;
        }

        public BigDecimal getOrderRate() {
            if (this.open.compareTo(BigDecimal.ZERO) == 0)
                return BigDecimal.ZERO;
            else {
                BigDecimal rate = this.last.subtract(this.open);

                if (null == rate)
                    return BigDecimal.ZERO;

                return rate.divide(this.open, 4, BigDecimal.ROUND_DOWN);
            }
        }
    }

}
