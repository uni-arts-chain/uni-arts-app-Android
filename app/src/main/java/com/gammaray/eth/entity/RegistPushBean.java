package com.gammaray.eth.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegistPushBean {

    /**
     * wallets : {"1596421679":["0x45F6eeC8dFFd83f9737BFC872f7C011d79C0164B"],"60":["0xa5760BB0777647cb1C69E75A64234F6103778D05"]}
     * deviceID : 7E66781C-6F03-48D9-BEFF-CDB2F7BA8913
     * preferences : {}
     * type : ios
     * token : 5639e8188fb3e53f1297cd8daaa553efbc8de0a15f4b469134ed0c15deb38283
     */

    private WalletsBean wallets;
    private String deviceID;
    private PreferencesBean preferences;
    private String type;
    private String token;

    public WalletsBean getWallets() {
        return wallets;
    }

    public void setWallets(WalletsBean wallets) {
        this.wallets = wallets;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public PreferencesBean getPreferences() {
        return preferences;
    }

    public void setPreferences(PreferencesBean preferences) {
        this.preferences = preferences;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class WalletsBean {
        @SerializedName("1596421679")
        private List<String> _$1596421679;
        @SerializedName("60")
        private List<String> _$60;

        public List<String> get_$1596421679() {
            return _$1596421679;
        }

        public void set_$1596421679(List<String> _$1596421679) {
            this._$1596421679 = _$1596421679;
        }

        public List<String> get_$60() {
            return _$60;
        }

        public void set_$60(List<String> _$60) {
            this._$60 = _$60;
        }
    }

    public static class PreferencesBean {
    }
}