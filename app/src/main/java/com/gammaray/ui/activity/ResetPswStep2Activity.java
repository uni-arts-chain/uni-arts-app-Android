package com.gammaray.ui.activity;


import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.databinding.ActivityRestPswStep2Binding;

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