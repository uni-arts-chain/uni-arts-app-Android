package com.gammaray.ui.x5;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.blankj.utilcode.util.ToastUtils;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.gammaray.R;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;


import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Synopsis     com.cashfishvay.app.activity.ui.x5.X5WebView
 * Author		Mosr
 * Version		1.0
 * Create 	    2019/12/28 08:12
 * Email  		intimatestranger@sina.cn
 */
public class X5WebView extends WebView {
    private static final String TAG = X5WebView.class.getSimpleName();
    //返回键上次点击的时间
    private long mLastClickTime;
    //进度条
    private ProgressBar mProgressBar;
    //文件上传
    private ValueCallback<Uri> mUploadFile;
    //加载状态监听
    private WebViewOnLoadListener mWebViewonLoadListener;

    public X5WebView(Context context) {
        super(context);
    }

    public X5WebView(final Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initWebClient();
        //initJS();
        initDownLoad();
        initImageEvent();
    }

    public X5WebView(Context fixedContext, AttributeSet attrs, int defStyle) {
        super(fixedContext, attrs, defStyle);
    }


    public X5WebView setUrl(String mUrl, Map<String, String> mHeadMap) {
        this.loadUrl(mUrl, mHeadMap);
        return this;
    }

    public X5WebView setUrl(String mUrl) {
        this.loadUrl(mUrl);
        return this;
    }

    public X5WebView setData(String mData, String mimeType, String encoding) {
        this.loadData(mData, mimeType, encoding);
        return this;
    }

    public X5WebView setProgressBar(ProgressBar mProgressBar) {
        this.mProgressBar = mProgressBar;
        return this;
    }


    public void setWebViewonLoadListener(WebViewOnLoadListener mWebViewonLoadListener) {
        this.mWebViewonLoadListener = mWebViewonLoadListener;
    }

