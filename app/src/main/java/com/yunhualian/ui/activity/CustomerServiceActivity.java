package com.yunhualian.ui.activity;


import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityCustomerServiceBinding;

public class CustomerServiceActivity extends BaseActivity<ActivityCustomerServiceBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_customer_service;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.service;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
    }
}