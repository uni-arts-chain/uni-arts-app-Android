package com.yunhualian.net;

/**
 * Created by gengda on 2018/4/9.
 */

public class SuccessVo {


    /**
     * success : {"message":"Success","code":1000}
     */

    private SuccessBean success;

    public SuccessBean getSuccess() {
        return success;
    }

    public void setSuccess(SuccessBean success) {
        this.success = success;
    }

    public static class SuccessBean {
        /**
         * message : Success
         * code : 1000
         */

        private String message;
        private String code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
