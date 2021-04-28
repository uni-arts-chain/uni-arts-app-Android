package com.yunhualian.net;

import android.text.TextUtils;

import com.blankj.utilcode.util.ActivityUtils;
import com.google.gson.Gson;
import com.yunhualian.utils.ErrorHelper;
import com.yunhualian.utils.IGenericsSerializator;
import com.yunhualian.utils.IView;
import com.zhy.http.okhttp.callback.Callback;

import java.lang.reflect.ParameterizedType;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by JimGong on 2016/6/23.
 */

public abstract class MvpNetCallback<T> extends Callback<T> implements IGenericsSerializator {

    private boolean showLoading;
    private String msg = "";
    private IView iView;

    public MvpNetCallback(IView view) {
        this(view, false);
    }

    public MvpNetCallback(IView view, String msg) {
        this(view, true, msg);
    }

    public MvpNetCallback(IView iView, boolean showLoading) {
        this.iView = iView;
        this.showLoading = showLoading;
    }

    public MvpNetCallback(IView iView, boolean showLoading, String msg) {
        this.iView = iView;
        this.showLoading = showLoading;
        this.msg = msg;
    }

    @Override
    public <T> T transform(String response, Class<T> classOfT) throws Exception {
        Gson mGson = new Gson();
        String jsonString = response;
        return mGson.fromJson(jsonString, classOfT);
    }


    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        String string = response.body().string();
        Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if (entityClass == String.class) {
            return (T) string;
        }
        try {
            T bean = this.transform(string, entityClass);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBefore(Request request, int id) {
        if (showLoading) {
            if (TextUtils.isEmpty(msg) && iView != null) {
                iView.showLoading();
            } else if (iView != null) {
                iView.showLoading(msg);
            }
        }
        super.onBefore(request, id);
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        if (showLoading && iView != null) {
            iView.dismissLoading();
        }
        ErrorHelper.getMessage(id, e.getMessage(), ActivityUtils.getTopActivity());
    }

    @Override
    public void onResponse(T response, int id) {
        if (showLoading && iView != null) {
            iView.dismissLoading();
        }
    }
}
