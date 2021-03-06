package jp.co.soramitsu.feature_account_impl.presentation.view.advanced.encryption.model

import jp.co.soramitsu.feature_account_api.domain.model.CryptoType

data class CryptoTypeSelectedModel(
    val name: String,
    val cryptoType: CryptoType
)