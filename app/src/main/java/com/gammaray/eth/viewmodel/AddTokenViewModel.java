package com.gammaray.eth.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.gammaray.eth.base.BaseViewModel;
import com.gammaray.eth.entity.NetworkInfo;
import com.gammaray.eth.interact.AddTokenInteract;
import com.gammaray.eth.interact.FetchWalletInteract;

public class AddTokenViewModel extends BaseViewModel {

    private final AddTokenInteract addTokenInteract;
    private final FetchWalletInteract findDefaultWalletInteract;

    private final MutableLiveData<Boolean> result = new MutableLiveData<>();

    AddTokenViewModel(
            AddTokenInteract addTokenInteract,
            FetchWalletInteract findDefaultWalletInteract
    ) {
        this.addTokenInteract = addTokenInteract;
        this.findDefaultWalletInteract = findDefaultWalletInteract;
    }

    //0xAE98b4B3Eaf3537374b0784294868ED21db6328F
    public void save(String name, String address, String symbol, int decimals, NetworkInfo server, String icon) {
        addTokenInteract
                .add(name, address, symbol, decimals, server, icon)
                .subscribe(this::onSaved, this::onError);
    }

    public void del(String address, String symbol, int decimals, String icon) {
        addTokenInteract
                .del(address, symbol, decimals, icon)
                .subscribe(this::onSaved, this::onError);
    }

//    public void update(String name, String address, String symbol, int decimals, NetworkInfo server, String icon, boolean isAdd) {
//        addTokenInteract
//                .update(name, address, symbol, decimals, server, icon, isAdd)
//                .subscribe(this::onSaved, this::onError);
//    }

    private void onSaved() {
        progress.postValue(false);
        result.postValue(true);
    }

    public LiveData<Boolean> result() {
        return result;
    }

    public void showTokens(Context context) {


    }
}