    /**
     * 初始化图片长按时间
     */
    private void initImageEvent() {
        this.getView().setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                HitTestResult hitTestResult = X5WebView.this.getHitTestResult();
//                final String path = mResourceUrl = hitTestResult.getExtra();
                switch (hitTestResult.getType()) {
                    case HitTestResult.IMAGE_TYPE://获取点击的标签是否为图片
                        break;
                    case HitTestResult.SRC_IMAGE_ANCHOR_TYPE://获取点击的标签是否为图片
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 初始化下载监听
     */
    private void initDownLoad() {
        this.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownloadStart(final String url, String userAgent, final String contentDisposition,
                                        String arg3, long arg4) {
                if (null == getContext()) {
                    return;
                }
                if (!(getContext() instanceof Activity)) {
                    return;
                }
                if (((Activity) getContext()).isFinishing()) {
                    return;
                }
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle("提示")
                        .setMessage("确定下载？")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        try {
                                            Uri uri = Uri.parse(url);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                            getContext().startActivity(intent);
                                            ((Activity) getContext()).finish();
                                        } catch (ActivityNotFoundException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        ((Activity) getContext()).finish();
                                    }
                                }).show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color._FFFFFF));
            }
        });
    }


    /**
     * 初始化Client
     */
    private void initWebClient() {
        WebSettings webSetting = this.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(getContext().getDir("minerappcache", 0).getPath());
        webSetting.setDatabasePath(getContext().getDir("minerdatabases", 0).getPath());
        webSetting.setGeolocationDatabasePath(getContext().getDir("minergeolocation", 0)
                .getPath());
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        String mUserAgent = webSetting.getUserAgentString();
        mUserAgent += " miner";
        webSetting.setUserAgentString(mUserAgent);

        /**
         * 防止加载网页时调起系统浏览器
         */
        this.setWebViewClient(new WebViewClient() {

                                  @Override
                                  public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                                      sslErrorHandler.proceed();
                                  }

                                  @Override
                                  public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                      Log.i("mosr", url);
                                      if (null == getContext()) {
                                          return false;
                                      }
                                      try {
                                          if (!TextUtils.isEmpty(url)) {
                                              //处理intent协议
                                              if (url.startsWith("intent://")) {
                                                  Intent intent;
                                                  try {
                                                      intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                                                      if (null == intent) {
                                                          return false;
                                                      }
                                                      intent.addCategory("android.intent.category.BROWSABLE");
                                                      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                      intent.setComponent(null);
                                                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                                                          intent.setSelector(null);
                                                      }
                                                      List<ResolveInfo> resolves = getContext().getPackageManager().queryIntentActivities(intent, 0);
                                                      if (null != resolves && !resolves.isEmpty()) {
                                                          ((Activity) getContext()).startActivityIfNeeded(intent, -1);
                                                      } else {
                                                          ToastUtils.showShort("尚未安装此应用！");
                                                      }
                                                      return true;
                                                  } catch (URISyntaxException e) {
                                                      e.printStackTrace();
                                                  }
                                              }
                                              // 处理自定义scheme协议
                                              if (!url.startsWith("http") && !url.startsWith("https")) {
                                                  if (TextUtils.equals(url.toString(), "about:blank")) {
                                                      ToastUtils.showShort("暂不支持！");
                                                      return true;
                                                  }
                                                  try {
                                                      final Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                                                      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                                      intent.addCategory("android.intent.category.BROWSABLE");
                                                      intent.setComponent(null);
                                                      if (null != getContext() && null != intent) {
                                                          getContext().startActivity(intent);
                                                      }
                                                  } catch (Exception e) {
                                                      // 没有安装的情况
                                                      e.printStackTrace();
                                                      ToastUtils.showShort("尚未安装此应用！");
                                                  }
                                                  return true;
                                              }
                                          }
                                      } catch (Exception e) {
                                          e.printStackTrace();
                                      }
                                      return false;
                                  }

                                  @Override
                                  public WebResourceResponse shouldInterceptRequest(WebView view,
                                                                                    WebResourceRequest request) {
                                      Log.e("mosr", request.getUrl().toString());
                                      return super.shouldInterceptRequest(view, request);
                                  }


                                  @Override
                                  public void onPageFinished(WebView view, String url) {
                                      super.onPageFinished(view, url);
                                      if (url.startsWith("wxp://") || url.startsWith("https://u.wechat.com/") || TextUtils.equals(url.toString(), "about:blank")) {
                                          ToastUtils.showShort("暂不支持！");
                                      }
                                      if (null != mWebViewonLoadListener) {
                                          mWebViewonLoadListener.onLoadFinish(view, url);
                                      }
                                  }
                              }

        );

        this.setWebChromeClient(new WebChromeClient() {

                                    /**更新进度条
                                     * @param webView
                                     * @param i
                                     */
                                    @Override
                                    public void onProgressChanged(WebView webView, int i) {

                                        if (null != mProgressBar) {
                                            if (i != 100 && mProgressBar.getVisibility() == GONE) {
                                                mProgressBar.setVisibility(VISIBLE);
                                            } else if (i == 100 && mProgressBar.getVisibility() == VISIBLE) {
                                                mProgressBar.setVisibility(GONE);
                                            }
                                            mProgressBar.setProgress(i);
                                        }
                                        super.onProgressChanged(webView, i);
                                    }

                                    @Override
                                    public boolean onJsConfirm(WebView arg0, String arg1, String arg2, JsResult arg3) {
                                        return super.onJsConfirm(arg0, arg1, arg2, arg3);
                                    }

                                    View myVideoView;
                                    View myNormalView;
                                    IX5WebChromeClient.CustomViewCallback callback;

                                    /**
                                     * 全屏播放配置
                                     */
                                    @Override
                                    public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback
                                            customViewCallback) {
                                        if (null == view) {
                                            return;
                                        }
                                        ViewGroup viewGroup = null != getParent() && getParent() instanceof ViewGroup ? (ViewGroup) getParent() : null;
                                        if (null != viewGroup) {
                                            viewGroup.addView(view);
                                            myVideoView = view;
                                        }
                                        callback = customViewCallback;
                                    }

                                    @Override
                                    public void onHideCustomView() {
                                        if (callback != null) {
                                            callback.onCustomViewHidden();
                                            callback = null;
                                        }
                                        if (myVideoView != null && null != myVideoView.getParent() && myVideoView.getParent() instanceof ViewGroup) {
                                            ViewGroup viewGroup = (ViewGroup) myVideoView.getParent();
                                            viewGroup.removeView(myVideoView);
                                            if (null != myNormalView) {
                                                viewGroup.addView(myNormalView);
                                            }
                                        }
                                    }

                                    @Override
                                    public boolean onShowFileChooser(WebView arg0,
                                                                     ValueCallback<Uri[]> arg1, FileChooserParams arg2) {
                                        return super.onShowFileChooser(arg0, arg1, arg2);
                                    }

                                    /**
                                     * 文件选择
                                     * @param uploadFile
                                     * @param acceptType
                                     * @param captureType
                                     */
                                    @Override
                                    public void openFileChooser(ValueCallback<Uri> uploadFile, String
                                            acceptType, String captureType) {

                                        mUploadFile = uploadFile;
                                        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                                        i.addCategory(Intent.CATEGORY_OPENABLE);
                                        i.setType(TextUtils.isEmpty(acceptType) ? "*/*" : acceptType);//"*/*" 表示全部类型文件
                                        ((Activity) getContext()).startActivityForResult(Intent.createChooser(i, "选择文件"), 0);
                                    }


                                    @Override
                                    public boolean onJsAlert(WebView arg0, String arg1, String arg2, JsResult arg3) {
                                        return super.onJsAlert(null, "www.baidu.com", "aa", arg3);
                                    }

                                    /**
                                     * 对应js 的通知弹框 ，可以用来实现js 和 android之间的通信
                                     */


                                    @Override
                                    public void onReceivedTitle(WebView arg0, final String arg1) {
                                        super.onReceivedTitle(arg0, arg1);
                                    }
                                }

        );
    }

    /**
     * 文件选择回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    if (null != mUploadFile) {
                        Uri result = data == null || resultCode != RESULT_OK ? null
                                : data.getData();
                        mUploadFile.onReceiveValue(result);
                        mUploadFile = null;
                    }
                    break;
                case 1:
                    Uri uri = data.getData();
                    String path = uri.getPath();
                    break;
                default:
                    break;
            }
        } else if (resultCode == RESULT_CANCELED) {
            if (null != mUploadFile) {
                mUploadFile.onReceiveValue(null);
                mUploadFile = null;
            }

        }
    }

    /**
     * 返回键事件处理
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && System.currentTimeMillis() - mLastClickTime > 500
                && this != null && this.canGoBack()) {
            this.goBack();
            mLastClickTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    public interface WebViewJavaScriptFunction {

        void onJsFunctionCalled(String tag);
    }

    /**
     * webView 加载监听
     */
    public interface WebViewOnLoadListener {

        void onLoadFinish(WebView view, String url);
    }

    public static String getTAG() {
        return TAG;
    }
}
