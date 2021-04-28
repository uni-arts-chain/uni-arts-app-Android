package com.yunhualian.service;

import java.io.Serializable;

/**
 * 响应数据头
 * Created by zhangzhongqiang on 2015/7/29.
 */
public class Head implements Serializable {

    // 返回值代码
    private String code = "";
    // 返回值详情
    private String msg;
    // 是否成功
    private boolean isSuccess;

    public boolean isSuccess() {
        return code.equals("1000");
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
