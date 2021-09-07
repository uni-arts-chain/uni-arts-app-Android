package com.gammaray.entity;

import java.io.Serializable;

/**
 * Synopsis     com.miner.client.entity.BaseResponseVo
 * Author		Mosr
 * Version		1.0.0
 * Create 	    2020-06-19 20:55:49
 * Email  		intimatestranger@sina.cn
 */
public class BaseResponseVo<T> implements Serializable {
    private HeadBean head;
    private T body;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public boolean isSuccessful() {
        if (null != head) {
            return head.getCode() == 1000;
        }
        return false;
    }

    public static class HeadBean implements Serializable {

        private int code;
        private String msg;
        private String total_count;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getTotal_count() {
            return total_count;
        }

        public void setTotal_count(String total_count) {
            this.total_count = total_count;
        }

    }

    public HeadBean getHead() {
        return head;
    }

    public void setHead(HeadBean head) {
        this.head = head;
    }

}
