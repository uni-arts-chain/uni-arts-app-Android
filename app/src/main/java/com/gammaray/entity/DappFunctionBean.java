package com.gammaray.entity;

public class DappFunctionBean {

    private String dappName;

    private int dappIcon;

    public DappFunctionBean(String dappName, int dappIcon) {
        this.dappName = dappName;
        this.dappIcon = dappIcon;
    }

    public String getDappName() {
        return dappName;
    }

    public void setDappName(String dappName) {
        this.dappName = dappName;
    }

    public int getDappIcon() {
        return dappIcon;
    }

    public void setDappIcon(int dappIcon) {
        this.dappIcon = dappIcon;
    }
}

