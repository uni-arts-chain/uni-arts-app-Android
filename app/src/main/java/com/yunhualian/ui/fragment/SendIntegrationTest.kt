package com.yunhualian.ui.fragment

import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import jp.co.soramitsu.fearless_utils.scale.invoke
import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType
import jp.co.soramitsu.fearless_utils.encrypt.Signer
import jp.co.soramitsu.fearless_utils.encrypt.model.Keypair
import jp.co.soramitsu.fearless_utils.runtime.Module
import jp.co.soramitsu.fearless_utils.scale.toByteArray
import jp.co.soramitsu.fearless_utils.scale.toHexString
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.scale
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.scaleCollection
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.account.AccountInfoRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.author.PendingExtrinsicsRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersionRequest
import jp.co.soramitsu.feature_account_api.domain.model.Node
import jp.co.soramitsu.feature_wallet_api.domain.model.Token
import jp.co.soramitsu.feature_wallet_api.domain.model.amountFromPlanks
import jp.co.soramitsu.feature_wallet_api.domain.model.calculateTotalBalance
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.extrinsics.TransferRequest
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.requests.GetStorageRequest
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.response.RuntimeVersion
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.*
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.AccountInfo.nonce
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.Call.args
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.Call.callIndex
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.OrderArgs.itemId
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.OrderArgs.value
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsic.signedExtrinsic
import org.bouncycastle.util.encoders.Hex
import java.math.BigDecimal


class SendIntegrationTest {

    private val sS58Encoder = SS58Encoder()


    public fun shouldfee(receiveAddress: String, amount: Int, collectId: Int, item_id: Int, privateKey: String, publicKey: String, nonceStr: String, rxWebSocket: SocketService, signer: Signer, hash: String): EncodableStruct<SubmittableExtrinsic> {

//        val keyPair = KeypairFactory().generate(EncryptionType.ECDSA, seed, "")
        val keypair = Keypair(Hex.decode(privateKey), Hex.decode(publicKey), Hex.decode(nonceStr))
        val accountId = Hex.decode(publicKey)
        val genesis = hash
        val genesisBytes = Hex.decode(genesis)


        val transferAmount = BigDecimal("1").scaleByPowerOfTen(Token.Type.DOT.mantissa)

        val runtimeInfo = rxWebSocket
                .executeRequest(RuntimeVersionRequest(), pojo<RuntimeVersion>().nonNull())
                .blockingGet()

        val specVersion = runtimeInfo.specVersion
        val transactionVersion = runtimeInfo.transactionVersion

        val pendingExtrinsics = rxWebSocket
                .executeRequest(PendingExtrinsicsRequest(), scaleCollection(SubmittableExtrinsic))
                .blockingGet().result!!

        val pendingForCurrent = pendingExtrinsics.count { it[signedExtrinsic][SignedExtrinsic.accountId].contentEquals(accountId) }

        val accountInfo = rxWebSocket
                .executeRequest(AccountInfoRequest(accountId), scale(AccountInfo))
                .blockingGet().result!!

        val nonce = accountInfo[nonce] + pendingForCurrent.toUInt()
        val nonceBigInt = nonce.toLong().toBigInteger()

        val receiverPublicKey = Hex.decode(receiveAddress)
        val callStruct = Call { call ->
            call[callIndex] = Pair(29.toUByte(), 14.toUByte())
            call[args] = OrderArgs { args ->
                args[OrderArgs.recipientId] = receiverPublicKey
                args[OrderArgs.collectionId] = collectId.toBigInteger()
                args[itemId] = item_id.toBigInteger()
                args[value] = amount.toBigInteger()
            }
        }

        val payload = ExtrinsicPayloadValue { payload ->
            payload[ExtrinsicPayloadValue.call] = callStruct
            payload[ExtrinsicPayloadValue.era] = Era.Immortal
            payload[ExtrinsicPayloadValue.nonce] = nonceBigInt
            payload[ExtrinsicPayloadValue.tip] = 0.toBigInteger()
            payload[ExtrinsicPayloadValue.specVersion] = specVersion.toUInt()
            payload[ExtrinsicPayloadValue.transactionVersion] = transactionVersion.toUInt()
            payload[ExtrinsicPayloadValue.genesis] = genesisBytes
            payload[ExtrinsicPayloadValue.blockHash] = genesisBytes
        }
        val payloadByte = payload.toByteArray()
        val hexStr = payload.toHexString()
        val signature = Signature(
                encryptionType = EncryptionType.SR25519,
                value = signer.sign(EncryptionType.SR25519, payload.toByteArray(), keypair).signature
        )

        val extrinsic = SignedExtrinsic { extrinsic ->
            extrinsic[SignedExtrinsic.accountId] = accountId
            extrinsic[SignedExtrinsic.signature] = signature
            extrinsic[SignedExtrinsic.nonce] = nonceBigInt
            extrinsic[SignedExtrinsic.call] = callStruct
        }

        val extrinsicBytes = SignedExtrinsic.toByteArray(extrinsic)
        val byteLength = extrinsicBytes.size.toBigInteger()

        return SubmittableExtrinsic { struct ->
            struct[SubmittableExtrinsic.byteLength] = byteLength
            struct[signedExtrinsic] = extrinsic
        }
    }

    public fun transfer(rxWebSocket: SocketService, transferRequest: TransferRequest): String {

        val result = rxWebSocket.executeRequest(transferRequest).blockingGet().result
        return result.toString()
    }

    public fun getBalance(rxWebSocket: SocketService, address: String): String {

        var accountId = sS58Encoder.decode(address)
        val key = Module.System.Account.storageKey(accountId)

        val request = GetStorageRequest(key)
        val result = rxWebSocket.executeRequest(request).blockingGet().result ?: return ""
        val accountStr = AccountInfo.read(result.toString())
        val tokenType = Token.Type.DOT
        val recipientData = accountStr[AccountInfo.data]
        val totalRecipientBalanceInPlanks = calculateTotalBalance(recipientData[AccountData.free], recipientData[AccountData.reserved])
        val totalRecipientBalance = tokenType.amountFromPlanks(totalRecipientBalanceInPlanks)

        return totalRecipientBalance.toString()
    }
}