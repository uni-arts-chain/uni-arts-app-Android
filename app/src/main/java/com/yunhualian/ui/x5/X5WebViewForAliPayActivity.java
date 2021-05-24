package com.yunhualian.ui.x5;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.blankj.utilcode.util.ToastUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityX5webviewBinding;

import java.util.HashMap;
import java.util.Map;


/**
 * Synopsis     com.cashfishvay.app.activity.ui.x5.X5WebView
 * Author		Mosr
 * Version		1.0
 * Create 	    2019/12/28 08:33
 * Email  		intimatestranger@sina.cn
 */
public class X5WebViewForAliPayActivity extends BaseActivity<ActivityX5webviewBinding> {
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String DATA = "data";
    public static final String TYPE = "payType";
    public static final String GO_BACK = "go_back";
    public static final String WECHAT = "wepay";
    public static final String FULLSCREEN = "full_screen";
    public static String RURL = "http://mall.senmeo.tech";
    LinearLayout linParent;
    private String mTitle;
    private String mUrl;
    private String mData;
    private long lastPressM;
    private boolean go_back = true;
    private String payType;
    /*
    * 9000——订单支付成功
8000——正在处理中
4000——订单支付失败
5000——重复请求
6001——用户中途取消
6002——网络连接出错
    * */
    public final String PAY_SUCCESS = "9000";
    public final String PAY_DEAL = "8000";
    public final String PAY_FAIL = "4000";
    public final String PAY_CANCLE = "6001";

    private void loadPage() {
        if (!TextUtils.isEmpty(mUrl)) {
            Map<String, String> map = new HashMap<>();
            if (payType.equals(WECHAT)) {
                map.put("Referer", RURL);
                mUrl = mUrl.concat("&redirect_url=" + RURL.concat("://"));
            }
            mDataBinding.wbvX5.setUrl(mUrl, map);
        } else if (!TextUtils.isEmpty(mData)) {
            mDataBinding.wbvX5.setData(mData, "text/html; charset=UTF-8", null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDataBinding.wbvX5.onActivityResult(requestCode, resultCode, data);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        return go_back && mDataBinding.wbvX5.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
//    }

    private Drawable drawSystemBar(@NonNull Activity mActivity, @NonNull int StartColor, @NonNull int EndColor) {
        GradientDrawable mGradientDrawable = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{StartColor,
                        EndColor});
        mGradientDrawable.setGradientCenter(0, 0);
        mGradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        mGradientDrawable.setShape(GradientDrawable.RECTANGLE);
        mGradientDrawable.setGradientRadius(mActivity.getWindowManager().getDefaultDisplay().getWidth() / 2);
        return mGradientDrawable;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_x5webview;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        if (null != getIntent()) {
            mTitle = getIntent().getStringExtra(TITLE);
            mUrl = getIntent().getStringExtra(URL);
            mData = getIntent().getStringExtra(DATA);
            payType = getIntent().getStringExtra(TYPE);
            go_back = getIntent().getBooleanExtra(GO_BACK, true);
        }

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleString = mTitle;
        setToolBar(mDataBinding.mAppBarLayout.mToolbar, mToolBarOptions, v -> {
//            if (go_back && null != mDataBinding.wbvX5 && mDataBinding.wbvX5.canGoBack() && System.currentTimeMillis() - lastPressM > 500) {
//                mDataBinding.wbvX5.goBack();
//                lastPressM = System.currentTimeMillis();
//            } else {
            finish();
//            }
        });

        getWindow().setFormat(PixelFormat.TRANSLUCENT);//设置窗口支持透明度

        if (null != mDataBinding.mProgressBar)
            mDataBinding.mProgressBar.setProgressDrawable(new ClipDrawable(drawSystemBar(this, Color.WHITE, getResources().getColor(R.color.colorAccent)), ClipDrawable.VERTICAL, ClipDrawable.HORIZONTAL));
        mDataBinding.wbvX5.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                final PayTask task = new PayTask(X5WebViewForAliPayActivity.this);
                boolean isIntercepted = task.payInterceptorWithUrl(s, true, new H5PayCallback() {
                    @Override
                    public void onPayResult(final H5PayResultModel result) {
                        // 支付结果返回
                        String code = result.getResultCode();

                        switch (code) {
                            case PAY_SUCCESS:
                                ToastUtils.showShort(getString(R.string.pay_result_success));
                                break;
                            case PAY_FAIL:
                                ToastUtils.showShort(getString(R.string.pay_result_fail));
                                break;
                            default:
                                ToastUtils.showShort(getString(R.string.pay_result_cancle));
                                break;
                        }
                        X5WebViewForAliPayActivity.this.finish();
                    }
                });
                if (!isIntercepted) {
                    webView.loadUrl(s);
                }
//                webView.loadUrl(s);
                return true;
            }

        });

        mDataBinding.wbvX5.setProgressBar(mDataBinding.mProgressBar)
                .setWebViewonLoadListener((view, url) -> {
                    if (null != mDataBinding && null != mDataBinding.mAppBarLayout && null != mDataBinding.mAppBarLayout.txtTitle && TextUtils.isEmpty(mTitle)) {
                        mDataBinding.mAppBarLayout.txtTitle.setText(view.getTitle());
                    }
                });
        loadPage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDataBinding.wbvX5.onPause();
        mDataBinding.wbvX5.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDataBinding.wbvX5.resumeTimers();
        mDataBinding.wbvX5.onResume();
    }

    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                dismissLoading();
            }
            mDataBinding.mProgressBar.setProgress(newProgress);

//            if (newProgress == 0) {
////                coolIndicatorLayout.hide();
//            } else if (newProgress > 0 && newProgress <= 10) {
////                coolIndicatorLayout.show();
//            } else if (newProgress > 10 && newProgress < 95) {
////                coolIndicatorLayout.setProgress(newProgress);
//            } else {
////                coolIndicatorLayout.setProgress(newProgress);
////                coolIndicatorLayout.hide();
//            }
        }

        // For Android >= 5.0
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//            DDToast.makeText(NestedScrollWebViewActivity.this, fileChooserParams.getAcceptTypes()[0] + "==");
            return true;
        }
    };
}
