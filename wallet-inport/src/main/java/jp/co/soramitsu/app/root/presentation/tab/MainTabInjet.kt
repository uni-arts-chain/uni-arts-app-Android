package jp.co.soramitsu.app.root.presentation.tab

import android.content.Context
import androidx.fragment.app.Fragment
import dagger.BindsInstance
import dagger.Subcomponent
import jp.co.soramitsu.common.di.FeatureUtils
import jp.co.soramitsu.common.di.scope.ScreenScope
import jp.co.soramitsu.splash.di.SplashFeatureApi
import jp.co.soramitsu.splash.di.SplashFeatureComponent

fun inject(context: Context, fragment: MainTabFragment) {
    FeatureUtils.getFeature<MainTabFeatureComponent>(context, MainTabFeatureApi::class.java)
            .mainTabComponentFactory()
            .create(fragment)
            .inject(fragment)
}