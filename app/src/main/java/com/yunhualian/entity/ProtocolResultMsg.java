package com.yunhualian.entity;

/**
 * 返回信息
 *
 * @author yinsh
 */
public class ProtocolResultMsg {

    private String code;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "ProtocolResultMsg{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
