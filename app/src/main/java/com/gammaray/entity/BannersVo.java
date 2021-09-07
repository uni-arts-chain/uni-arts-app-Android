package com.gammaray.entity;

import java.io.Serializable;

/**
 * Synopsis     com.miner.client.entity.BannersVo
 * Author		Mosr
 * Version		1.0.0
 * Create 	    2020/7/2 20:57
 * Email  		intimatestranger@sina.cn
 */
public class BannersVo implements Serializable {

    /**
     * title : 测试2
     * content : 测试2
     * url : https://stackoverflow.com
     * img_max : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/banner/img_max/7/fda9a687-1ab8-4fe3-a7d6-d99b7d3ee90d.jpg
     * img_middle : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/banner/img_middle/7/d4f449ca-c59f-425d-81b7-04e92c1f7880.jpg
     * img_min : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/banner/img_min/7/3f0cd5ca-0890-4bbe-9aa2-3c28657feaf5.jpg
     */

    private String title;
    private String content;
    private String url;
    private String img_max;
    private String img_middle;
    private String img_min;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg_max() {
        return img_max;
    }

    public void setImg_max(String img_max) {
        this.img_max = img_max;
    }

    public String getImg_middle() {
        return img_middle;
    }

    public void setImg_middle(String img_middle) {
        this.img_middle = img_middle;
    }

    public String getImg_min() {
        return img_min;
    }

    public void setImg_min(String img_min) {
        this.img_min = img_min;
    }
}
