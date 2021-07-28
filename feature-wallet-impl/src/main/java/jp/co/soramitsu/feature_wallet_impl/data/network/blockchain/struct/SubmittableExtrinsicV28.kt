package jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct

import io.emeraldpay.polkaj.scale.ScaleCodecReader
import io.emeraldpay.polkaj.scale.ScaleCodecWriter
import jp.co.soramitsu.common.utils.MultiAddress
import jp.co.soramitsu.fearless_utils.scale.*
import jp.co.soramitsu.fearless_utils.scale.dataType.*

private val VERSION = "84".toUByte(radix = 16)
private val TIP = 0.toBigInteger()

object MultiAddressType : DataType<MultiAddress>() {

    private val idCoder = byteArraySized(32)
    private val address32Coder = idCoder
    private val address20Coder = byteArraySized(20)

    override fun conformsType(value: Any?) = value is MultiAddress

    override fun read(reader: ScaleCodecReader): MultiAddress {
        return when (val typeIndex = reader.readByte().toInt()) {
            0 -> MultiAddress.Id(idCoder.read(reader))
            1 -> MultiAddress.Index(compactInt.read(reader))
            2 -> MultiAddress.Raw(byteArray.read(reader))
            3 -> MultiAddress.Address32(address32Coder.read(reader))
            4 -> MultiAddress.Address32(address20Coder.read(reader))
            else -> throw IllegalArgumentException("$typeIndex is not supported in MultiAddress")
        }
    }

    override fun write(writer: ScaleCodecWriter, multiAddress: MultiAddress) {
        byte.write(writer, multiAddress.enumIndex.toByte())

        return when (multiAddress) {
            is MultiAddress.Id -> idCoder.write(writer, multiAddress.value)
            is MultiAddress.Index -> compactInt.write(writer, multiAddress.value)
            is MultiAddress.Raw -> byteArray.write(writer, multiAddress.value)
            is MultiAddress.Address32 -> address32Coder.write(writer, multiAddress.value)
            is MultiAddress.Address20 -> address20Coder.write(writer, multiAddress.value)
        }
    }
}

object SubmittableExtrinsicV28 : Schema<SubmittableExtrinsicV28>() {
    val byteLength by compactInt()

    val signedExtrinsic by schema(SignedExtrinsicV28)
}

object SignedExtrinsicV28 : Schema<SignedExtrinsicV28>() {
    val version by uint8(default = VERSION)

    val accountId by custom(MultiAddressType)

    val signature by custom(SignatureType)

    val era by custom(EraType, default = Era.Immortal)

    val nonce by compactInt()

    val tip by compactInt(default = TIP)

    val call by schema(Call)
}

object TransferCallV28 : Schema<TransferCallV28>() {
    val callIndex by pair(uint8, uint8)

    val args by schema(TransferArgsV28)
}

object TransferArgsV28 : Schema<TransferArgsV28>() {
    val recipientId by custom(MultiAddressType)

    val collectionId by uint64()

    val itemId by uint64()

    val value by uint64()
}
