package com.gammaray.ui.activity;


import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.databinding.ActivityRestPswBinding;

public class ResetPswActivity extends BaseActivity<ActivityRestPswBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_rest_psw;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mDataBinding.next.setOnClickListener(view -> startActivity(ResetPswStep2Activity.class));
    }
}