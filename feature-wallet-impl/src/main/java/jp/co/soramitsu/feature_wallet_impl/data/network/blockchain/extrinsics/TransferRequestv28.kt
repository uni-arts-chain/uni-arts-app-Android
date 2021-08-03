package jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.extrinsics

import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.RuntimeRequest
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsicV28

class TransferRequestV28(extrinsic: EncodableStruct<SubmittableExtrinsicV28>) : RuntimeRequest(
    method = "author_submitExtrinsic",
    params = listOf(
        SubmittableExtrinsicV28.toHexString(extrinsic)
    )
)