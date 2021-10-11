package com.gammaray.ui.activity


import android.view.View
import com.gammaray.R
import com.gammaray.base.BaseActivity
import com.gammaray.databinding.ActivityDappWebLayoutBinding
import com.gammaray.ui.web3.WebAppInterface
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

//DApp WebView
class DAppWebsActivity : BaseActivity<ActivityDappWebLayoutBinding>(), View.OnClickListener{

    companion object {
        private const val DAPP_URL =
            "https://cbridge.celer.network/?locale=zh-CN&utm_source=imtoken"
        private const val CHAIN_ID = 1
        private const val RPC_URL = "https://mainnet.infura.io/v3/7e2855d5896946cb985af8944713a371"
    }

    private lateinit var mDAppUrl: String

    private lateinit var mTitle: String

    override fun getLayoutId(): Int {
        return R.layout.activity_dapp_web_layout
    }

    override fun initPresenter() {
    }

    override fun initView() {

        //加载固定JS
        val provderJs = loadProviderJs()

        if (intent != null) {
            mTitle = intent.getStringExtra("dapp_title")
            mDataBinding.tvAppName.text = mTitle

//            mDAppUrl = intent.getStringExtra("dapp_url")
        }

        //加载RPC配置JS
        val initJs = loadInitJs(
            CHAIN_ID,
            RPC_URL
        )

        //初始化点击事件
        initListener()

        WebView.setWebContentsDebuggingEnabled(true)

        mDataBinding.webviewDapp.settings.run {
            javaScriptEnabled = true
            domStorageEnabled = true
        }

        WebAppInterface(this, mDataBinding.webviewDapp, DAPP_URL).run {
            mDataBinding.webviewDapp.addJavascriptInterface(this, "_tw_")

            val webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.evaluateJavascript(provderJs, null)
                    view?.evaluateJavascript(initJs, null)
                }
            }
            mDataBinding.webviewDapp.webViewClient = webViewClient
            mDataBinding.webviewDapp.loadUrl(DAPP_URL)
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
        return source
    }

    private fun initListener(){
        mDataBinding.rlFunctions.setOnClickListener(this)
        mDataBinding.rlClose.setOnClickListener(this)
    }

    private fun showFunctionPopWindow(){

    }
    
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.rl_functions -> showFunctionPopWindow()
            R.id.rl_close -> finish()
        }
    }
}