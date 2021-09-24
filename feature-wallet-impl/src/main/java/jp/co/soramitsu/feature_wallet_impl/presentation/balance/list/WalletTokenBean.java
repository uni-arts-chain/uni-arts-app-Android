package jp.co.soramitsu.feature_wallet_impl.presentation.balance.list;

import java.io.Serializable;

public class WalletTokenBean implements Serializable {

    private String shortName;

    private String name;

    private int imgId;

    private String balance;

    public WalletTokenBean(String shortName, String name, int imgId, String balance) {
        this.shortName = shortName;
        this.name = name;
        this.imgId = imgId;
        this.balance = balance;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
