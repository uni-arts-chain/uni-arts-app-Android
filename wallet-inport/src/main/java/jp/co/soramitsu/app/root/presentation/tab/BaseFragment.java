package jp.co.soramitsu.app.root.presentation.tab;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import jp.co.soramitsu.inport.R;


/**
 * Synopsis     BaseFragment
 * Author		Mosr
 * Version		${VERSION}
 * Create 	    2020-06-19 20:55:49
 * Email  		intimatestranger@sina.cn
 */
public abstract class BaseFragment<T> extends Fragment {
    protected View rootView;
    protected T mBinding;
    private ViewDataBinding mViewDataBinding;
    protected ToolbarHelper2 mToolbarHelper;
    protected AppCompatActivity mActivity;
    protected Toast mToast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
        mBinding = null != mViewDataBinding ? (T) mViewDataBinding : null;
        rootView = null != mViewDataBinding ? mViewDataBinding.getRoot() : null;
        if (rootView == null)
            rootView = inflater.inflate(getLayoutResource(), container, false);
        return rootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPresenter();
        initView();
    }

    public void initToolbar(View view, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper2(mActivity, getView());

        mToolbarHelper.initToolbar(view);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack) {
        mToolbarHelper = new ToolbarHelper2(mActivity, getView());

        mToolbarHelper.initToolbar(title);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack, @ColorInt int resId) {
        mToolbarHelper = new ToolbarHelper2(mActivity, getView());

        mToolbarHelper.initToolbar(title, resId);
        setBackNavigation(hasBack);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    public void initToolbar(int resId, boolean hasBack) {
        initToolbar(getString(resId), hasBack);
    }

    public void setRightNavigation(String str, @DrawableRes int resId, ToolbarHelper2.OnSingleMenuItemClickListener listener) {
        if (null != mToolbarHelper)
            mToolbarHelper.setSingleMenu(str, resId, listener);
    }

    public void setLeftNavigation(@DrawableRes int resId, View.OnClickListener listener) {
        if (null != mToolbarHelper)
            mToolbarHelper.setBackNavigationIcon(resId, listener);
    }

    public void setRightNavigation(String str, @DrawableRes int resId, @ColorInt int _resId, ToolbarHelper2.OnSingleMenuItemClickListener listener) {
        if (null != mToolbarHelper)
            mToolbarHelper.setSingleMenu(str, resId, _resId, listener);
    }

    public void initToolbar(int resId, boolean hasBack, @ColorInt int colorResID) {
        initToolbar(getString(resId), hasBack, colorResID);
    }

    public void initToolbar(String title, boolean hasBack, @ColorInt int resId, @ColorInt int layoutColorId) {
        mToolbarHelper = new ToolbarHelper2(mActivity, getView());

        mToolbarHelper.initToolbar(title, resId, layoutColorId, -1001);
        setBackNavigation(hasBack);
    }

    public void initToolbar(String title, boolean hasBack, @ColorInt int resId, @ColorInt int layoutColorId, int mTitlegravity) {
        mToolbarHelper = new ToolbarHelper2(mActivity, getView());

        mToolbarHelper.initToolbar(title, resId, layoutColorId, mTitlegravity);
        setBackNavigation(hasBack);
    }

    public void setBackNavigation(boolean hasBack) {
        if (null != mToolbarHelper)
            mToolbarHelper.setBackNavigation(hasBack, (v) -> mActivity.onBackPressed());
    }

    /**
     * 获取布局文件
     *
     * @return
     */
    protected abstract int getLayoutResource();

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
    protected abstract void initView();


    /**
     * 通过Class跳转界面
     **/
    public void startActivity(Class<?> cls) {
        startActivity(cls, null);
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
        intent.setClass(getActivity(), cls);
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
        intent.setClass(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    // Fragment页面onResume函数重载
    public void onResume() {
        super.onResume();
    }

    // Fragment页面onResume函数重载
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
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
            startActivity(new Intent(getActivity(), cls));
    }

    /**
     * 不重复打开并校验登录状态
     *
     * @param cls
     */
    public void startActivitySingleCheckLogin(Class<?> cls) {
        /*if (!GuessBallApplication.isLogin()) {
            startActivitySingle(LoginActivity.class);
            return;
        }*/
        startActivitySingle(cls);
    }

    public void setToolBar(Toolbar mToolbar, ToolBarOptions options) {
        setToolBar(mToolbar, options, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    public int setMenuRes() {
        return 0;
    }

    public void setTitle(int resId) {
        if (resId != 0)
            setTitle(getString(resId));
    }

    public void setTitle(String title) {
        if (null != mToolbarHelper)
            mToolbarHelper.setTitle(title);
    }

    public void setToolBar(Toolbar mToolbar, ToolBarOptions options, View.OnClickListener mOnClickListener) {
        if (null == mToolbar) {
            return;
        }
        if (null == options) {
            return;
        }

        TextView txt_title = mToolbar.findViewById(R.id.txt_title);

        if (null != txt_title && options.titleId != 0) {
            txt_title.setText(options.titleId);
        }
        if (null != txt_title && !TextUtils.isEmpty(options.titleString)) {
            txt_title.setText(options.titleString);
        }
        if (options.logoId != 0) {
            mToolbar.setLogo(options.logoId);
        }

        if (null != getActivity() && !getActivity().isFinishing() && getActivity() instanceof AppCompatActivity) {
            if (setMenuRes() != 0) {
                mToolbar.inflateMenu(setMenuRes());

                MenuItem btn_right = mToolbar.getMenu().findItem(R.id.action_messages);

                if (options.rightPicRes != 0)
                    btn_right.setIcon(options.rightPicRes);

                if (null != options.onRightPicClick)
                    mToolbar.setOnMenuItemClickListener(options.onRightPicClick);

                if (options.rightTextString != 0)
                    btn_right.setTitle(getContext().getString(options.rightTextString));

                if (null != options.onRightTextClick)
                    mToolbar.setOnMenuItemClickListener(options.onRightTextClick);

            } /*else {
                throw new IllegalStateException("please set menu res");
            }*/

//            Menu menu = mToolbar.getMenu();
//            AppCompatActivity mAppCompatActivity = ((AppCompatActivity) getActivity());
//            mToolbar.getMenu().clear();
//            AppCompatActivity mAppCompatActivity = ((AppCompatActivity) getActivity());
//            mAppCompatActivity.setSupportActionBar(mToolbar);
//            ActionBar mSupportActionBar = mAppCompatActivity.getSupportActionBar();
//            mSupportActionBar.setDisplayHomeAsUpEnabled(true);
//            mSupportActionBar.setDisplayShowTitleEnabled(false);
        }

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

//    int getmColor(@ColorRes int id) {
//        return YunApplication.getInstance().getResources().getColor(id);
//    }

    public void showLoading(int txt) {
        if (isAdded())
            showLoading(0 != txt ? getResources().getString(txt) : "");
    }

    public void showLoading(String txt) {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).showLoading(txt);
        }
    }

    public void dismissLoading() {
        if (getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).dismissLoading();
        }
    }

    protected void showShortToast(final int resId) {
        mActivity.runOnUiThread(() -> {
            if (null == mToast)
                mToast = Toast.makeText(mActivity, getResources().getString(resId), Toast.LENGTH_SHORT);
            else
                mToast.setText(resId);

            mToast.show();
        });
    }

    protected void showShortToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            mActivity.runOnUiThread(() -> {
                if (null == mToast)
                    mToast = Toast.makeText(mActivity, text, Toast.LENGTH_SHORT);
                else
                    mToast.setText(text);

                mToast.show();
            });
        }
    }

    protected void showLongToast(final int resId) {
        mActivity.runOnUiThread(() -> {
            if (null == mToast)
                mToast = Toast.makeText(mActivity, getResources().getString(resId), Toast.LENGTH_SHORT);
            else
                mToast.setText(resId);

            mToast.show();
        });
    }

    protected void showLongToast(final String text) {
        if (!TextUtils.isEmpty(text)) {
            mActivity.runOnUiThread(() -> {
                if (null == mToast)
                    mToast = Toast.makeText(mActivity, text, Toast.LENGTH_SHORT);
                else
                    mToast.setText(text);

                mToast.show();
            });
        }
    }
}

