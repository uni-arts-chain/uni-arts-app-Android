package com.yunhualian.constant;


import com.yunhualian.BuildConfig;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Synopsis     ${SYNOPSIS}
 * Author		Mosr
 * Version		${VERSION}
 * Create 	    2020-06-19 20:55:49
 * Email  		intimatestranger@sina.cn
 */
public class AppConstant {
    public static final int REQ_PERM_CAMERA = 2;
    public static final int INTENT_EXTRA_KEY_QR_SCAN = 4;
    public static final String TOKEN_FAILURE = "10002";
    //接口地址
    private static final String IPADDRESS_RELEASE = "uniarts.senmeo.tech";
    private static final String IPADDRESS_DEBUG = "app.uniarts.me";

    //    public static final String ADDRESS = (BuildConfig.DEBUG ? "http" : "https") + "://" + (BuildConfig.DEBUG ? IPADDRESS_DEBUG : IPADDRESS_RELEASE);
    public static final String ADDRESS = (BuildConfig.DEBUG ? "https" : "https") + "://" + (BuildConfig.DEBUG ? IPADDRESS_RELEASE : IPADDRESS_RELEASE);

    // 服务器固定参数
    public static final String MD5_KEY = "h0r9,cf@5Ae;aB,0";
    public static final String HOME_CURRENT_ITEM_ID = "HOME_CURRENT_ITEM_ID";
    public static final String APP_CACHE_WEBVIEW_DIRNAME = "APP_CACHE_WEBVIEW_DIRNAME";
    public static final String UMENG_APP_KEY = "60a258b1c9aacd3bd4d7fe8d";

    public static final String URL_DOWNLOAD_ALICLOUD_APP = "http://otp.aliyun.com/m/";

    public static final long DEFAULT_CLICK_DURATION = 1000;
    public static final int REQ_QR_CODE = 1;
    public static int REQUEST_CODE_SCAN_GALLERY = 3;

    public static int getThemeColor() {
        return YunApplication.getInstance().getResources().getColor(R.color.colorAccent);
    }

    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL = new ThreadLocal<>();

    public static SimpleDateFormat getDefaultFormat() {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        }
        return simpleDateFormat;
    }

    private static final ThreadLocal<SimpleDateFormat> SDF_THREAD_LOCAL_ASSETS = new ThreadLocal<>();

    public static SimpleDateFormat getAssetsFormat() {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL_ASSETS.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("HH:mm MM/dd", Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        }
        return simpleDateFormat;
    }

    public static SimpleDateFormat getYYYYMMDDFormat() {
        SimpleDateFormat simpleDateFormat = SDF_THREAD_LOCAL_ASSETS.get();
        if (simpleDateFormat == null) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SDF_THREAD_LOCAL.set(simpleDateFormat);
        }
        return simpleDateFormat;
    }


    public static class DownLoadValues {
        public static boolean IsNeedDownLoad = false;
        public static boolean IsDownLoadFinish = true;
        public static boolean IsDownLoadDialogShow = true;

        public static String DownLoadVersion = "";
        public static String DownLoadUrl = "";
        public static String DownLoading = BuildConfig.APPLICATION_ID + ".downloading";
        public static String DownLoadSuccess = BuildConfig.APPLICATION_ID + ".downloadsuccess";
        public static String DownLoadError = BuildConfig.APPLICATION_ID + ".downloaderror";

        public static void reset() {
            IsNeedDownLoad = false;
            IsDownLoadFinish = true;
            IsDownLoadDialogShow = true;

            DownLoadVersion = "";
            DownLoadUrl = "";
        }
    }

    /**
     * signup, sigin, change_pwd, forget_pwd, bind_phone, change_email, set_pay_pwd, bind_otp, forget_pay_pwd
     * signup, sigin, change_pwd, forget_pwd, bind_email, change_phone, set_pay_pwd
     * <p>
     * SIGNUP, SIGIN, CHANGE_PWD, FORGET_PWD, BIND_PHONE, CHANGE_EMAIL, SET_PAY_PWD, BIND_OTP, FORGET_PAY_PWD
     * SIGNUP, SIGIN, CHANGE_PWD, FORGET_PWD, BIND_EMAIL, CHANGE_PHONE, SET_PAY_PWD
     */
    public static class CaptchaType {
        public static final String TYPE_SIGNUP = "signup";
        public static final String TYPE_CHANGE_PWD = "change_pwd";
        public static final String TYPE_FORGET_PWD = "forget_pwd";
        public static final String TYPE_BIND_PHONE = "bind_phone";
        public static final String TYPE_CHANGE_EMAIL = "change_email";
        public static final String TYPE_SET_PAY_PWD = "set_pay_pwd";
        public static final String TYPE_BIND_OTP = "bind_otp";
        public static final String TYPE_FORGET_PAY_PWD = "forget_pay_pwd";
        public static final String TYPE_BIND_EMAIL = "bind_email";
        public static final String TYPE_CHANGE_PHONE = "change_phone";
    }

    public static class NewsType {
        public static final String TYPE_NEW = "New::New";
        public static final String TYPE_ANNOUNCEMENT = "New::Announcement";
    }

    public static class FiatCurrencyType {
        public static final String TYPE_CNY = "cny";
        public static final String TYPE_USD = "usd";
    }

    public static class HistoryType {
        public static final int DEPOSIT = 0;
        public static final int WITHDRAW = 1;
        public static final int TRANSFER = 2;
        public static final int AWARDS_TRANSFER = 3;
        public static final int FUNDS_AWARD = 4;
        public static final int ALL = 5;
    }

    public static class Currency {
        public static final String PAY_CURRENCY = "TMS";
        public static final String OUT_CURRENCY = "QTS";
        public static final String HUA_CURRENCY = "HUA";
    }

    public static class TokenType {
        public static final String SMS = "sms";
        public static final String EMAIL = "email";
    }


}
