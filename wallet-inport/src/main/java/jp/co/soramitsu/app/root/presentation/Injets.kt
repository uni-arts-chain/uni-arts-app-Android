package jp.co.soramitsu.app.root.presentation

import jp.co.soramitsu.app.root.di.RootApi
import jp.co.soramitsu.app.root.di.RootComponent
import jp.co.soramitsu.common.di.FeatureUtils

fun injets(vpActivity: VpActivity) {
    FeatureUtils.getFeature<RootComponent>(vpActivity, RootApi::class.java)
            .mainActivityComponentFactory()
            .create(vpActivity)
            .injets(vpActivity)
}