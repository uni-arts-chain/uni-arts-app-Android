package jp.co.soramitsu.app.root.presentation.tab;

import javax.inject.Inject;

import jp.co.soramitsu.common.base.BaseViewModel;
import jp.co.soramitsu.feature_account_impl.presentation.AccountRouter;
import jp.co.soramitsu.feature_wallet_api.domain.interfaces.WalletInteractor;

public class MainTabViewModel extends BaseViewModel {
    private AccountRouter router;
    @Inject
    public MainTabViewModel(AccountRouter router) {
        this.router = router;
    }

    public void goMain() {
        router.openMain();
    }
}
