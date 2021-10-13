package com.gammaray.eth.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gammaray.base.YunApplication;
import com.gammaray.eth.interact.CreateTransactionInteract;
import com.gammaray.eth.interact.FetchGasSettingsInteract;
import com.gammaray.eth.interact.FetchWalletInteract;
import com.gammaray.eth.repository.EthereumNetworkRepository;
import com.gammaray.eth.repository.RepositoryFactory;


public class ConfirmationViewModelFactory implements ViewModelProvider.Factory {

    private final EthereumNetworkRepository ethereumNetworkRepository;
    private FetchWalletInteract findDefaultWalletInteract;
    private FetchGasSettingsInteract fetchGasSettingsInteract;
    private CreateTransactionInteract createTransactionInteract;

    public ConfirmationViewModelFactory() {
        RepositoryFactory rf = YunApplication.repositoryFactory();

        this.ethereumNetworkRepository = rf.ethereumNetworkRepository;
        this.findDefaultWalletInteract = new FetchWalletInteract();
        this.fetchGasSettingsInteract = new FetchGasSettingsInteract(YunApplication.sp, ethereumNetworkRepository);
        this.createTransactionInteract = new CreateTransactionInteract(ethereumNetworkRepository);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ConfirmationViewModel(ethereumNetworkRepository, findDefaultWalletInteract, fetchGasSettingsInteract , createTransactionInteract);
    }
}
