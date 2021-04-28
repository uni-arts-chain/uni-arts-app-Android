package com.yunhualian.net;

import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public interface HttpRequestInterf {
    void request(HashMap<String, String> params, String url, String header, Callback callback);

    void requestFile(Map<String, Object> params, String keyname, String url, File file, String header, Callback callback);

    void requestGet(Map<String, Object> params, String url, String header, Callback callback);

    void cancel();
}
