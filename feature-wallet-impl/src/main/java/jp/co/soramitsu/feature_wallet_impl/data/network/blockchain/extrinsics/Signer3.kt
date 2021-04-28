package jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.extrinsics

import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType
import jp.co.soramitsu.fearless_utils.encrypt.Signer
import jp.co.soramitsu.fearless_utils.encrypt.model.Keypair
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.ExtrinsicPayloadValue2

fun Signer.signExtrinsic3(
        payload: String,
        keyPair: Keypair,
        encryptionType: EncryptionType
): ByteArray {
    val message = payload.toByteArray()

    return sign(encryptionType, message, keyPair).signature
}