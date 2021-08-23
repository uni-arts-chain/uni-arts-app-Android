package com.yunhualian.ui.activity;

import android.text.Html;

import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityAuctionRuleLayoutBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.UserAuctionsVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;

import retrofit2.Call;
import retrofit2.Response;

public class AuctionRuleActivity extends BaseActivity<ActivityAuctionRuleLayoutBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_auction_rule_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleString = "竞拍须知";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        getRules();
    }

    private void getRules() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryAuctionRules(new MinerCallback<BaseResponseVo<UserAuctionsVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserAuctionsVo>> call, Response<BaseResponseVo<UserAuctionsVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mDataBinding.tvRules.setText(Html.fromHtml(response.body().getBody().getAuction_notice()));
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<UserAuctionsVo>> call, Response<BaseResponseVo<UserAuctionsVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}
