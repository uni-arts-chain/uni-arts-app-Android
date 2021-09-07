package com.gammaray.adapter;

import java.io.Serializable;

/**
 * @Date:2021/8/5
 * @Author:Created by peter_ben
 */
public class UploadCodeBean implements Serializable {

    private String pay_type;

    private Img img;

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public Img getImg() {
        return img;
    }

    public void setImg(Img img) {
        this.img = img;
    }

    public static class Img {
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
