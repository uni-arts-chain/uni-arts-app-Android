package com.yunhualian.net;

import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.gson.Gson;
import com.yunhualian.utils.DateUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;

public class HttpRequestUtils implements HttpRequestInterf {

    private static HttpRequestUtils INSTANCE = new HttpRequestUtils();

    private HttpRequestUtils() {

    }

    public static synchronized HttpRequestUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HttpRequestUtils();
        }
        return INSTANCE;
    }

    /**
     * @param params
     * @param url
     * @param callback
     */
    @Override
    public void request(HashMap<String, String> params, String url, String header, Callback callback) {
        String currentTime = DateUtil.getCurrentSecondTime();

        OkHttpUtils.postString().tag(ActivityUtils.getTopActivity())
                .url(url)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .content(new Gson().toJson(HttpTools.Params(params)))
                .addHeader("Tonce", currentTime)
                .addHeader("Sign", HttpTools.SigInfo(params, currentTime, url, "POST"))
                .addHeader("Authorization", TextUtils.isEmpty(header) ? "Basic dXBiZXN0OnVwYmVzdA==" : header)
                .build().execute(callback);
    }

    @Override
    public void requestFile(Map<String, Object> params, String keyname, String url, File file, String header, Callback callback) {
        OkHttpUtils.post().tag(ActivityUtils.getTopActivity()).params(HttpTools.Params(params))
                .url(url).addFile(keyname, file.getName(), file)
                .addHeader("Authorization", TextUtils.isEmpty(header) ? "Basic dXBiZXN0OnVwYmVzdA==" : header)
                .build().execute(callback);
    }

    @Override
    public void requestGet(Map<String, Object> params, String url, String header, Callback callback) {
        OkHttpUtils.get().tag(ActivityUtils.getTopActivity()).url(url)
                .params(HttpTools.Params(params))
                .addHeader("Authorization", TextUtils.isEmpty(header) ? "Basic dXBiZXN0OnVwYmVzdA==" : header)
                .build().execute(callback);
    }


    @Override
    public void cancel() {
        try {
            OkHttpUtils.getInstance().cancelTag(ActivityUtils.getTopActivity());
            finishInstance();
        } catch (Exception e) {
        }
    }

    public static void finishInstance() {
        INSTANCE = null;
    }
}
