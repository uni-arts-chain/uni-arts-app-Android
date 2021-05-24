package jp.co.soramitsu.feature_wallet_impl.data.network.integration

import com.google.gson.Gson
import com.neovisionaries.ws.client.WebSocketFactory
import com.upbest.arouter.Extras.privateKey
import com.upbest.arouter.Extras.publicKey
import io.reactivex.Observable
import jp.co.soramitsu.fearless_utils.scale.EncodableStruct
import jp.co.soramitsu.fearless_utils.scale.invoke
import jp.co.soramitsu.common.resources.ResourceManager
import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType
import jp.co.soramitsu.fearless_utils.encrypt.KeypairFactory
import jp.co.soramitsu.fearless_utils.encrypt.Signer
import jp.co.soramitsu.fearless_utils.encrypt.model.Keypair
import jp.co.soramitsu.fearless_utils.runtime.Module
import jp.co.soramitsu.fearless_utils.scale.toByteArray
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.nonNull
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.pojo
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.scale
import jp.co.soramitsu.fearless_utils.wsrpc.mappers.scaleCollection
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.account.AccountInfoRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.author.PendingExtrinsicsRequest
import jp.co.soramitsu.fearless_utils.wsrpc.request.runtime.chain.RuntimeVersionRequest
import jp.co.soramitsu.fearless_utils.wsrpc.subscription.SubscriptionChange
import jp.co.soramitsu.feature_account_api.domain.model.Node
import jp.co.soramitsu.feature_wallet_api.domain.model.Token
import jp.co.soramitsu.feature_wallet_api.domain.model.amountFromPlanks
import jp.co.soramitsu.feature_wallet_api.domain.model.calculateTotalBalance
import jp.co.soramitsu.feature_wallet_impl.data.mappers.mapAssetLocalToAsset
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.WssSubstrateSource
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.extrinsics.TransferRequest
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.extrinsics.signExtrinsic
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.extrinsics.signExtrinsic2
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.extrinsics.signExtrinsic3
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.requests.FeeCalculationRequest
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.requests.GetStorageRequest
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.requests.SubscribeStorageRequest
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.response.BalanceChange
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.response.RuntimeVersion
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.*
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.AccountInfo.nonce
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.Call.args
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.Call.callIndex
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.Call2.args2
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.OrderArgs.itemId
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.OrderArgs.value
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsic.signedExtrinsic
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.SubmittableExtrinsic2.signedExtrinsic2
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.TransferArgs.amount
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.TransferArgs.colloectId
import jp.co.soramitsu.feature_wallet_impl.data.network.blockchain.struct.TransferArgs.recipientId
import org.bouncycastle.util.encoders.Hex
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.math.BigDecimal

//private const val PUBLIC_KEY = "f65a7d560102f2019da9b9d8993f53f51cc38d50cdff3d0b8e71997d7f911ff1"
private const val PUBLIC_KEY = "b682e7480e2a6466771adfac3f55a7abeeabdd89f8428a6cf18cf4975c204822"

//private const val PRIVATE_KEY = "ae4093af3c40f2ecc32c14d4dada9628a4a42b28ca1a5b200b89321cbc883182"
private const val PRIVATE_KEY = "e7f4018d35645c7c6e772b966278a1dec21190546150c9dbc9a470bee84ca00d"
private const val TO_ADDRESS = "5DEwU2U97RnBHCpfwHMDfJC7pqAdfWaPFib9wiZcr2ephSfT"

private const val NONCE_STR = "9141d3c87bc52ff5c9a71df1ef014e4e09c51d68b45a9823fc96d849a909f729"

private const val URL = "wss://testnet.uniarts.me"

@RunWith(MockitoJUnitRunner::class)
@Ignore("Manual run only")
class SendIntegrationTest {
    private val sS58Encoder = SS58Encoder()
    private val signer = Signer()

    private val mapper = Gson()

    @Mock
    private lateinit var resourceManager: ResourceManager

    private lateinit var rxWebSocket: SocketService

    @Before
    fun setup() {
        given(resourceManager.getString(anyInt())).willReturn("Mock")

        rxWebSocket = SocketService(mapper, StdoutLogger(), WebSocketFactory())

        rxWebSocket.start(URL)
    }

    @After
    fun tearDown() {
        rxWebSocket.stop()
    }

    @Test
    fun `should perform transfer`() {
        val keyPair = Keypair(Hex.decode(PRIVATE_KEY), Hex.decode(PUBLIC_KEY))

        val submittableExtrinsic = generateExtrinsic(keyPair)

        val feeRequest = TransferRequest(submittableExtrinsic)

        val result = rxWebSocket.executeRequest(feeRequest).blockingGet().result

        assert(result != null)
    }

