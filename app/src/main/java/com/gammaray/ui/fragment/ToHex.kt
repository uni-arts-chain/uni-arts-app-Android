package com.gammaray.ui.fragment

import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsic

fun toHex(submittableExtrinsic: EncodableStruct<SubmittableExtrinsic>): String {
    return SubmittableExtrinsic.toHexString(submittableExtrinsic)
}