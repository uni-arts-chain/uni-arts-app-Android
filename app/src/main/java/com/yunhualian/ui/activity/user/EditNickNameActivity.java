package com.yunhualian.ui.activity.user;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.ToastUtils;
import com.lljjcoder.Constant;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.databinding.ActivityEditNickNameBinding;
import com.yunhualian.entity.ArtPriceVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.HttpRequestUtils;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.MvpNetCallback;
import com.yunhualian.net.RequestManager;
import com.zhy.http.okhttp.callback.Callback;

import org.web3j.crypto.Hash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

/*
 * 修改昵称
 *
 * */
public class EditNickNameActivity extends BaseActivity<ActivityEditNickNameBinding> {
    //输入框初始值
    private int num = 0;
    //输入框最大值
    public int mMaxNum = 8;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_nick_name;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.edit_nick_name_title;
        mToolBarOptions.rightTextString = R.string.user_profile_desc_save;
        mDataBinding.mAppBarLayoutAv.mToolbar.findViewById(R.id.txt_right).setOnClickListener(v -> save());
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        mDataBinding.nickName.addTextChangedListener(new TextWatcher() {
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
                selectionStart = mDataBinding.nickName.getSelectionStart();
                selectionEnd = mDataBinding.nickName.getSelectionEnd();
                //判断大于最大值
                if (wordNum.length() > mMaxNum) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mDataBinding.nickName.setText(s);
                    mDataBinding.nickName.setSelection(tempSelection);//设置光标在最后
                    //吐司最多输入300字
                    ToastUtils.showLong("最多输入8字");
                }
            }
        });
    }

    public void save() {
        if (TextUtils.isEmpty(mDataBinding.nickName.getText().toString())) {
            ToastUtils.showLong("昵称不能为空");
            return;
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("display_name", mDataBinding.nickName.getText().toString());
        RequestManager.instance().changeUserInfo(param, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                if (response.isSuccessful()) {
                    YunApplication.setmUserVo(response.body().getBody());
                    ToastUtils.showLong("修改成功");
                    finish();
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

}