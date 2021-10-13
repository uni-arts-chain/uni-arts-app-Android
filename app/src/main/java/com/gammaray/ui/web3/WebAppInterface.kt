package com.gammaray.ui.web3

import android.webkit.JavascriptInterface
import com.gammaray.eth.util.BalanceUtils
import com.gammaray.ui.activity.DAppWebsActivity
import com.gammaray.ui.x5.X5WebView
import org.json.JSONObject
import wallet.core.jni.CoinType
import wallet.core.jni.Curve
import wallet.core.jni.PrivateKey
import java.math.BigInteger

class WebAppInterface(
    private val context: DAppWebsActivity,
    private val webView: X5WebView,
    private val dappUrl: String,
    private val selfPrivateKey: String
) {
    private val privateKey = PrivateKey(selfPrivateKey.toHexByteArray())
    private val addr = CoinType.ETHEREUM.deriveAddress(privateKey).toLowerCase()

    //    private val addr = "0x6B9690CEa137b694f5E052A6459798aE995991Af"
//        private val addr = "0xf2a377D4efE32A62400974c00A61acDcB3cA5735"

    @JavascriptInterface
    fun postMessage(json: String) {
        val obj = JSONObject(json)
        println(obj)
        val id = obj.getLong("id")
        val method = DAppMethod.fromValue(obj.getString("name"))
        when (method) {
            DAppMethod.REQUESTACCOUNTS -> {
//                context.materialAlertDialog {
//                    title = "Request Accounts"
//                    message = "${dappUrl} requests your address"
//                    okButton {
//                        val setAddress = "window.ethereum.setAddress(\"$addr\");"
//                        val callback = "window.ethereum.sendResponse($id, [\"$addr\"])"
//                        webView.post {
//                            webView.evaluateJavascript(setAddress) {
//                                // ignore
//                            }
//                            webView.evaluateJavascript(callback) { value ->
//                                println(value)
//                            }
//                        }
//                    }
//                    cancelButton()
//                }.show()
                val setAddress = "window.ethereum.setAddress(\"$addr\");"
                val callback = "window.ethereum.sendResponse($id, [\"$addr\"])"
                webView.post {
                    webView.evaluateJavascript(setAddress) {
                        // ignore
                    }
                    webView.evaluateJavascript(callback) { value ->
                        println(value)
                    }
                }
            }
            DAppMethod.SIGNMESSAGE -> {
                val data = extractMessage(obj)
                handleSignMessage(id, data, addPrefix = false)
            }
            DAppMethod.SIGNPERSONALMESSAGE -> {
                val data = extractMessage(obj)
                handleSignMessage(id, data, addPrefix = true)
            }
            DAppMethod.SIGNTYPEDMESSAGE -> {
                val data = extractMessage(obj)
                val raw = extractRaw(obj)
                handleSignTypedMessage(id, data, raw)
            }
            DAppMethod.SIGNTRANSACTION -> {
                val param = obj.getJSONObject("object")
                val transValue = BalanceUtils.weiToEth(BigInteger(param.getString("value").removePrefix("0x"),16)).toPlainString()
                val gasPrice = BalanceUtils.weiToGwei(BigInteger(param.getString("gasPrice").removePrefix("0x"),16))
                val gasLimit = BalanceUtils.weiToGwei(BigInteger(param.getString("gasLimit").removePrefix("0x"),16))
                val from = param.getString("from")
                val to = param.getString("to")
                context.toPinCodeActivity(transValue,gasPrice,gasLimit,from,to)
            }
            else -> {
//                context.materialAlertDialog {
//                    title = "Error"
//                    message = "$method not implemented"
//                    okButton {
//                    }
//                }.show()
            }
        }
    }

    private fun extractMessage(json: JSONObject): ByteArray {
        val param = json.getJSONObject("object")
        val data = param.getString("data")
        return Numeric.hexStringToByteArray(data)
    }

    private fun extractRaw(json: JSONObject): String {
        val param = json.getJSONObject("object")
        return param.getString("raw")
    }

    private fun handleSignMessage(id: Long, data: ByteArray, addPrefix: Boolean) {
//        context.materialAlertDialog {
//            title = "Sign Message"
//            message = if (addPrefix) String(data, Charsets.UTF_8) else Numeric.toHexString(data)
//            cancelButton {
//                webView.sendError("Cancel", id)
//            }
//            okButton {
//                webView.sendResult(signEthereumMessage(data, addPrefix), id)
//            }
//        }.show()
    }

    private fun handleSignTypedMessage(id: Long, data: ByteArray, raw: String) {
//        context.materialAlertDialog {
//            title = "Sign Typed Message"
//            message = raw
//            cancelButton {
//                webView.sendError("Cancel", id)
//            }
//            okButton {
//                webView.sendResult(signEthereumMessage(data, false), id)
//            }
//        }.show()
        webView.sendResult(signEthereumMessage(data, false), id)
    }

    private fun signEthereumMessage(message: ByteArray, addPrefix: Boolean): String {
        var data = message
        if (addPrefix) {
            val messagePrefix = "\u0019Ethereum Signed Message:\n"
            val prefix = (messagePrefix + message.size).toByteArray()
            val result = ByteArray(prefix.size + message.size)
            System.arraycopy(prefix, 0, result, 0, prefix.size)
            System.arraycopy(message, 0, result, prefix.size, message.size)
            data = wallet.core.jni.Hash.keccak256(result)
        }

        val signatureData = privateKey.sign(data, Curve.SECP256K1)
            .apply {
                (this[this.size - 1]) = (this[this.size - 1] + 27).toByte()
            }
        return Numeric.toHexString(signatureData)
    }
}
