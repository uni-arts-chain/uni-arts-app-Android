package com.yunhualian.net;

/**
 * Created by gengda on 2018/4/9.
 */

public class ErrorVo {


    /**
     * error : {"code":1001,"message":"market is missing, market does not have a valid value"}
     */

    private ErrorBean error;

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public static class ErrorBean {
        /**
         * code : 1001
         * message : market is missing, market does not have a valid value
         */

        private String code;
        private String message;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
