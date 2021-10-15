package com.gammaray.entity;

public class ETHToCNYBean {

    private String last_updated;

    private long interval_time;

    private Price price;

    public static class Price {

        private String eth;

        public String getEth() {
            return eth;
        }

        public void setEth(String eth) {
            this.eth = eth;
        }
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public long getInterval_time() {
        return interval_time;
    }

    public void setInterval_time(long interval_time) {
        this.interval_time = interval_time;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }
}
