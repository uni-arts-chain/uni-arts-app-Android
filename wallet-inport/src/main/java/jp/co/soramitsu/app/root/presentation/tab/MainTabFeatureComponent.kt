package jp.co.soramitsu.app.root.presentation.tab

import dagger.BindsInstance
import dagger.Component
import jp.co.soramitsu.common.di.CommonApi
import jp.co.soramitsu.common.di.scope.FeatureScope
import jp.co.soramitsu.feature_account_api.di.AccountFeatureApi
import jp.co.soramitsu.feature_account_impl.presentation.AccountRouter
import jp.co.soramitsu.splash.SplashRouter
import jp.co.soramitsu.splash.presentation.di.SplashComponent

@Component(
        dependencies = [
            MainTabFeatureDependencies::class
        ],
        modules = [
            MainTabFeatureModule::class
        ]
)
@FeatureScope
interface MainTabFeatureComponent : MainTabFeatureApi {

    fun mainTabComponentFactory(): MainTabComponent.Factory

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun router(splashRouter: AccountRouter): Builder

        fun withDependencies(deps: MainTabFeatureDependencies): Builder

        fun build(): MainTabFeatureComponent
    }

    @Component(
            dependencies = [
                CommonApi::class,
                AccountFeatureApi::class
            ]
    )
    interface MainTabFeatureDependenciesComponent : MainTabFeatureDependencies
}