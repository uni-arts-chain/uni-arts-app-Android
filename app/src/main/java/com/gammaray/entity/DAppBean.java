package com.gammaray.entity;

public class DAppBean {

    private int appIcon;

    private String appName;

    public DAppBean(int appIcon, String appName) {
        this.appIcon = appIcon;
        this.appName = appName;
    }

    public int getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(int appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
