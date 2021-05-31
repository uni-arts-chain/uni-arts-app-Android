package com.yunhualian.service;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.lljjcoder.Constant;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.entity.ProtocolResultMsg;
import com.yunhualian.utils.LoginOutEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by win on 2017/6/5 0005.
 */

public class TokenInterceptor implements Interceptor {
    private Gson mGson = new Gson();
    private ProtocolResultMsg ss = null;

    public TokenInterceptor() {
    }

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        ss = null;
        Request request = chain.request();
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        try {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            String bodyString = buffer.clone().readString(charset);
            String jsonString = bodyString;
            if (!TextUtils.isEmpty(jsonString) && response.code() == 200) {
                ss = mGson.fromJson(jsonString, ProtocolResultMsg.class);
            }
        } catch (Exception e) {
        }

        if (ss != null && isTokenExpired(ss.getCode())) {//根据和服务端的约定判断token过期
            LoginOut();
        }
        return response;
    }

    /**
     * 根据Response，判断Token是否失效
     *
     * @param code
     * @return
     */
    private boolean isTokenExpired(String code) {
        try {
            if (!TextUtils.isEmpty(code) && (code.equals(AppConstant.TOKEN_FAILURE) || code.equals(AppConstant.TOKEN_MISS))) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 退出登录跳转到登录页
     */
    public void LoginOut() {
        try {
            EventBus.getDefault().post(new EventBusMessageEvent(EventEntity.EVENT_REFRESH_TOKEN, ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
