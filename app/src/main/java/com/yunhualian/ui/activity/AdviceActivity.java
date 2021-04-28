package com.yunhualian.ui.activity;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityAdviceBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;

import org.web3j.crypto.Hash;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class AdviceActivity extends BaseActivity<ActivityAdviceBinding> {

    //输入框初始值
    private int num = 0;
    //输入框最大值
    public int mMaxNum = 100;

    @Override
    public int getLayoutId() {
        return R.layout.activity_advice;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.advice_title;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        mDataBinding.confirm.setOnClickListener(v -> onClick());
        mDataBinding.advice.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num + s.length();
                //TextView显示剩余字数
                mDataBinding.textNum.setText("" + number + "/" + mMaxNum);
                selectionStart = mDataBinding.advice.getSelectionStart();
                selectionEnd = mDataBinding.advice.getSelectionEnd();
                //判断大于最大值
                if (wordNum.length() > mMaxNum) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mDataBinding.advice.setText(s);
                    mDataBinding.advice.setSelection(tempSelection);//设置光标在最后
                    //吐司最多输入300字
                    ToastUtils.showLong("最多输入100字");
                }
            }
        });
    }

    private void onClick() {
        if (TextUtils.isEmpty(mDataBinding.advice.toString())) {
            ToastUtils.showLong("意见不能为空");
            return;
        }
        feedBack();
    }

    private void feedBack() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("advise", mDataBinding.advice.getText().toString());
        hashMap.put("contact", mDataBinding.phone.getText().toString());
        RequestManager.instance().feedBack(hashMap, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                if (response.isSuccessful()) {
                    ToastUtils.showLong("提交成功");
                    finish();
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }
}