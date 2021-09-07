package com.gammaray.eth.entity;


import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface TokenRepositoryType {

    Observable<Token[]> fetch(String walletAddress);

    Completable addToken(String name, String walletAddress, String address, String symbol, int decimals, NetworkInfo server, String icon);

//    Completable updateTokenInfoCache(String name, String walletAddress, String address, String symbol, int decimals, NetworkInfo server, String icon, boolean isAdd);

    Completable delToken(String walletAddress, String address, String symbol, int decimals, String icon);

    BigDecimal getBlockNum() throws ExecutionException, InterruptedException;

}
