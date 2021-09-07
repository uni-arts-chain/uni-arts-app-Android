package com.gammaray.entity;

public class PayResult {

    /**
     * url : https://openapi.alipay.com/gateway.do?app_id=2021002132648950&charset=UTF-8&sign_type=RSA2&version=1.0&timestamp=2021-04-29+18%3A31%3A04&method=alipay.trade.wap.pay&notify_url&biz_content=%7B%22out_trade_no%22%3A%22WXqtu3PaiE8XKE91BdbwiQ9z1eYTHxy1%22%2C%22total_amount%22%3A%220.1%22%2C%22subject%22%3A%22manghe+box%22%2C%22product_code%22%3A%22QUICK_WAP_WAY%22%7D&sign=dJJZ4CEK63iFOCGbJxzqA1gWny%2FszdG2R4cU0IoDmU5VSD6WwX9Fj%2BZ3iZLOL8uSCwYFsaE66vREchdigKB9NKo39p9%2B8cO7pXSfTTJt4w%2FC%2B%2BH4tk7WFBuriPHRCuQE4MFzjtkz5Ok%2FDPfVDLV13zHM0rCF63lQxZmShDRM%2BQrg%2FhpCPVY%2BfnCsu6QynHV4x%2FsqPtnsrsZ%2BWkTDYFh6KSMWcReFc1HCRdvStIsv08jrhVYmaM1B9AgMPniq5ahLxnMu2UjpEQSvWTCTlBVa94mQ%2Bn21sABG6ZuRhFJJHXT7Gm2ziSrEpLbgMhucE7mZ5kGyp5tbeL7F3aimT58uMg%3D%3D
     */

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
