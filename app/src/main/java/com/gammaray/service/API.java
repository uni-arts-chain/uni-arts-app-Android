package com.gammaray.service;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;

import retrofit2.Call;


/**
 * Created by Yanmin on 2016/3/15.
 */

public abstract class API<T> {

    private String _name;
    private String _url;
    private TypeToken token;
    private boolean _responseIsJson;

    public API(String name, String url, boolean responseIsJson) {
        this._name = name;
        this._url = url;
        this._responseIsJson = responseIsJson;
        Type superclass = getClass().getGenericSuperclass();
        ParameterizedType parameterized = (ParameterizedType) superclass;
        Type type = parameterized.getActualTypeArguments()[0];

        token = TypeToken.get(type);
    }

    public API(String name) {
        this(name, "", true);
    }

    public API(String name, String url) {
        this(name, url, true);
    }

    public abstract Call request(HashMap<String, String> params);

    public Type getEntryType() {
        return token.getType();
    }

    public void setToken(TypeToken token) {
        this.token = token;
    }

    public String getName() {
        return _name;
    }

    public String getUrl() {
        return _url;
    }

    public void set_url(String _url) {
        this._url = _url;
    }

    public boolean isResponseJson() {
        return _responseIsJson;
    }
}
