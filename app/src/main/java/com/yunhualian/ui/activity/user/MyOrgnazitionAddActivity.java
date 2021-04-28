package com.yunhualian.ui.activity.user;

import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityMyOrgnazitionBinding;

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