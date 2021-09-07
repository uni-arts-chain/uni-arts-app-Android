package com.gammaray.ui;

import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityAboutUsBinding;

public class AboutUsActivity extends BaseActivity<ActivityAboutUsBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.about_us;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

    }
}