package com.yunhualian.ui.activity.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityUserDescBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.utils.SharedPreUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class UserDescActivity extends BaseActivity<ActivityUserDescBinding> {
    public static String DEFULT_DESC = "desc";
    //输入框初始值
    private int num = 0;
    //输入框最大值
    public int mMaxNum = 100;
    String defaultDesc;

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_desc;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        defaultDesc = getIntent().getStringExtra(DEFULT_DESC);
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.user_profile_desc;
        mToolBarOptions.rightTextString = R.string.user_profile_desc_save;
        TextView rightTx = mDataBinding.mAppBarLayoutAv.mToolbar.findViewById(R.id.txt_right);
        rightTx.setOnClickListener(v -> save());
        rightTx.setTextColor(getResources().getColor(R.color.blue_txt_color));
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

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
        if (!TextUtils.isEmpty(defaultDesc)) {
            mDataBinding.advice.setText(defaultDesc);
            mDataBinding.advice.setSelection(defaultDesc.length());
        } else if (!TextUtils.isEmpty(YunApplication.getmUserVo().getDesc())) {
            mDataBinding.advice.setText(YunApplication.getmUserVo().getDesc());
            mDataBinding.advice.setSelection(YunApplication.getmUserVo().getDesc().length());
        }
        String addr = SharedPreUtils.getString(this, SharedPreUtils.KEY_ADDRESS);
        String seed = SharedPreUtils.getString(this, SharedPreUtils.KEY_SEED);
        String publicKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PUBLICKEY);
        Log.e("tag", seed);
    }

    public void save() {
        if (TextUtils.isEmpty(mDataBinding.advice.getText().toString())) {
            ToastUtils.showLong("描述内容不能为空");
            return;
        }
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> param = new HashMap<>();
        param.put("desc", mDataBinding.advice.getText().toString());
        RequestManager.instance().changeUserInfo(param, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
//                    YunApplication.setmUserVo(response.body().getBody());
                    YunApplication.getmUserVo().setDesc(mDataBinding.advice.getText().toString());
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