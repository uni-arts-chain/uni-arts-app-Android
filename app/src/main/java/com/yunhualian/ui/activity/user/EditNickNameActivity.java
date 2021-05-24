package com.yunhualian.ui.activity.user;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.blankj.utilcode.util.ToastUtils;
import com.lljjcoder.Constant;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityEditNickNameBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;


import java.util.HashMap;

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
        TextView rightTx = mDataBinding.mAppBarLayoutAv.mToolbar.findViewById(R.id.txt_right);
        rightTx.setOnClickListener(v -> save());
        rightTx.setTextColor(getResources().getColor(R.color.blue_txt_color));
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        if (!TextUtils.isEmpty(YunApplication.getmUserVo().getDisplay_name())) {
            mDataBinding.nickName.setText(YunApplication.getmUserVo().getDisplay_name());
            mDataBinding.nickName.setSelection(YunApplication.getmUserVo().getDisplay_name().length());
        }
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
        showLoading(getString(R.string.progress_loading));
        if (TextUtils.isEmpty(mDataBinding.nickName.getText().toString())) {
            ToastUtils.showLong("昵称不能为空");
            return;
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("display_name", mDataBinding.nickName.getText().toString());
        RequestManager.instance().changeUserInfo(param, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
//                    YunApplication.setmUserVo(response.body().getBody());
                    YunApplication.getmUserVo().setDisplay_name(mDataBinding.nickName.getText().toString());
                    ToastUtils.showLong("修改成功");
                    finish();
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

}