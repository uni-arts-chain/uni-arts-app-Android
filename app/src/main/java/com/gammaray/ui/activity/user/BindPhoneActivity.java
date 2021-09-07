package com.gammaray.ui.activity.user;


import android.os.CountDownTimer;
import android.text.TextUtils;

import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.base.YunApplication;
import com.gammaray.constant.ExtraConstant;
import com.gammaray.databinding.ActivityBindPhoneBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.utils.CheckStringUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class BindPhoneActivity extends BaseActivity<ActivityBindPhoneBinding> {

    private CountDownTimer mCountDownTimer;

    private String acount;

    private String codeMsg;
    private static int defaultTimes = 60 * ExtraConstant.DEFAULT_TIME_EPLI;
    private static int timeStep = ExtraConstant.DEFAULT_TIME_EPLI;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_phone;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.bind_phone;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        mDataBinding.getCode.setOnClickListener(v -> {
            if (checkInputString()) {
                getCode(acount);
            }
        });
        mDataBinding.bindAction.setOnClickListener(v -> {

            if (checkCode()) {
                bindPhone();
            }

        });
    }

    private boolean checkCode() {
        //756845
        if (!TextUtils.isEmpty(mDataBinding.registerCode.getText())) {
            String cd = mDataBinding.registerCode.getText().toString();
            if (mDataBinding.registerCode.getText().length() == 6) {
                codeMsg = mDataBinding.registerCode.getText().toString();
            } else {
                ToastUtils.showShortToast(this, getString(R.string.register_currently_code));
                return false;
            }
        } else {
            ToastUtils.showShortToast(this, getString(R.string.register_hint_code));
            return false;
        }
        return true;

    }

    public boolean checkInputString() {

        if (!TextUtils.isEmpty(mDataBinding.registerPhone.getText())) {
//            type = CheckStringUtils.isPhone(mDataBinding.registerPhone.getText().toString()) ? TYPE_PHONE : TYPE_MAIL;
//            acount = mDataBinding.registerPhone.getText().toString();
            if (CheckStringUtils.isPhone(mDataBinding.registerPhone.getText().toString())) {
                acount = mDataBinding.registerPhone.getText().toString();
            } else {
                ToastUtils.showShortToast(this, getString(R.string.input_current_phone_no));
                return false;
            }
        } else {
            ToastUtils.showShortToast(this, getString(R.string.please_input_phone_no));
            return false;
        }

        return true;
    }

    private void getCode(String phoneNumber) {
        HashMap<String, String> params = new HashMap<>();
        if (phoneNumber.length() == 11) {
            phoneNumber = "86".concat(phoneNumber);
            acount = phoneNumber;
        }
        params.put("phone_number", phoneNumber);
        params.put("send_type", "change_phone");
        RequestManager.instance().sendCode(params, new MinerCallback<BaseResponseVo>() {
            @Override
            public void onSuccess(Call<BaseResponseVo> call, Response<BaseResponseVo> response) {
                if (response.isSuccessful()) {
                    ToastUtils.showShortToast(BindPhoneActivity.this, getString(R.string.send_success));
                    startCountDownTimer();
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

    private void bindPhone() {

        showLoading(getString(R.string.progress_loading));
        RequestManager.instance().bindPhone(acount, codeMsg, new MinerCallback<BaseResponseVo>() {
            @Override
            public void onSuccess(Call<BaseResponseVo> call, Response<BaseResponseVo> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    ToastUtils.showShortToast(BindPhoneActivity.this, getString(R.string.bind_success));
                    YunApplication.getmUserVo().setPhone_number(acount);
                    finish();
                }
            }

            @Override
            public void onError(Call<BaseResponseVo> call, Response<BaseResponseVo> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private boolean bSendCodeTag;

    private void startCountDownTimer() {
        bSendCodeTag = true;
        if (null == mCountDownTimer)
            initCountDownTimer();
        mCountDownTimer.start();
        mDataBinding.getCode.setEnabled(false);
//        mDataBinding.edtArCode.requestFocus();
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
            }
        };
    }
}