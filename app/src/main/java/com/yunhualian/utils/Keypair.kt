package com.yunhualian.utils

class Keypair(
        val privateKey: ByteArray,
        val publicKey: ByteArray,
        val nonce: ByteArray? = null
)