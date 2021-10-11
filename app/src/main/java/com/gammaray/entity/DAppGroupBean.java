package com.gammaray.entity;

import java.io.Serializable;
import java.util.List;

public class DAppGroupBean implements Serializable {

    private int id;

    private String title;

    private List<DApps> dapps;

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

    public List<DApps> getDapps() {
        return dapps;
    }

    public void setDapps(List<DApps> dapps) {
        this.dapps = dapps;
    }

    public static class DApps{

        private int id;

        private Logo logo;

        public static class Logo{

            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        private String website_url;

        private int chain_category_id;

        private String created_at;

        private String updated_at;

        private String title;

        private String desc;

        private boolean favorite_by_me;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Logo getLogo() {
            return logo;
        }

        public void setLogo(Logo logo) {
            this.logo = logo;
        }

        public String getWebsite_url() {
            return website_url;
        }

        public void setWebsite_url(String website_url) {
            this.website_url = website_url;
        }

        public int getChain_category_id() {
            return chain_category_id;
        }

        public void setChain_category_id(int chain_category_id) {
            this.chain_category_id = chain_category_id;
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public boolean isFavorite_by_me() {
            return favorite_by_me;
        }

        public void setFavorite_by_me(boolean favorite_by_me) {
            this.favorite_by_me = favorite_by_me;
        }
    }
}
