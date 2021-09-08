package com.gammaray.eth.entity;


import io.reactivex.Completable;
import io.reactivex.Single;

public interface TokenLocalSource {
    Completable put(NetworkInfo networkInfo, String walletAddress, TokenInfo tokenInfo);

    Completable del(NetworkInfo networkInfo, String walletAddress, TokenInfo tokenInfo);

    Single<TokenInfo[]> fetch(NetworkInfo networkInfo, String walletAddress);
}
