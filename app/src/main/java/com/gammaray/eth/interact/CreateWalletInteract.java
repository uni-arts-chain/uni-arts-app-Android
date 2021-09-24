package com.gammaray.eth.interact;

import android.util.Log;

import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.util.ETHWalletUtils;
import com.gammaray.eth.util.WalletDaoUtils;

import java.util.Arrays;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class CreateWalletInteract {


    public CreateWalletInteract() {
    }

    public Single<ETHWallet> create(final String name, final String pwd, String confirmPwd, String pwdReminder, String symble) {
        return Single.fromCallable(() -> {
            ETHWallet ethWallet = ETHWalletUtils.generateMnemonic(name, pwd, symble);
            ethWallet.setMultipleCoins(true);
            ethWallet.setSymble(symble);
            WalletDaoUtils.insertNewWallet(ethWallet);
            return ethWallet;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Single<ETHWallet> loadWalletByKeystore(final String walletName,final String keystore, final String pwd, boolean isMultiple, String symble) {
        return Single.fromCallable(() -> {
            ETHWallet ethWallet = ETHWalletUtils.loadWalletByKeystore(walletName,keystore, pwd);

            if (ethWallet != null) {
                ethWallet.setMultipleCoins(isMultiple);
                ethWallet.setSymble(symble);
                WalletDaoUtils.insertNewWallet(ethWallet);
            }

            return ethWallet;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ETHWallet> loadWalletByPrivateKey(final String walletName,final String privateKey, final String pwd, boolean isMultiple, String symble) {
        return Single.fromCallable(() -> {

                    ETHWallet ethWallet = ETHWalletUtils.loadWalletByPrivateKey(walletName,privateKey, pwd);

                    if (ethWallet != null) {
                        ethWallet.setMultipleCoins(isMultiple);
                        ethWallet.setSymble(symble);
                        WalletDaoUtils.insertNewWallet(ethWallet);
                    }
                    return ethWallet;
                }
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    public Single<ETHWallet> loadWalletByMnemonic(final String walletName,final String bipPath, final String mnemonic, final String pwd, boolean isMultiple) {
        return Single.fromCallable(() -> {
            ETHWallet ethWallet = ETHWalletUtils.importMnemonic(walletName,bipPath
                    , Arrays.asList(mnemonic.split(" ")), pwd);

            if (ethWallet != null) {
                String symble = bipPath.equalsIgnoreCase(ETHWalletUtils.ETH_CUSTOM_TYPE2) ? "QTS" : "ETH";
                ethWallet.setMultipleCoins(isMultiple);
                ethWallet.setSymble(symble);
                WalletDaoUtils.insertNewWallet(ethWallet);
            }
            return ethWallet;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
