package com.gammaray.eth.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegisterPush_E {

    /**
     * wallets : {"1596421679":["0x45F6eeC8dFFd83f9737BFC872f7C011d79C0164B"],"60":["0xa5760BB0777647cb1C69E75A64234F6103778D05","0x3db741bdf7104Da5872d79Ca1dD46E5708a2Ec6d"]}
     * deviceID : 11D36066-E903-463C-8E71-1776956222D4
     * preferences : {}
     * type : ios
     * token : ea35e033e52b8c40c325b4471de4e3cf
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
        @SerializedName("60")
        private List<String> _$60;

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
