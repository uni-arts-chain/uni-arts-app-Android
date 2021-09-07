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
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityX5webviewBinding;


/**
 * Synopsis     com.cashfishvay.app.activity.ui.x5.X5WebView
 * Author		Mosr
 * Version		1.0
 * Create 	    2019/12/28 08:33
 * Email  		intimatestranger@sina.cn
 */
public class ExplorerWebViewActivity extends BaseActivity<ActivityX5webviewBinding> {
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String DATA = "data";
    public static final String GO_BACK = "go_back";
    public static final String FULLSCREEN = "full_screen";
    LinearLayout linParent;
    private String mTitle;
    private String mUrl;
    private String mData;
    private long lastPressM;
    private boolean go_back = true;


    private void loadPage() {
        if (!TextUtils.isEmpty(mUrl)) {
            mDataBinding.wbvX5.setUrl(mUrl);
        } else if (!TextUtils.isEmpty(mData)) {
            mDataBinding.wbvX5.setData(mData, "text/html; charset=UTF-8", null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mDataBinding.wbvX5.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return go_back && mDataBinding.wbvX5.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
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
            go_back = getIntent().getBooleanExtra(GO_BACK, true);
        }

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleString = mTitle;
        setToolBar(mDataBinding.mAppBarLayout.mToolbar, mToolBarOptions, v -> {
            if (go_back && null != mDataBinding.wbvX5 && mDataBinding.wbvX5.canGoBack() && System.currentTimeMillis() - lastPressM > 500) {
                mDataBinding.wbvX5.goBack();
                lastPressM = System.currentTimeMillis();
            } else {
                finish();
            }
        });

        getWindow().setFormat(PixelFormat.TRANSLUCENT);//设置窗口支持透明度

        if (null != mDataBinding.mProgressBar)
            mDataBinding.mProgressBar.setProgressDrawable(new ClipDrawable(drawSystemBar(this, Color.WHITE, getResources().getColor(R.color.colorAccent)), ClipDrawable.VERTICAL, ClipDrawable.HORIZONTAL));
        mDataBinding.wbvX5.setWebChromeClient(mWebChromeClient);
        mDataBinding.wbvX5.addJavascriptInterface(new JsToAndroid(ExplorerWebViewActivity.this),"jsMethod");
        mDataBinding.wbvX5.setProgressBar(mDataBinding.mProgressBar)
                .setWebViewonLoadListener((view, url) -> {
                    if (null != mDataBinding && null != mDataBinding.mAppBarLayout && null != mDataBinding.mAppBarLayout.txtTitle && TextUtils.isEmpty(mTitle)) {
                        mDataBinding.mAppBarLayout.txtTitle.setText(view.getTitle());
                    }
                });
        loadPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    protected WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                dismissLoading();
            }
            mDataBinding.mProgressBar.setProgress(newProgress);

        }

        // For Android >= 5.0
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//            DDToast.makeText(NestedScrollWebViewActivity.this, fileChooserParams.getAcceptTypes()[0] + "==");
            return true;
        }
    };
}
