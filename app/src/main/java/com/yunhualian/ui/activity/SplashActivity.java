package com.yunhualian.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.yunhualian.MainActivity;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.YunApplication;
import com.yunhualian.utils.SharedPreUtils;

import jp.co.soramitsu.app.root.presentation.RootActivity;

/**
 * Synopsis     ${SYNOPSIS}
 * Author		Mosr
 * Version		${VERSION}
 * Create 	    2019.02.02 14:56
 * Email  		intimatestranger@sina.cn
 */
public class SplashActivity extends BaseActivity {
    private String guide_flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initPresenter() {
    }

    @Override
    public void initView() {
        skipTomMain();
//        YunApplication.refreshUser(true);
    }

    private void skipTomMain() {
//        startActivity(RootActivity.class);
        if (!TextUtils.isEmpty(SharedPreUtils.getString(this, SharedPreUtils.FIRST))) {
            startActivity(MainActivity.class);
        } else {
            startActivity(RootActivity.class);
            SharedPreUtils.setString(this, SharedPreUtils.FIRST, "old");
        }
        SplashActivity.this.finish();
        overridePendingTransition(R.anim.boxing_fade_in, R.anim.boxing_fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

