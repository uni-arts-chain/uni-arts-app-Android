package com.yunhualian.ui.fragment

import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsic
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsicV28

fun toHex(submittableExtrinsic: EncodableStruct<SubmittableExtrinsicV28>): String {
    return SubmittableExtrinsicV28.toHexString(submittableExtrinsic)
}