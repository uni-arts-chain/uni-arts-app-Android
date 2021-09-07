package com.gammaray.ui.activity.user;

import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityMyOrgnazitionBinding;

/*
 * 我的机构
 * */
public class MyOrgnazitionAddActivity extends BaseActivity<ActivityMyOrgnazitionBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_my_orgnazition;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.my_orgnaziton;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
    }
}