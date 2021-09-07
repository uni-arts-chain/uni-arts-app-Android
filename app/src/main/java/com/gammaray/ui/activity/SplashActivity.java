package com.gammaray.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.WindowManager;

import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.gammaray.MainActivity;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.constant.ExtraConstant;
import com.gammaray.databinding.ActivitySplashBinding;
import com.gammaray.utils.LoadOneTimeGifUtils;

/**
 * Synopsis     ${SYNOPSIS}
 * Author		Mosr
 * Version		${VERSION}
 * Create 	    2019.02.02 14:56
 * Email  		intimatestranger@sina.cn
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> implements LoadOneTimeGifUtils.GifListener {
    private String guide_flag;
    private long default_time = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setTheme(R.style.SplashTheme);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            skipTomMain();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        guide_flag = CacheDiskStaticUtils.getString(ExtraConstant.KEY_GUIDE_FLAG);
//        handler.sendEmptyMessageDelayed(0, default_time);
        LoadOneTimeGifUtils.loadOneTimeGif(this,R.drawable.app_launch,mDataBinding.imgSplash,this);

    }

    private void skipTomMain() {
        startActivity(TextUtils.equals("1", guide_flag) ? MainActivity.class : GuideActivity.class);
        SplashActivity.this.finish();
        overridePendingTransition(R.anim.boxing_fade_in, R.anim.boxing_fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void gifPlayComplete() {
        skipTomMain();
    }
}

