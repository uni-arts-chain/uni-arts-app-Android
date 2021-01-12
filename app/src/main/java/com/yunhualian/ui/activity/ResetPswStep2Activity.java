package com.yunhualian.ui.activity;


import com.blankj.utilcode.util.ToastUtils;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivityRestPswStep2Binding;

public class ResetPswStep2Activity extends BaseActivity<ActivityRestPswStep2Binding> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_rest_psw_step2;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        mDataBinding.confirmButton.setOnClickListener(view -> ToastUtils.showLong("confirm"));

    }
}