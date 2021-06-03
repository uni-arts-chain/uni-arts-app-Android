package com.yunhualian.entity;

/**
 * 返回信息
 *
 * @author yinsh
 */
public class ProtocolResultMsg {


    /**
     * head : {"code":1070,"msg":"签名无效","detail":""}
     * body : {}
     */

    private HeadBean head;
    private BodyBean body;

    public HeadBean getHead() {
        return head;
    }

    public void setHead(HeadBean head) {
        this.head = head;
    }

    public BodyBean getBody() {
        return body;
    }

    public void setBody(BodyBean body) {
        this.body = body;
    }

    public static class HeadBean {
        /**
         * code : 1070
         * msg : 签名无效
         * detail :
         */

        private int code;
        private String msg;
        private String detail;

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

        public String getDetail() {
            return detail;
        }

        public void setDetail(String detail) {
            this.detail = detail;
        }
    }

    public static class BodyBean {
    }
}
