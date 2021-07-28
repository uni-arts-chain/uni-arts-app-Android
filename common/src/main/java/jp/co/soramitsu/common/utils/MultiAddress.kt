package jp.co.soramitsu.common.utils

import java.math.BigInteger

sealed class MultiAddress(val enumIndex: Int) {
    companion object {
        const val TYPE_ID = "Id"
        const val TYPE_INDEX = "Index"
        const val TYPE_RAW = "Raw"
        const val TYPE_ADDRESS32 = "Address32"
        const val TYPE_ADDRESS20 = "Address20"
    }

    class Id(val value: ByteArray) : MultiAddress(0)
    class Index(val value: BigInteger) : MultiAddress(1)
    class Raw(val value: ByteArray) : MultiAddress(2)
    class Address32(val value: ByteArray) : MultiAddress(3)
    class Address20(val value: ByteArray) : MultiAddress(4)
}