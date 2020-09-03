package com.yunhualian.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.yunhualian.base.YunApplication;

import java.util.HashMap;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.Request;

public class RemoteParametersManager {
    private static String mLocalLanguage = "";
    private static String mLocalCountry = "";
    private static volatile RemoteParametersManager mRemoteParametersManager;

    public static RemoteParametersManager getInstance() {
        if (null == mRemoteParametersManager) {
            synchronized (RemoteParametersManager.class) {
                if (null == mRemoteParametersManager) {
                    mRemoteParametersManager = new RemoteParametersManager();
                }
            }
        }
        return mRemoteParametersManager;
    }

    public static String getmLocalLanguage() {
        if (TextUtils.isEmpty(mLocalLanguage))
            mLocalLanguage = Locale.getDefault().getLanguage() + "-" + getmLocalCountry();
        return mLocalLanguage;
    }

    public static String getmLocalCountry() {
        if (TextUtils.isEmpty(mLocalCountry))
            mLocalCountry = Locale.getDefault().getCountry();
        return mLocalCountry;
    }


    public String disposeParameters(Gson mGson, HashMap<String, Object> mContentMap) {
        /*String reqTime = TimeUtils.millis2String(System.currentTimeMillis(), AppConstant.getDefaultFormat());
        mContentMap.put("reqTime", reqTime);*/
        return null != mContentMap ? mGson.toJson(mContentMap) : null;
    }

    public FormBody disposeParameters(FormBody mFormBody) {
        FormBody.Builder nFormBody = new FormBody.Builder();
        for (int i = 0; i < mFormBody.size(); i++) {
            nFormBody.add(mFormBody.name(i), mFormBody.value(i));
        }
        String mToken;
        if (!TextUtils.isEmpty(mToken = YunApplication.getmUserVo(false).getToken()))
            nFormBody.add("token", mToken);
        return nFormBody.build();
    }

    public void disposeHeader(Request.Builder mRequestBuilder) {
        mRequestBuilder.header("Accept-Language", getmLocalLanguage());
        String mToken;
        if (!TextUtils.isEmpty(mToken = YunApplication.getmUserVo(false).getToken())) {
            mRequestBuilder.header("Authorization", mToken);
        }
    }
}
