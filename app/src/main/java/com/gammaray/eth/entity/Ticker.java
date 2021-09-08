package com.gammaray.eth.entity;


import java.util.List;

public class Ticker {
    /**
     * tickers : [{"base":"CNY","symbol":"ETH","change_24h":"0.0036810581482406","provider":"tatams","price":"4127.9242","id":60},{"base":"CNY","symbol":"QTS","change_24h":"0.0009799118079373","provider":"tatams","price":"1.3105845","id":1596421679},{"base":"CNY","symbol":"FFT","change_24h":"-0.0019672131147541","provider":"tatams","price":"1.952726","id":1596421679},{"base":"CNY","symbol":"TMS","change_24h":"0","provider":"tatams","price":"6.415","id":1596421679},{"base":"CNY","symbol":"EPC","change_24h":"0.0254727940562559","provider":"tatams","price":"237.9485158","id":1596421679},{"base":"CNY","symbol":"THHC","change_24h":"-0.0065941437422082","provider":"tatams","price":"12.08408946","id":1596421679},{"base":"CNY","symbol":"FFST","change_24h":"-0.0134470560229414","provider":"tatams","price":"10.28868492","id":1596421679},{"base":"CNY","symbol":"KJQ","change_24h":"0","provider":"tatams","price":"0","id":1596421679},{"base":"CNY","symbol":"FFT2","change_24h":"0","provider":"tatams","price":"12.83","id":1596421679},{"base":"CNY","symbol":"HERA","change_24h":"0","provider":"tatams","price":"0","id":60}]
     * currency : CNY
     */

    private String currency;
    private List<TickersBean> tickers;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<TickersBean> getTickers() {
        return tickers;
    }

    public void setTickers(List<TickersBean> tickers) {
        this.tickers = tickers;
    }

    public static class TickersBean {
        /**
         * base : CNY
         * symbol : ETH
         * change_24h : 0.0036810581482406
         * provider : tatams
         * price : 4127.9242
         * id : 60
         */

        private String base;
        private String symbol;
        private String change_24h;
        private String provider;
        private String price;
        private int id;

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getChange_24h() {
            return change_24h;
        }

        public void setChange_24h(String change_24h) {
            this.change_24h = change_24h;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }


    /**
     * tickers : [{"base":"TMS","symbol":"QTS","change_24h":"-0.0039177277179236","provider":"tatams","price":"1.308879","id":1596421679}]
     * currency : QTS
     */


}
