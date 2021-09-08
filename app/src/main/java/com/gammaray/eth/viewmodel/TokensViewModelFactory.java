package com.gammaray.eth.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.gammaray.base.YunApplication;
import com.gammaray.eth.interact.FetchTokensInteract;
import com.gammaray.eth.interact.FetchWalletInteract;
import com.gammaray.eth.repository.EthereumNetworkRepository;
import com.gammaray.eth.repository.RepositoryFactory;

public class TokensViewModelFactory implements ViewModelProvider.Factory {

    private final FetchTokensInteract fetchTokensInteract;
    private final EthereumNetworkRepository ethereumNetworkRepository;

    public TokensViewModelFactory() {

        RepositoryFactory rf = YunApplication.repositoryFactory();
        fetchTokensInteract = new FetchTokensInteract(rf.tokenRepository);
        ethereumNetworkRepository = rf.ethereumNetworkRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new TokensViewModel(
                ethereumNetworkRepository,
                new FetchWalletInteract(),
                fetchTokensInteract
                );
    }
}
