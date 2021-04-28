package com.yunhualian.net;


/**
 * Created by gengda on 2018/4/3.
 */

import com.yunhualian.BuildConfig;

public class CommonConstants {

    private CommonConstants() {

    }

    public static class HttpHeadConfig {

        public static final String TONCE = "Tonce";
        public static final String DEVICE = "Device";
        public static final String SIGN = "Sign";
        public static final String PLATFORM = "Platform";
        public static final String VERSIONCODE = "VersionCode";
        public static final String AUTHORIZATION = "Authorization";
        public static final String LANGUAGE = "Accept-Language";
        public static final String USERAGENT = "User-Agent";
        public static final String UUID = "uuid";

        public static final String UNVERIFY = "2013";
        public static final String UNLOGIN = "2015";
        public static final String UNLOGINV4 = "2016";
        public static final String UNACTIVE = "2017";
        public static final String UNREALNAME = "2018";
        public static final String UNPHONE = "2019";
        public static final String UNOTP = "2020";
        public static final String MAINTAIN = "503";
        public static final String UNTRADE = "1028";
        public static final String UNPURCHASE = "3001";
        public static final String TRADEPAIRCLOSE = "1021";
    }

    public static class PusherConfig {
        public static String Base_Scheme = (BuildConfig.DEBUG ? "push-otc." : "push-otc.");
        //        public static final String HOST = "52.79.46.240";
        public static String HOST = Base_Scheme + (BuildConfig.DEBUG ? "tatmas.mobi" : "tatmas.mobi");
        public static final int PORT = 443;
        public static final String API_KEY = "315f973507276m12w4892787336167k6";

    }

}
