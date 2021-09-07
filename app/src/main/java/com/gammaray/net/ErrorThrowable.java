package com.gammaray.net;


import com.gammaray.entity.BaseResponseVo;

/**
 * Synopsis     com.miner.client.net.ErrorThrowable
 * Author		Mosr
 * Version		1.0.0
 * Create 	    2020/7/12 16:48
 * Email  		intimatestranger@sina.cn
 */
public class ErrorThrowable extends Throwable {
    private BaseResponseVo errorReponse;

    public ErrorThrowable() {
    }

    public ErrorThrowable(BaseResponseVo errorReponse) {
        this.errorReponse = errorReponse;
    }

    public ErrorThrowable(BaseResponseVo errorReponse, String message) {
        super(message);
        this.errorReponse = errorReponse;
    }

    public BaseResponseVo getErrorReponse() {
        return errorReponse;
    }

    public ErrorThrowable setErrorReponse(BaseResponseVo errorReponse) {
        this.errorReponse = errorReponse;
        return this;
    }
}
