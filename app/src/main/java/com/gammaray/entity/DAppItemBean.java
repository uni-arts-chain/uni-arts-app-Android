package com.gammaray.entity;

import java.io.Serializable;

public class DAppItemBean implements Serializable {

    private int id;

    private String title;

    private String desc;

    private String website_url;

    private DAppGroupBean.DApps.Logo logo;

    private ChainCategory chain_category;


    public static class ChainCategory {

        private int id;

        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public DAppGroupBean.DApps.Logo getLogo() {
        return logo;
    }

    public void setLogo(DAppGroupBean.DApps.Logo logo) {
        this.logo = logo;
    }

    public ChainCategory getChain_category() {
        return chain_category;
    }

    public void setChain_category(ChainCategory chain_category) {
        this.chain_category = chain_category;
    }
}
