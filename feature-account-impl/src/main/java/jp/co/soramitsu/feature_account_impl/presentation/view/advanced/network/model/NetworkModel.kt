package jp.co.soramitsu.feature_account_impl.presentation.view.advanced.network.model

import jp.co.soramitsu.feature_account_api.domain.model.Node
import jp.co.soramitsu.feature_account_api.domain.model.Node.NetworkType.POLKADOT
import jp.co.soramitsu.feature_account_impl.R

data class NetworkModel(
    val name: String,
    val networkTypeUI: NetworkTypeUI
) {
    sealed class NetworkTypeUI(val icon: Int, val networkType: Node.NetworkType) {
        object Polkadot : NetworkTypeUI(R.drawable.ic_polkadot_24, POLKADOT)
//        object Kusama : NetworkTypeUI(R.drawable.ic_ksm_24, KUSAMA)
//        object Westend : NetworkTypeUI(R.drawable.ic_westend_24, WESTEND)
    }
}