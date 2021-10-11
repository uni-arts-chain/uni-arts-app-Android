package com.gammaray.ui.activity


import android.content.ClipData
import android.content.ClipboardManager
import android.text.TextUtils
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
import com.gammaray.entity.BaseResponseVo
import com.gammaray.entity.DAppItemBean
import com.gammaray.entity.DAppRecentlyBean
import com.gammaray.entity.DappFunctionBean
import com.gammaray.net.MinerCallback
import com.gammaray.net.RequestManager
import com.gammaray.ui.web3.WebAppInterface
import com.gammaray.widget.BasePopupWindow
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import retrofit2.Call
import retrofit2.Response

//DApp WebView
class DAppWebsActivity : BaseActivity<ActivityDappWebLayoutBinding>(), View.OnClickListener {

    companion object {
        private const val DAPP_URL =
            "https://cbridge.celer.network/?locale=zh-CN&utm_source=imtoken"
        private const val CHAIN_ID = 1
        private const val RPC_URL = "https://mainnet.infura.io/v3/7e2855d5896946cb985af8944713a371"
    }

    private var mDAppUrl: String = ""

    private lateinit var mTitle: String

    private var mDAppId: Int = -1

    private var mDAppFunctionsWindow: PopupWindow? = null

    private var mFuncListView: RecyclerView? = null

    private var mAdapter: DAppFunctionAdapter? = null

    private val mFunctions: ArrayList<DappFunctionBean> = ArrayList()

    private val mCollect = DappFunctionBean("取消收藏", R.mipmap.icon_a_collect)

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

            mDAppId = intent.getIntExtra("dapp_id", -1)

            mDAppUrl = intent.getStringExtra("dapp_url")

            bIsCollect = intent.getBooleanExtra("dapp_collect",false)
        }

        if (!TextUtils.isEmpty(mDAppId.toString())) {
            sendRecentlyDApps(mDAppId.toString())
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
            if(!TextUtils.isEmpty(DAPP_URL)){
                mDataBinding.webviewDapp.loadUrl(DAPP_URL)
            }
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
                        unFavouriteDApp(mDAppId.toString())
                    } else {
                        favouriteDApp(mDAppId.toString())
                    }
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
        val mClipData = ClipData.newPlainText("Label", mDAppUrl)
        cm.primaryClip = mClipData
        ToastUtils.showShort("复制成功")
    }

    //最近使用DApp
    private fun sendRecentlyDApps(id: String) {
        val params = HashMap<String, String>()
        params["dapp_id"] = id
        RequestManager.instance()
            .sendRecentlyDApps(params, object : MinerCallback<BaseResponseVo<DAppRecentlyBean>> {
                override fun onSuccess(
                    call: Call<BaseResponseVo<DAppRecentlyBean>>?,
                    response: Response<BaseResponseVo<DAppRecentlyBean>>?
                ) {
                    if (response != null && response.isSuccessful) {

                    }
                }

                override fun onError(
                    call: Call<BaseResponseVo<DAppRecentlyBean>>?,
                    response: Response<BaseResponseVo<DAppRecentlyBean>>?
                ) {

                }

                override fun onFailure(call: Call<*>?, t: Throwable?) {

                }

            })
    }

    private fun favouriteDApp(id: String) {
        showLoading(R.string.progress_loading)
        val params = HashMap<String, String>()
        params["id"] = id
        RequestManager.instance()
            .favoriteDApp(id, object : MinerCallback<BaseResponseVo<DAppItemBean>> {
                override fun onSuccess(
                    call: Call<BaseResponseVo<DAppItemBean>>?,
                    response: Response<BaseResponseVo<DAppItemBean>>?
                ) {
                    dismissLoading()
                    if (response != null && response.isSuccessful) {
                        bIsCollect = true
                        mFunctions[0].dappIcon = R.mipmap.icon_a_collect
                        mFunctions[0].dappName = "取消收藏"
                        mAdapter!!.notifyDataSetChanged()
                        ToastUtils.showShort("已收藏")
                    }
                }

                override fun onError(
                    call: Call<BaseResponseVo<DAppItemBean>>?,
                    response: Response<BaseResponseVo<DAppItemBean>>?
                ) {
                    dismissLoading()
                }

                override fun onFailure(call: Call<*>?, t: Throwable?) {
                    dismissLoading()
                }
            })
    }

    private fun unFavouriteDApp(id: String) {
        showLoading(R.string.progress_loading)
        val params = HashMap<String, String>()
        params["id"] = id
        RequestManager.instance()
            .unfavoriteDApp(id, object : MinerCallback<BaseResponseVo<DAppItemBean>> {
                override fun onSuccess(
                    call: Call<BaseResponseVo<DAppItemBean>>?,
                    response: Response<BaseResponseVo<DAppItemBean>>?
                ) {
                    dismissLoading()
                    if (response != null && response.isSuccessful) {
                        bIsCollect = false
                        mFunctions[0].dappIcon = R.mipmap.icon_c_collect
                        mFunctions[0].dappName = "收藏"
                        mAdapter!!.notifyDataSetChanged()
                        ToastUtils.showShort("已取消收藏")
                    }
                }

                override fun onError(
                    call: Call<BaseResponseVo<DAppItemBean>>?,
                    response: Response<BaseResponseVo<DAppItemBean>>?
                ) {
                    dismissLoading()
                }

                override fun onFailure(call: Call<*>?, t: Throwable?) {
                    dismissLoading()
                }
            })
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.rl_functions -> showFunctionPopWindow()
            R.id.rl_close -> finish()
        }
    }
}