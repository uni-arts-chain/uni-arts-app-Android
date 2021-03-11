package com.yunhualian.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.WindowManager;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.CacheDiskStaticUtils;
import com.yunhualian.MainActivity;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.ExtraConstant;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    guide_flag = CacheDiskStaticUtils.getString(ExtraConstant.KEY_GUIDE_FLAG);

                    Thread.sleep(1000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            skipTomMain();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        YunApplication.refreshUser(true);
    }

    private void skipTomMain() {
        startActivity(TextUtils.equals("1", guide_flag) ? RootActivity.class : GuideActivity.class);
        SplashActivity.this.finish();
        overridePendingTransition(R.anim.boxing_fade_in, R.anim.boxing_fade_out);
    }
}

