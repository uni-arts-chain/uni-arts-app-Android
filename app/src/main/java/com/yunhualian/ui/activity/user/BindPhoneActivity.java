package com.yunhualian.ui.activity.user;


import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;

import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityBindPhoneBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.utils.CheckStringUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class BindPhoneActivity extends BaseActivity<ActivityBindPhoneBinding> {

    private CountDownTimer mCountDownTimer;

    private String acount;

    private String codeMsg;

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
            Log.e("TAG", "size" + cd.length());
            if (mDataBinding.registerCode.getText().length() == 6) {
                codeMsg = mDataBinding.registerCode.getText().toString();
            } else {
                ToastUtils.showShortToast(this, "请输入正确验证码");
                return false;
            }
        } else {
            ToastUtils.showShortToast(this, "请输入验证码");
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
                ToastUtils.showShortToast(this, "请输入正确手机号");
                return false;
            }
        } else {
            ToastUtils.showShortToast(this, "请输入手机号");
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
                    ToastUtils.showShortToast(BindPhoneActivity.this, "已发送");
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

        RequestManager.instance().bindPhone(acount, codeMsg, new MinerCallback<BaseResponseVo>() {
            @Override
            public void onSuccess(Call<BaseResponseVo> call, Response<BaseResponseVo> response) {
                if (response.isSuccessful()) {
                    ToastUtils.showShortToast(BindPhoneActivity.this, "绑定成功");
                    UserVo userVo = YunApplication.getmUserVo();
                    userVo.setPhone_number(acount);
                    YunApplication.setmUserVo(userVo);
                    finish();
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
        mCountDownTimer = new CountDownTimer(60000, 1000) {
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