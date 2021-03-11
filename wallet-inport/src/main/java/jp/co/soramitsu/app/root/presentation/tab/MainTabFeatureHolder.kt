package jp.co.soramitsu.app.root.presentation.tab

import jp.co.soramitsu.common.di.FeatureApiHolder
import jp.co.soramitsu.common.di.FeatureContainer
import jp.co.soramitsu.common.di.scope.ApplicationScope
import jp.co.soramitsu.feature_account_api.di.AccountFeatureApi
import jp.co.soramitsu.feature_account_impl.presentation.AccountRouter
import jp.co.soramitsu.feature_wallet_api.domain.interfaces.WalletInteractor
import jp.co.soramitsu.splash.SplashRouter
import javax.inject.Inject

@ApplicationScope
class MainTabFeatureHolder @Inject constructor(
        featureContainer: FeatureContainer,
        private val splashRouter: AccountRouter,
) : FeatureApiHolder(featureContainer) {

    override fun initializeDependencies(): Any {
        val mainTabFeatureDependencies = DaggerMainTabFeatureComponent_MainTabFeatureDependenciesComponent.builder()
                .commonApi(commonApi())
                .accountFeatureApi(getFeature(AccountFeatureApi::class.java))
                .build()
        return DaggerMainTabFeatureComponent.builder()
                .withDependencies(mainTabFeatureDependencies)
                .router(splashRouter)
                .build()
    }
}