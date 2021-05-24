package com.yunhualian.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityAboutUsBinding;

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