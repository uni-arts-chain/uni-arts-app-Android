package com.yunhualian.ui.activity;


import android.text.TextUtils;
import android.view.Gravity;

import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityExchangeNFTBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.widget.ExchangeNFTSuccessPopUpWindow;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class ExchangeNFTActivity extends BaseActivity<ActivityExchangeNFTBinding> {

    ExchangeNFTSuccessPopUpWindow exchangeNFTSuccessPopUpWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_exchange_n_f_t;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.title_exchange_nft;
        mToolBarOptions.rightTextString = R.string.title_exchange_action;
        mDataBinding.mAppBarLayoutAv.mToolbar.findViewById(R.id.txt_right).setOnClickListener(v -> save());
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        exchangeNFTSuccessPopUpWindow = new ExchangeNFTSuccessPopUpWindow(this, () -> {
            exchangeNFTSuccessPopUpWindow.dismiss();
            finish();
        });
    }

    private void save() {
        if (TextUtils.isEmpty(mDataBinding.exchangeCode.getText().toString())) {
            ToastUtils.showShortToast(this, getString(R.string.text_exchange_code_null_tips));
            return;
        }
        HashMap<String, String> param = new HashMap<>();
        param.put("sn", mDataBinding.exchangeCode.getText().toString());
        RequestManager.instance().exchangeNFT(param, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                if (response.isSuccessful()) {
                    exchangeNFTSuccessPopUpWindow.showAtLocation(mDataBinding.parent, Gravity.CENTER, 0, 0);
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