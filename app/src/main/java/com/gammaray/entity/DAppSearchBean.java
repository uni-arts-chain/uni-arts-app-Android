package com.gammaray.entity;

import java.io.Serializable;

public class DAppSearchBean implements Serializable {

    private int id;

    private String title;

    private String desc;

    private String website_url;

    private Logo logo;

    private ChainCategory chain_category;

    private boolean favorite_by_me;

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

    public Logo getLogo() {
        return logo;
    }

    public void setLogo(Logo logo) {
        this.logo = logo;
    }

    public ChainCategory getChain_category() {
        return chain_category;
    }

    public void setChain_category(ChainCategory chain_category) {
        this.chain_category = chain_category;
    }

    public boolean isFavorite_by_me() {
        return favorite_by_me;
    }

    public void setFavorite_by_me(boolean favorite_by_me) {
        this.favorite_by_me = favorite_by_me;
    }

    public static class Logo {

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class ChainCategory {

        private int id;

        private int chain_id;

        private int sort;

        private String created_at;

        private String updated_at;

        private String title;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getChain_id() {
            return chain_id;
        }

        public void setChain_id(int chain_id) {
            this.chain_id = chain_id;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

    }
}
