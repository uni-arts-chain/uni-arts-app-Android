package com.gammaray.ui.x5;

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

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.base.YunApplication;
import com.gammaray.databinding.ActivityX5webviewBinding;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * Synopsis     com.cashfishvay.app.activity.ui.x5.X5WebView
 * Author		Mosr
 * Version		1.0
 * Create 	    2019/12/28 08:33
 * Email  		intimatestranger@sina.cn
 */
public class X5WebViewActivity extends BaseActivity<ActivityX5webviewBinding> {
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
    String redirectUrl = "";
    String prePayId = "";
    private boolean needReloadResult = false;

    private void loadPage() {
        if (!TextUtils.isEmpty(mUrl)) {
            Map<String, String> map = new HashMap<>();
            if (payType.equals(WECHAT)) {
                map.put("Referer", RURL);
                int charIndex = mUrl.indexOf("?");
                String paramUrl = mUrl.substring(charIndex + 1, mUrl.length());
                String[] paramsArray = paramUrl.split("&");
                HashMap<String, String> parmMap = new HashMap<>();
                for (String param : paramsArray) {
                    String[] argV = param.split("=");
                    parmMap.put(argV[0], argV[1]);
                }
                if (parmMap.size() > 0) {
                    redirectUrl = parmMap.get("redirect_url");
                    prePayId = parmMap.get("prepay_id");
                }
                if (mUrl.contains("redirect_url")) {
                    mUrl = mUrl.replace(redirectUrl, RURL.concat("://"));
                } else
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
                Log.e("test", "test" + "支付Url跳转处理：" + s);
                Map extraHeaders = new HashMap();
                if (payType.equals(WECHAT))
                    extraHeaders.put("Referer", RURL); //例如：https://www.pingxx.com
                if (s.startsWith("weixin://")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(s));
                        startActivity(intent);
                        needReloadResult = true;
                    } catch (Exception e) {
                        ToastUtils.showShort("未安装微信");
                        finish();
                    }

                    return true;
                } else if (s.startsWith("http://")) {

                    if (!TextUtils.isEmpty(redirectUrl) && needReloadResult && !TextUtils.isEmpty(prePayId)) {
                        s = redirectUrl.concat("?token=".concat(YunApplication.getmUserVo().getToken()).concat("&prepay_id=").concat(prePayId));
                        try {
                            s = URLDecoder.decode(s, "utf-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                webView.loadUrl(s, extraHeaders);
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
        Log.e("test", "test" + "支付Url跳转处理：");

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
