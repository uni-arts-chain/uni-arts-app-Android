package com.gammaray.entity;

import java.io.Serializable;

/**
 * Synopsis     com.miner.client.entity.AppUpdateVo
 * Author       mosr
 * Version      1.0.0
 * Create       2020/7/17 16:15
 * Email        intimatestranger@sina.cn
 */
public class AppUpdateVo implements Serializable {

    /**
     * version_code : 202005300
     * version_name : v1.0.0
     * phone_type : andr
     * download_count : 0
     * download_url : {"url":"https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/mobile_client_version/download_url/1/46d15271-dd45-4eec-af0d-c86d63dd0aa2.apk"}
     * force_updated : true
     * desc : 内测版本
     */

    private String version_code;
    private String version_name;
    private String phone_type;
    private int download_count;
    private DownloadUrlBean download_url;
    private boolean force_updated;
    private String desc;

    public String getVersion_code() {
        return version_code;
    }

    public void setVersion_code(String version_code) {
        this.version_code = version_code;
    }

    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getPhone_type() {
        return phone_type;
    }

    public void setPhone_type(String phone_type) {
        this.phone_type = phone_type;
    }

    public int getDownload_count() {
        return download_count;
    }

    public void setDownload_count(int download_count) {
        this.download_count = download_count;
    }

    public DownloadUrlBean getDownload_url() {
        return download_url;
    }

    public void setDownload_url(DownloadUrlBean download_url) {
        this.download_url = download_url;
    }

    public boolean isForce_updated() {
        return force_updated;
    }

    public void setForce_updated(boolean force_updated) {
        this.force_updated = force_updated;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static class DownloadUrlBean implements Serializable {
        /**
         * url : https://miner-pool.oss-cn-hongkong.aliyuncs.com/miner_pool/mobile_client_version/download_url/1/46d15271-dd45-4eec-af0d-c86d63dd0aa2.apk
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
