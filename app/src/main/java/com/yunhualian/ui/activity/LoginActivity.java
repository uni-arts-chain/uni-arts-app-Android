package com.yunhualian.ui.activity;

import android.view.View;

import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivityLoginBinding;

public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener {

    private boolean isLogin = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        setAndroidNativeLightStatusBar(false, true, true);

        mDataBinding.login.setOnClickListener(this);
        mDataBinding.register.setOnClickListener(this);
        mDataBinding.forgetPsw.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.register:
                changeRegisterStatus();
                break;
            case R.id.login:
                changeLoginStatus();
                break;
            case R.id.forget_psw:
                startActivity(ResetPswActivity.class);
                break;

        }
    }

    private void changeRegisterStatus() {
        mDataBinding.bgLayout.setBackgroundResource(R.mipmap.bg_register);
        mDataBinding.loginLayout.setVisibility(View.GONE);
        mDataBinding.registerLayout.setVisibility(View.VISIBLE);
        mDataBinding.forgetPsw.setVisibility(View.GONE);
        mDataBinding.chbUserAgreement.setVisibility(View.VISIBLE);
        mDataBinding.forgetPsw.setVisibility(View.GONE);
    }

    private void changeLoginStatus() {
        mDataBinding.bgLayout.setBackgroundResource(R.mipmap.bg_denglu);
        mDataBinding.loginLayout.setVisibility(View.VISIBLE);
        mDataBinding.registerLayout.setVisibility(View.GONE);
        mDataBinding.forgetPsw.setVisibility(View.VISIBLE);
        mDataBinding.chbUserAgreement.setVisibility(View.GONE);
        mDataBinding.forgetPsw.setVisibility(View.VISIBLE);
    }
}