package com.gammaray.ui.activity;

import android.os.CountDownTimer;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.constant.ExtraConstant;
import com.gammaray.databinding.ActivityLoginBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;

import retrofit2.Call;
import retrofit2.Response;

//@Route(path = ArouterModelPath.MAIN)
public class LoginActivity extends BaseActivity<ActivityLoginBinding> implements View.OnClickListener {

    private boolean isLogin = false;

    private CountDownTimer mCountDownTimer;
    private static int defaultTimes = 60 * ExtraConstant.DEFAULT_TIME_EPLI;
    private static int timeStep = ExtraConstant.DEFAULT_TIME_EPLI;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initPresenter() {

    }

    private void initCountDownTimer() {
        mCountDownTimer = new CountDownTimer(defaultTimes, timeStep) {
            @Override
            public void onTick(long millisUntilFinished) {
                mDataBinding.getCode.setText(getString(R.string.reacquire_code, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                mDataBinding.getCode.setText(R.string.register_txt_code);
                mDataBinding.getCode.setEnabled(true);
                mDataBinding.getCode.setTextColor(getColor(R.color.apply_sign));
            }
        };
    }

    private boolean bSendCodeTag;

    private void startCountDownTimer() {
        bSendCodeTag = true;
        if (null == mCountDownTimer)
            initCountDownTimer();
        mCountDownTimer.start();
        mDataBinding.getCode.setTextColor(getColor(R.color.picture_profile));
        mDataBinding.getCode.setEnabled(false);
//        mDataBinding.edtArCode.requestFocus();
    }

    @Override
    public void initView() {
        setAndroidNativeLightStatusBar(false, true, true);

        mDataBinding.login.setOnClickListener(this);
        mDataBinding.register.setOnClickListener(this);
        mDataBinding.forgetPsw.setOnClickListener(this);
        mDataBinding.getCode.setOnClickListener(this);
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
            case R.id.get_code:
                getCode("8618651090153", "signup");
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

    public void getCode(String phone_number, String send_type) {
        RequestManager.instance().sendSmsCode(phone_number, send_type, new MinerCallback<BaseResponseVo>() {
            @Override
            public void onSuccess(Call<BaseResponseVo> call, Response<BaseResponseVo> response) {
                if (response.isSuccessful()) {
                    ToastUtils.showLong(R.string.send_code_success);
                }
            }

            @Override
            public void onError(Call<BaseResponseVo> call, Response<BaseResponseVo> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }
}