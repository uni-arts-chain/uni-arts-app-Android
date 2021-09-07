package com.gammaray.ui.x5;


import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.LogUtils;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.databinding.ActivityWebViewBinding;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class WebViewActivity extends BaseActivity<ActivityWebViewBinding> {
    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String TYPE = "payType";
    public static final String DATA = "data";
    public static final String GO_BACK = "go_back";
    public static final String FULLSCREEN = "full_screen";
    public static String RURL = "http://nft.qianchishuke.com";
    private String mTitle;
    private String mUrl;
    private String payType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_web_view;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        if (null != getIntent()) {
            mTitle = getIntent().getStringExtra(TITLE);
            mUrl = getIntent().getStringExtra(URL);
            payType = getIntent().getStringExtra(TYPE);
        }
        Map<String, String> header = new HashMap<>();
        header.put("Referer", "http://nft.qianchishuke.com");
        mDataBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Map<String, String> header = new HashMap<>();
                header.put("Referer", "http://nft.qianchishuke.com");
                if (url.startsWith("weixin://")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url, header);
                return true;
            }
        });
        mUrl = mUrl.concat("&redirect_url=" + RURL.concat("://"));
        LogUtils.e("url == " + mUrl);
        mDataBinding.webView.loadUrl(URLEncoder.encode(mUrl), header);
    }
}