package com.yunhualian.ui.activity;


import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivityRestPswBinding;

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