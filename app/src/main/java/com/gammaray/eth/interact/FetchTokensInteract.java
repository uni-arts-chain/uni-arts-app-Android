package com.gammaray.eth.interact;


import com.gammaray.eth.entity.Token;
import com.gammaray.eth.entity.TokenRepositoryType;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class FetchTokensInteract {

    private final TokenRepositoryType tokenRepository;

    public FetchTokensInteract(TokenRepositoryType tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Observable<Token[]> fetch(String walletAddress) {
        return tokenRepository.fetch(walletAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public BigDecimal getBlockNum() throws ExecutionException, InterruptedException {
        return tokenRepository.getBlockNum();

    }
}
