package com.gammaray.eth.repository;


import com.gammaray.eth.entity.NetworkInfo;
import com.gammaray.eth.entity.RealmTokenInfo;
import com.gammaray.eth.entity.TokenInfo;
import com.gammaray.eth.entity.TokenLocalSource;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class RealmTokenSource implements TokenLocalSource {

    @Override
    public Completable put(NetworkInfo networkInfo, String walletAddress, TokenInfo tokenInfo) {
        return Completable.fromAction(() -> putInNeed(networkInfo, walletAddress, tokenInfo));
    }

    @Override
    public Completable del(NetworkInfo networkInfo, String walletAddress, TokenInfo tokenInfo) {
        return Completable.fromAction(() -> delInNeed(networkInfo, walletAddress, tokenInfo));
    }

    @Override
    public Single<TokenInfo[]> fetch(NetworkInfo networkInfo, String walletAddress) {
        return Single.fromCallable(() -> {
            Realm realm = null;
            int preIndex = 0;
            try {
                realm = getRealmInstance(networkInfo, walletAddress);
                RealmResults<RealmTokenInfo> realmItems = realm.where(RealmTokenInfo.class)
                        .sort("addedTime", Sort.ASCENDING)
                        .findAll();
                int len = realmItems.size();
//                if (len == 0 && MainActivity.firstAccount) {
//                    preIndex = 2;
//                }
                TokenInfo[] result = new TokenInfo[len + preIndex];
                if (preIndex > 0)
                    for (int i = 0; i < preIndex; i++) {
                        result[i] = QtsTokenInforRepository.init().getPerTokens(i).tokenInfo;
                    }

                for (int i = 0; i < len; i++) {
                    RealmTokenInfo realmItem = realmItems.get(i);
                    if (realmItem != null) {
                        result[i + preIndex] = new TokenInfo(
                                realmItem.getAddress(),
                                realmItem.getName(),
                                realmItem.getSymbol(),
                                realmItem.getDecimals(),
                                realmItem.getRpcServer(), realmItem.getIcon());
                    }
                }
                return result;
            } finally {
                if (realm != null) {
                    realm.close();
                }
            }
        });
    }

    private Realm getRealmInstance(NetworkInfo networkInfo, String walletAddress) {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(walletAddress + "-" + networkInfo.name + ".realm")
                .schemaVersion(1)
                .build();
        return Realm.getInstance(config);
    }

    /*RealmTokenInfo =
     proxy[{address:0x46c5567eB6D20bAf86dD05c4947188d13666B0D3},
       {name:Elf Pool Coin},
       {symbol:EPC},
       {decimals:6},
       {addedTime:0},
       {icon:https://tatmas.vip/icons/epc.png},
       {rpcServer:https://ropsten.infura.io/v3/8ddd215139c849559864f7aaf7097307}]*/
    private void putInNeed(NetworkInfo networkInfo, String walletAddress, TokenInfo tokenInfo) {
        Realm realm = null;
        try {
            realm = getRealmInstance(networkInfo, walletAddress);
            RealmTokenInfo realmTokenInfo = realm.where(RealmTokenInfo.class)
                    .equalTo("address", tokenInfo.address)
                    .findFirst();
            if (realmTokenInfo == null) {
                realm.executeTransaction(r -> {
                    RealmTokenInfo obj = r.createObject(RealmTokenInfo.class, tokenInfo.address);
                    obj.setName(tokenInfo.name);
                    obj.setSymbol(tokenInfo.symbol);
                    obj.setDecimals(tokenInfo.decimals);
                    obj.setRpcServer(tokenInfo.server);
                    obj.setIcon(tokenInfo.icon);
                    obj.setAddedTime(System.currentTimeMillis());
                });
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private void delInNeed(NetworkInfo networkInfo, String walletAddress, TokenInfo tokenInfo) {
        Realm realm = null;
        try {
            realm = getRealmInstance(networkInfo, walletAddress);
            RealmTokenInfo realmTokenInfo = realm.where(RealmTokenInfo.class)
                    .equalTo("address", tokenInfo.address)
                    .findFirst();
            if (realmTokenInfo != null) {
                realm.executeTransaction(r -> {
                    realmTokenInfo.deleteFromRealm();
                });
            }
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

}
