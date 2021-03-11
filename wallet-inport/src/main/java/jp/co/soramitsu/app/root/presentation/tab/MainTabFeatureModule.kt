package jp.co.soramitsu.app.root.presentation.tab

import dagger.Module
import dagger.Provides
import jp.co.soramitsu.common.di.scope.FeatureScope
import jp.co.soramitsu.common.interfaces.FileProvider
import jp.co.soramitsu.feature_account_api.domain.interfaces.AccountRepository
import jp.co.soramitsu.feature_wallet_api.domain.interfaces.WalletInteractor
import jp.co.soramitsu.feature_wallet_api.domain.interfaces.WalletRepository
import jp.co.soramitsu.feature_wallet_impl.domain.WalletInteractorImpl

@Module
class MainTabFeatureModule{
//    @Provides
//    @FeatureScope
//    fun provideWalletInteractor(
//            walletRepository: WalletRepository,
//            accountRepository: AccountRepository,
//            fileProvider: FileProvider
//    ): WalletInteractor = WalletInteractorImpl(walletRepository, accountRepository, fileProvider)

}