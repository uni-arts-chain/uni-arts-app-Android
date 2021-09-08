package com.gammaray.eth.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegiseResponse {

    /**
     * id : 5fe013349c4be79e7ac3e653
     * deviceID : 00533622-b6c2-32f3-a295-56811f0005c5
     * createdAt : 1608520500
     * token : e5676fa272e9c1871165964d6de96ef0
     * type : android
     * updatedAt : 1608520500
     * wallets : {"1596421679":["0xDbE8Fbe92751Cb0Ff02432468b0a07F271622f11"]}
     */

    private String id;
    private String deviceID;
    private int createdAt;
    private String token;
    private String type;
    private int updatedAt;
    private WalletsBean wallets;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public int getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(int createdAt) {
        this.createdAt = createdAt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(int updatedAt) {
        this.updatedAt = updatedAt;
    }

    public WalletsBean getWallets() {
        return wallets;
    }

    public void setWallets(WalletsBean wallets) {
        this.wallets = wallets;
    }

    public static class WalletsBean {
        @SerializedName("1596421679")
        private List<String> _$1596421679;

        public List<String> get_$1596421679() {
            return _$1596421679;
        }

        public void set_$1596421679(List<String> _$1596421679) {
            this._$1596421679 = _$1596421679;
        }
    }
}
