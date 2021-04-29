package com.yunhualian.ui.activity.user;


import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.Glide;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityCreateOrderBlindBoxBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.BlindBoxVo;
import com.yunhualian.entity.PayResult;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;

import java.math.BigDecimal;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class CreateOrderForBlindBoxActivity extends BaseActivity<ActivityCreateOrderBlindBoxBinding> implements View.OnClickListener {
    public static final String BLIND_BOX_INFO = "info";
    public static final String BUY_AMOUNT = "amount";

    BlindBoxVo sellingArtVo;

    private int MAX_VALUE = 1000;
    private int MAX_DAYS = 180;
    private int MIN_VALUE = 3;

    private String buy_amount;

    private boolean softInputIsShowing = false;
    private static String powers_num = "1";
    private String totalPay;
    private String payType = "wepay";

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_order_blind_box;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.blind_box_pay;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        buy_amount = getIntent().getStringExtra(BUY_AMOUNT);
        sellingArtVo = (BlindBoxVo) getIntent().getExtras().getSerializable(BLIND_BOX_INFO);

        if (sellingArtVo != null)
            initPageData();
        mDataBinding.weiPayLayout.performClick();
    }

    public void initPageData() {

        Glide.with(this).load(sellingArtVo.getImg_path()).into(mDataBinding.hotPicture);

        mDataBinding.name.setText(sellingArtVo.getTitle());

        mDataBinding.artAmount.setText(buy_amount);
        if (new BigDecimal(buy_amount).compareTo(BigDecimal.ONE) > 0) {
            totalPay = new BigDecimal(buy_amount).multiply(new BigDecimal(sellingArtVo.getPrice())).stripTrailingZeros().toPlainString();
        } else totalPay = (sellingArtVo.getPrice());
        mDataBinding.price.setText(getString(R.string.blind_buy_price, "￥".concat(totalPay)));
        mDataBinding.priceTotal.setText(getString(R.string.text_buy_amount, totalPay));
        mDataBinding.weiPayLayout.setOnClickListener(this);
        mDataBinding.aPayLayout.setOnClickListener(this);
        mDataBinding.buyNow.setOnClickListener(this);
        mDataBinding.weichatPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && mDataBinding.aliPay.isChecked()) {
                payType = "wepay";
                mDataBinding.aliPay.setChecked(false);
            }
        });
        mDataBinding.aliPay.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked && mDataBinding.weichatPay.isChecked()) {
                payType = "alipay";
                mDataBinding.weichatPay.setChecked(false);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.aPayLayout:
                mDataBinding.aliPay.performClick();
                break;
            case R.id.weiPayLayout:
                mDataBinding.weichatPay.performClick();
                break;
            case R.id.buy_now:
                orderPayRequest();
                break;
        }
    }

    private void orderPayRequest() {
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> params = new HashMap<>();
        params.put("box_id", String.valueOf(sellingArtVo.getId()));
        params.put("amount", buy_amount);
        params.put("order_from", "android");
        params.put("pay_type", payType);
        RequestManager.instance().blindBoxOrders(params, new MinerCallback<BaseResponseVo<PayResult>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<PayResult>> call, Response<BaseResponseVo<PayResult>> response) {
                dismissLoading();
                if (response.isSuccessful()) {

                    if (!TextUtils.isEmpty(response.body().getBody().getUrl())) {
                        Uri uri = Uri.parse(response.body().getBody().getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<PayResult>> call, Response<BaseResponseVo<PayResult>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}