    @Test
    fun getBalance() {
        var address = "5DvJ1kADPVRacmumyf1T4TfW6CzoXkTLksF3d4R3zq5pRYZd"
//        var key = "0x26aa394eea5630e07c48ae0c9558cef7b99d880ec681799c0cf30e8886371da930dae9360f519bb979826d423e4e94e06ae1740362e00e6793c99fe1377c2636c35324b63004d75114f3caeb8ead874f"
        var accountId = sS58Encoder.decode(address)
        val key = Module.System.Account.storageKey(accountId)

        val request = GetStorageRequest(key)
        val result = rxWebSocket.executeRequest(request).blockingGet().result
        val accountStr = AccountInfo.read(result.toString())

        val tokenType = Token.Type.DOT

        val recipientData = accountStr[AccountInfo.data]
        val totalRecipientBalanceInPlanks = calculateTotalBalance(recipientData[AccountData.free], recipientData[AccountData.reserved])
        val totalRecipientBalance = tokenType.amountFromPlanks(totalRecipientBalanceInPlanks)

        assert(accountStr != null)
    }

    private fun emptyAccountInfo() = AccountInfo { info ->
        info[nonce] = 0.toUInt()
        info[AccountInfo.refCount] = 0.toUInt()

        info[AccountInfo.data] = AccountData { data ->
            data[AccountData.free] = 0.toBigInteger()
            data[AccountData.reserved] = 0.toBigInteger()
            data[AccountData.miscFrozen] = 0.toBigInteger()
            data[AccountData.feeFrozen] = 0.toBigInteger()
        }
    }

    private fun buildBalanceChange(subscriptionChange: SubscriptionChange) {
        val block = subscriptionChange.params.result.block

        val change = subscriptionChange.params.result.getSingleChange()

    }

    @Test
    fun `should calculate fee`() {
        val seed = ByteArray(32) { 1 }
        val keyPair = KeypairFactory().generate(EncryptionType.ECDSA, seed, "")

        val submittableExtrinsic = generateExtrinsic(keyPair)

        val feeRequest = FeeCalculationRequest(submittableExtrinsic)

        val result = rxWebSocket.executeRequest(feeRequest).blockingGet().result

        assert(result != null)
    }

    @Test
    fun `should fee`() {

        val seed = ByteArray(32) { 1 }
//        val keyPair = KeypairFactory().generate(EncryptionType.ECDSA, seed, "")
        val keypair = Keypair(Hex.decode(PRIVATE_KEY), Hex.decode(PUBLIC_KEY), Hex.decode(NONCE_STR));
        val accountId = Hex.decode(PUBLIC_KEY)
        val genesis = Node.NetworkType.POLKADOT.runtimeConfiguration.genesisHash
        val genesisBytes = Hex.decode(genesis)


        val transferAmount = BigDecimal("0.001").scaleByPowerOfTen(Token.Type.DOT.mantissa)

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

        val receiverPublicKey = Hex.decode("3343492d96f833032637884ff91e253a5359a2f174ba317ba5d69e83543af25d")
        val callStruct = Call { call ->
            call[callIndex] = Pair(29.toUByte(), 14.toUByte())
            call[args] = OrderArgs { args ->
                args[OrderArgs.recipientId] = receiverPublicKey
                args[OrderArgs.collectionId] = transferAmount.toBigIntegerExact()
                args[itemId] = 254.toBigInteger()
                args[value] = transferAmount.toBigIntegerExact()
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

        val submittableExtrinsic = SubmittableExtrinsic { struct ->
            struct[SubmittableExtrinsic.byteLength] = byteLength
            struct[signedExtrinsic] = extrinsic
        }
        print(submittableExtrinsic.toString())
    }

    private fun generateExtrinsic(keypair: Keypair): EncodableStruct<SubmittableExtrinsic> {

        val accountId = Hex.decode(PUBLIC_KEY)
        val genesis = Node.NetworkType.POLKADOT.runtimeConfiguration.genesisHash
        val genesisBytes = Hex.decode(genesis)


        val transferAmount = BigDecimal("0.001").scaleByPowerOfTen(Token.Type.DOT.mantissa)

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

        val receiverPublicKey = sS58Encoder.decode(TO_ADDRESS)
        val collectionId: UInt = 12.toUInt()
        val callStruct = Call { call ->
            call[callIndex] = Pair(4.toUByte(), 0.toUByte())

//            call[args] = TransferArgs { args ->
//                args[recipientId] = receiverPublicKey
//                args[colloectId] = transferAmount.toBigIntegerExact()
//                args[amount] = transferAmount.toBigIntegerExact()
//            }
//            call[args] = OrderArgs { args ->
//                args[OrderArgs.recipientId] = receiverPublicKey
//                args[OrderArgs.collectionId] = 12.toBigInteger()
//                args[OrderArgs.itemId] = 12.toBigInteger()
//                args[OrderArgs.value] = transferAmount.toBigIntegerExact()
//            }
        }

        val payload = ExtrinsicPayloadValue { payload ->
            payload[ExtrinsicPayloadValue.call] = callStruct
            payload[ExtrinsicPayloadValue.nonce] = nonceBigInt
            payload[ExtrinsicPayloadValue.specVersion] = specVersion.toUInt()
            payload[ExtrinsicPayloadValue.transactionVersion] = transactionVersion.toUInt()
            payload[ExtrinsicPayloadValue.genesis] = genesisBytes
            payload[ExtrinsicPayloadValue.blockHash] = genesisBytes
        }

        val signature = Signature(
                encryptionType = EncryptionType.ECDSA,
                value = signer.signExtrinsic(payload, keypair, EncryptionType.ECDSA)
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
}