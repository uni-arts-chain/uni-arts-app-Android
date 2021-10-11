package com.gammaray.ui.activity


import android.content.ClipData
import android.content.ClipboardManager
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.gammaray.R
import com.gammaray.adapter.DAppFunctionAdapter
import com.gammaray.base.BaseActivity
import com.gammaray.databinding.ActivityDappWebLayoutBinding
import com.gammaray.entity.DappFunctionBean
import com.gammaray.ui.web3.WebAppInterface
import com.gammaray.widget.BasePopupWindow
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient

//DApp WebView
class DAppWebsActivity : BaseActivity<ActivityDappWebLayoutBinding>(), View.OnClickListener {

    companion object {
        private const val DAPP_URL =
            "https://cbridge.celer.network/?locale=zh-CN&utm_source=imtoken"
        private const val CHAIN_ID = 1
        private const val RPC_URL = "https://mainnet.infura.io/v3/7e2855d5896946cb985af8944713a371"
    }

    private lateinit var mDAppUrl: String

    private lateinit var mTitle: String

    private var mDAppFunctionsWindow: PopupWindow? = null

    private var mFuncListView: RecyclerView? = null

    private var mAdapter: DAppFunctionAdapter? = null

    private val mFunctions: ArrayList<DappFunctionBean> = ArrayList()

    private val mCollect = DappFunctionBean("收藏", R.mipmap.icon_a_collect)

    private val mUnCollect = DappFunctionBean("收藏", R.mipmap.icon_c_collect)

    private val mCopyUrl = DappFunctionBean("复制链接", R.mipmap.icon_url_copy)

    private val mRefresh = DappFunctionBean("刷新", R.mipmap.icon_refresh)

    private var bIsCollect = false

    override fun getLayoutId(): Int {
        return R.layout.activity_dapp_web_layout
    }

    override fun initPresenter() {
    }

    override fun initView() {

        //加载固定JS
        val providerJs = loadProviderJs()

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

        initFunctionPopWindow()

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
                    view?.evaluateJavascript(providerJs, null)
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

    private fun initListener() {
        mDataBinding.rlFunctions.setOnClickListener(this)
        mDataBinding.rlClose.setOnClickListener(this)
    }

    private fun initFunctionPopWindow() {
        val functionView = LayoutInflater.from(this).inflate(R.layout.pop_dapp_manage_layout, null)
        mFuncListView = functionView.findViewById(R.id.rv_dapp_functions)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        mFuncListView?.layoutManager = layoutManager
        mAdapter = DAppFunctionAdapter(mFunctions)
        mFuncListView?.adapter = mAdapter

        mDAppFunctionsWindow = BasePopupWindow(this)
        mDAppFunctionsWindow?.contentView = functionView
        mDAppFunctionsWindow?.width = ViewGroup.LayoutParams.MATCH_PARENT
        mDAppFunctionsWindow?.height = ViewGroup.LayoutParams.WRAP_CONTENT
        mDAppFunctionsWindow?.isOutsideTouchable = false
        mDAppFunctionsWindow?.isTouchable = true
        mDAppFunctionsWindow?.animationStyle = R.style.mypopwindow_anim_style

        if (bIsCollect) {
            mFunctions.add(mCollect)
        } else {
            mFunctions.add(mUnCollect)
        }
        mFunctions.add(mCopyUrl)
        mFunctions.add(mRefresh)

        mAdapter!!.onItemClickListener =
            BaseQuickAdapter.OnItemClickListener { adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
                if (position == 0) {
                    if (bIsCollect) {
                        bIsCollect = false
                        mFunctions[position].dappIcon = R.mipmap.icon_c_collect
                        mFunctions[position].dappName = "收藏"
                    } else {
                        bIsCollect = true
                        mFunctions[position].dappIcon = R.mipmap.icon_a_collect
                        mFunctions[position].dappName = "取消收藏"
                    }
                    mAdapter!!.notifyDataSetChanged()
                } else if (position == 1) {
                    copyDAppUrl()
                    if (mDAppFunctionsWindow != null) {
                        mDAppFunctionsWindow?.dismiss()
                    }
                } else if (position == 2) {
                    mDataBinding.webviewDapp.reload()
                    if (mDAppFunctionsWindow != null) {
                        mDAppFunctionsWindow?.dismiss()
                    }
                }
            }

        functionView.findViewById<View>(R.id.tv_hint_close).setOnClickListener { view: View? ->
            if (mDAppFunctionsWindow != null) {
                mDAppFunctionsWindow?.dismiss()
            }
        }
    }

    private fun showFunctionPopWindow() {
        mDAppFunctionsWindow?.showAtLocation(mDataBinding.parentLayout, Gravity.BOTTOM, 0, 0)
    }

    private fun copyDAppUrl() {
        val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        val mClipData = ClipData.newPlainText("Label", DAPP_URL)
        // 将ClipData内容放到系统剪贴板里。
        cm.primaryClip = mClipData
        ToastUtils.showShort("复制成功")
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.rl_functions -> showFunctionPopWindow()
            R.id.rl_close -> finish()
        }
    }
}