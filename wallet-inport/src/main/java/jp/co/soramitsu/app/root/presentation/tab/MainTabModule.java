package jp.co.soramitsu.app.root.presentation.tab;

import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import jp.co.soramitsu.common.di.viewmodel.ViewModelKey;
import jp.co.soramitsu.common.di.viewmodel.ViewModelModule;
import jp.co.soramitsu.feature_account_impl.presentation.AccountRouter;
import jp.co.soramitsu.feature_wallet_api.domain.interfaces.WalletInteractor;
import jp.co.soramitsu.splash.SplashRouter;

@Module(includes = ViewModelModule.class)
public class MainTabModule {
    @Provides
    @IntoMap
    @ViewModelKey(MainTabViewModel.class)
    public MainTabViewModel provideSignInViewModel(AccountRouter router) {
        return new MainTabViewModel(router);
    }
}
