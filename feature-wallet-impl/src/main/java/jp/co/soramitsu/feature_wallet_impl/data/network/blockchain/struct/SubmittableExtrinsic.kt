package jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct

import jp.co.soramitsu.fearless_utils.extensions.toHexString
import jp.co.soramitsu.fearless_utils.scale.*
import jp.co.soramitsu.fearless_utils.scale.dataType.uint8
import org.bouncycastle.crypto.digests.Blake2bDigest
import org.bouncycastle.jcajce.provider.digest.BCMessageDigest

private val VERSION = "84".toUByte(radix = 16)
private val TIP = 0.toBigInteger()
private var current = 0

object SubmittableExtrinsic : Schema<SubmittableExtrinsic>() {
    val byteLength by compactInt()

    val signedExtrinsic by schema(SignedExtrinsic)
}

object SubmittableExtrinsic2 : Schema<SubmittableExtrinsic2>() {
    val byteLength2 by compactInt()

    val signedExtrinsic2 by schema(SignedExtrinsic2)
}

object SignedExtrinsic : Schema<SignedExtrinsic>() {
    val version by uint8(default = VERSION)

    val accountId by sizedByteArray(32)

    val signature by custom(SignatureType)

    val era by custom(EraType, default = Era.Immortal)

    val nonce by compactInt()

    val tip by compactInt(default = TIP)

    val call by schema(Call)

}

object SignedExtrinsic2 : Schema<SignedExtrinsic2>() {
    val version by uint8(default = VERSION)

    val accountId2 by sizedByteArray(32)

    val signature2 by custom(SignatureType)

    val era by custom(EraType, default = Era.Immortal)

    val nonce by compactInt()

    val tip by compactInt(default = TIP)

    val call2 by schema(Call2)

}

object Call : Schema<Call>() {
    val callIndex by pair(uint8, uint8)


    val args by schema(TransferArgs)

}

fun setCurrent(a: Int) {
    current = a;
}

object Call2 : Schema<Call2>() {
    val callIndex by pair(uint8, uint8)

    val args2 by schema(OrderArgs)

}


object TransferArgs : Schema<TransferArgs>() {
    val recipientId by sizedByteArray(32)
    val colloectId by compactInt()
    val amount by compactInt()
}

object OrderArgs : Schema<OrderArgs>() {
    val collectionId by uint64()
    val itemId by uint64()
    val value by uint64()
    val price by uint64()
}

object ExtrinsicPayloadValue : Schema<ExtrinsicPayloadValue>() {
    val call by schema(Call)

//    val era by custom(EraType, default = Era.Immortal)

    val nonce by compactInt()

//    val tip by compactInt(default = TIP)

    val specVersion by uint32()
    val transactionVersion by uint32()

    val genesis by sizedByteArray(32)
    val blockHash by sizedByteArray(32)
}

object ExtrinsicPayloadValue2 : Schema<ExtrinsicPayloadValue2>() {
    val call by schema(Call2)

    val era by custom(EraType, default = Era.Immortal)

    val nonce by compactInt()

    val tip by compactInt(default = TIP)

    val specVersion by uint32()
    val transactionVersion by uint32()

    val genesis by sizedByteArray(32)
    val blockHash by sizedByteArray(32)
}

object Blake2b256 : BCMessageDigest(Blake2bDigest(256))

fun EncodableStruct<SubmittableExtrinsic>.hash(): String {
    val bytes = Blake2b256.digest(SubmittableExtrinsic.toByteArray(this))

    return bytes.toHexString(withPrefix = true)
}