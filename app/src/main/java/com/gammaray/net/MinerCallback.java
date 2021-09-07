package com.gammaray.net;

import retrofit2.Call;

/**
 * Synopsis     com.miner.client.net.MinerCallback
 * Author		Mosr
 * Version		1.0.0
 * Create 	    2020-06-19 20:55:49
 * Email  		intimatestranger@sina.cn
 */
public interface MinerCallback<T> {
    void onSuccess(Call<T> call, retrofit2.Response<T> response);

    void onError(Call<T> call, retrofit2.Response<T> response);

    void onFailure(Call<?> call, Throwable t);
}
