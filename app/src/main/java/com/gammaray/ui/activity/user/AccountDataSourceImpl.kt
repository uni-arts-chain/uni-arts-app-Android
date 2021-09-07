package com.gammaray.ui.activity.user

import jp.co.soramitsu.fearless_utils.scale.Schema
import jp.co.soramitsu.fearless_utils.scale.byteArray
import jp.co.soramitsu.fearless_utils.scale.string

object SourceInternal : Schema<SourceInternal>() {
    val Type by string()

    val PrivateKey by byteArray()
    val PublicKey by byteArray()

    val Nonce by byteArray().optional()

    val Seed by byteArray().optional()
    val Mnemonic by string().optional()

    val DerivationPath by string().optional()
}

