package com.gammaray.eth.interact;


import com.gammaray.eth.entity.NetworkInfo;
import com.gammaray.eth.entity.TokenRepositoryType;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class AddTokenInteract {
    private final TokenRepositoryType tokenRepository;
    private final FetchWalletInteract findDefaultWalletInteract;

    public AddTokenInteract(
            FetchWalletInteract findDefaultWalletInteract, TokenRepositoryType tokenRepository) {
        this.findDefaultWalletInteract = findDefaultWalletInteract;
        this.tokenRepository = tokenRepository;
    }

    public Completable add(String name, String address, String symbol, int decimals, NetworkInfo server, String icon) {
        return findDefaultWalletInteract
                .findDefault()
                .flatMapCompletable(wallet -> tokenRepository.addToken(name, wallet.address, address, symbol, decimals, server, icon)
                        .observeOn(AndroidSchedulers.mainThread()));
    }

    public Completable del(String address, String symbol, int decimals, String icon) {
        return findDefaultWalletInteract
                .findDefault()
                .flatMapCompletable(wallet -> tokenRepository
                        .delToken(wallet.address, address, symbol, decimals, icon)
                        .observeOn(AndroidSchedulers.mainThread()));
    }

//    public Completable update(String name, String address, String symbol, int decimals, NetworkInfo server, String icon,boolean isAdd) {
//        return findDefaultWalletInteract
//                .findDefault()
//                .flatMapCompletable(wallet -> tokenRepository
//                        .updateTokenInfoCache(name, wallet.address, address, symbol, decimals, server, icon, isAdd)
//                        .observeOn(AndroidSchedulers.mainThread()));
//    }
}
