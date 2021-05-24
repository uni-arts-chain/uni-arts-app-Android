package com.yunhualian.base;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.bumptech.glide.Glide;
import com.bumptech.glide.util.Util;
import com.umeng.analytics.MobclickAgent;
import com.yunhualian.R;
import com.yunhualian.utils.StatusBarCompat;
import com.yunhualian.widget.LoadingDialog;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;


/**
 * Synopsis     BaseActivity
 * Author		Mosr
 * Version		${VERSION}
 * Create 	    2020-06-19 20:55:49
 * Email  		intimatestranger@sina.cn
 */
public abstract class BaseActivity<T> extends AppCompatActivity {
    protected T mDataBinding;
    private LoadingDialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        initContenView();
        initPresenter();
        initView();
        setAndroidNativeLightStatusBar(true, false, false);
    }

    private void initContenView() {
        ActivityManager.getAppManager().addActivity(this);
        ViewDataBinding mViewDataBinding = null;
        try {
            mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
            mDataBinding = null != mViewDataBinding ? (T) mViewDataBinding : null;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (null == mViewDataBinding || null == mViewDataBinding.getRoot()) {
            setContentView(getLayoutId());
        }
    }

    /**
     * 获取布局文件
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 简单页面无需mvp就不用管此方法即可,完美兼容各种实际场景的变通
     *
     * @return
     */
    public abstract void initPresenter();

    /**
     * 初始化view
     *
     * @return
     */
    public abstract void initView();


    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
        StatusBarCompat.setTranslucentStatus(this, true);
    }


    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar() {
        StatusBarCompat.translucentStatusBar(this);
    }


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Class跳转界面
     **/
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void startActivityAnim(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    /**
     * 通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, int requestCode) {
        startActivityForResult(cls, null, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);

    }

    /**
     * 不重复打开
     *
     * @param cls
     */
    public void startActivitySingle(Class<?> cls) {
        if (ActivityManager.getAppManager().isOpenActivity(cls))
            ActivityManager.getAppManager().returnToActivity(cls);
        else
            startActivity(new Intent(this, cls));
    }


    // Activity页面onResume函数重载
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    // Activity页面onResume函数重载
    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fixInputMethodManagerLeak(this);
        //Glide 回收
        if (Util.isOnMainThread() && !this.isFinishing()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && !this.isDestroyed()) {
                Glide.with(this).pauseRequests();
            }
        }
        ActivityManager.getAppManager().finishActivity(this);
    }


    /**
     * 内存泄露现象及解决
     *
     * @param destContext
     */
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) return;
        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
        Field f;
        Object obj_get;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    f.setAccessible(true);
                }
                obj_get = f.get(imm);
                if (obj_get != null && obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    // 被InputMethodManager持有引用的context是想要目标销毁的
                    if (v_get.getContext() == destContext) {
                        // 置空，破坏掉path to gc节点
                        f.set(imm, null);
                    } else {
                        // 不是想要目标销毁的，即为又进了另一层界面了，不要处理，避免影响原逻辑,也就不用继续for循环了
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }


    public void setToolBar(Toolbar mToolbar, ToolBarOptions options) {
        setToolBar(mToolbar, options, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void setToolBar(Toolbar mToolbar, ToolBarOptions options, View.OnClickListener mOnClickListener) {
        if (null == mToolbar) {
            return;
        }
        if (null == options) {
            return;
        }

        TextView txt_title = mToolbar.findViewById(R.id.txt_title);
        TextView txt_right = mToolbar.findViewById(R.id.txt_right);
        if (null != txt_right && options.rightTextString != 0) {
            txt_right.setText(options.rightTextString);
        }
        if (null != txt_title && options.titleId != 0) {
            txt_title.setText(options.titleId);
        }
        if (null != txt_title && options.titleTxtColor != 0) {
            txt_title.setTextColor(options.titleTxtColor);
        }
        if (null != txt_title && !TextUtils.isEmpty(options.titleString)) {
            txt_title.setText(options.titleString);
        }
        if (options.logoId != 0) {
            mToolbar.setLogo(options.logoId);
        }
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (options.isNeedNavigate && 0 != options.navigateId) {
            mToolbar.setNavigationIcon(options.navigateId);
//            mToolbar.setContentInsetStartWithNavigation(0);
            mToolbar.setNavigationOnClickListener(mOnClickListener);
        } else if (options.isNeedClose && 0 != options.closeId) {
            mToolbar.setNavigationIcon(options.closeId);
            mToolbar.setNavigationOnClickListener(mOnClickListener);
        } else if (options.isNeedBack && 0 != options.backId) {
            mToolbar.setNavigationIcon(options.backId);
            mToolbar.setNavigationOnClickListener(mOnClickListener);
        } else {
            mToolbar.setNavigationIcon(null);
        }
    }

    public void showLoading(int txt) {
        showLoading(0 != txt ? getResources().getString(txt) : "");
    }

    public void showLoading(String txt) {
        if (null == mLoadingDialog)
            mLoadingDialog = new LoadingDialog(this);

        if (!isFinishing() && !mLoadingDialog.isShowing()) {
            mLoadingDialog.setLoadingText(txt);
            mLoadingDialog.show();
        }
    }

    public void dismissLoading() {
        if (null != mLoadingDialog) {
            mLoadingDialog.dismiss();
        }
    }

    public int getmColor(@ColorRes int id) {
        return YunApplication.getInstance().getResources().getColor(id);
    }

    public String getmString(@StringRes int id) {
        return YunApplication.getInstance().getResources().getString(id);
    }

    protected void setAndroidNativeLightStatusBar(boolean dark, boolean fullscreen, boolean transparentStatusBar) {
        if (transparentStatusBar) {
            transparentStatusBar();
        }
        View decor = getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(fullscreen ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(fullscreen ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE : View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    void transparentStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
