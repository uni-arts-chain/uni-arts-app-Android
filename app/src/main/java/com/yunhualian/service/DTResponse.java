package com.yunhualian.service;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by zhangzhongqiang on 2015/7/29.
 */
public class DTResponse<V> implements Serializable {

    // 响应数据头
    private Head head;
    // 响应数据体
    private V body;

    public DTResponse() {
        super();
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public V getBody() {
        return body;
    }

    public void setBody(V body) {
        this.body = body;
    }

    public String getBodyToString(){
        return new Gson().toJson(body);
    }
}
