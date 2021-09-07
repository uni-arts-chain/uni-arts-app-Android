package com.gammaray.net;


import com.blankj.utilcode.util.AppUtils;
import com.gammaray.utils.UuidUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 添加请求头
 */
public class HeaderIntercepter implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request().newBuilder()
                .addHeader("ver", String.valueOf(AppUtils.getAppVersionCode()))
                .addHeader("uuid", UuidUtils.getUnqueUUid())
                .addHeader("device", "android")//1 是安卓
                .build();
        return chain.proceed(request);
    }
}
