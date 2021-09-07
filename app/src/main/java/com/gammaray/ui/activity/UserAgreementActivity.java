package com.gammaray.ui.activity;


import android.text.Html;

import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityUserAgreementBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.UserAggrementVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;

import retrofit2.Call;
import retrofit2.Response;

public class UserAgreementActivity extends BaseActivity<ActivityUserAgreementBinding> {

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_agreement;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.user_agreement;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getContent();
    }

    private void getContent() {
        RequestManager.instance().getAgreement(new MinerCallback<BaseResponseVo<UserAggrementVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserAggrementVo>> call, Response<BaseResponseVo<UserAggrementVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null)
                        mDataBinding.content.setText(Html.fromHtml(response.body().getBody().getUser_agreement()));
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<UserAggrementVo>> call, Response<BaseResponseVo<UserAggrementVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}