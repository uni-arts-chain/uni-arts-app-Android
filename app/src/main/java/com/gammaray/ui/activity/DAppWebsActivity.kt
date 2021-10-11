package com.gammaray.ui.activity

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.gammaray.R
import com.gammaray.ui.web3.WebAppInterface

class DAppWebsActivity : AppCompatActivity() {
    companion object {
        private const val DAPP_URL = "https://cbridge.celer.network/?locale=zh-CN&utm_source=imtoken"
        private const val CHAIN_ID = 1
        private const val RPC_URL = "https://mainnet.infura.io/v3/7e2855d5896946cb985af8944713a371"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dapp_web_layout)

        val provderJs = loadProviderJs()
        val initJs = loadInitJs(
            CHAIN_ID,
            RPC_URL
        )
        WebView.setWebContentsDebuggingEnabled(true)
        val webview: WebView = findViewById(R.id.webview_dapp)
        webview.settings.run {
            javaScriptEnabled = true
            domStorageEnabled = true
        }
        WebAppInterface(this, webview, DAPP_URL).run {
            webview.addJavascriptInterface(this, "_tw_")

            val webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.evaluateJavascript(provderJs, null)
                    view?.evaluateJavascript(initJs, null)
                }
            }
            webview.webViewClient = webViewClient
            webview.loadUrl(DAPP_URL)
        }
    }

    private fun loadProviderJs(): String {
        return resources.openRawResource(R.raw.trust_min).bufferedReader().use { it.readText() }
    }

    private fun loadInitJs(chainId: Int, rpcUrl: String): String {
        val source = """
        (function() {
            var config = {
                chainId: $chainId,
                rpcUrl: "$rpcUrl",
                isDebug: true
            };
            window.ethereum = new trustwallet.Provider(config);
            window.web3 = new trustwallet.Web3(window.ethereum);
            trustwallet.postMessage = (json) => {
                window._tw_.postMessage(JSON.stringify(json));
            }
        })();
        """
        return  source
    }
}