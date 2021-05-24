package com.yunhualian.ui.activity.user;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityCreateOrderBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.OrderAmountVo;
import com.yunhualian.entity.PayResyltVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.order.OrderDetailActivity;
import com.yunhualian.ui.x5.WebViewActivity;
import com.yunhualian.ui.x5.X5WebViewActivity;
import com.yunhualian.ui.x5.X5WebViewForAliPayActivity;
import com.yunhualian.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Response;

public class CreateOrderActivity extends BaseActivity<ActivityCreateOrderBinding> implements View.OnClickListener {
    public static final String ARTINFO = "art";
    public static final String ORDERINFO = "order";

    SellingArtVo sellingArtVo;
    OrderAmountVo orderAmountVo;
    private int MAX_VALUE = 1000;
    private int MAX_DAYS = 180;
    private int MIN_VALUE = 1;

    private int buy_amount = 1;

    private boolean softInputIsShowing = false;
    private String powers_num = "1";
    private String payType = "wepay";
    private boolean isNoRotaly = false;
    private int ROUND = 2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_order;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.order_info_detail;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        sellingArtVo = (SellingArtVo) getIntent().getExtras().getSerializable(ARTINFO);
        orderAmountVo = (OrderAmountVo) getIntent().getExtras().getSerializable(ORDERINFO);
        isNoRotaly = !orderAmountVo.getNeed_royalty();
        if (sellingArtVo != null && orderAmountVo != null)
            initPageData();
        initListener();
    }

    public void initPageData() {
        MAX_VALUE = orderAmountVo.getAmount();
        showImage();
        mDataBinding.lastAmount.setText(getString(R.string.last_amount_, String.valueOf(orderAmountVo.getAmount())));
        mDataBinding.name.setText(sellingArtVo.getName());
        String rotalyRate = isNoRotaly ? "0" : sellingArtVo.getRoyalty() == null ? "0" : sellingArtVo.getRoyalty().toPlainString();
        mDataBinding.rotayRate.setText(getString(R.string.royalty_rate_value,
                TextUtils.isEmpty(rotalyRate) ?
                        "0%" :
                        new BigDecimal(rotalyRate).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString().concat("%")));

        mDataBinding.rotayPrice.setText(getString(R.string.text_buy_amount,
                new BigDecimal(orderAmountVo.getPrice())
                        .multiply(new BigDecimal(rotalyRate)).setScale(ROUND, RoundingMode.UP).stripTrailingZeros().toPlainString()));
        mDataBinding.artName.setText(sellingArtVo.getAuthor().getDisplay_name());

        mDataBinding.addr.setText(getString(R.string.nft_address, sellingArtVo.getItem_hash()));
        if (sellingArtVo.getCollection_mode() != 3) {
            mDataBinding.cut.setVisibility(View.GONE);
            mDataBinding.add.setVisibility(View.GONE);
            mDataBinding.inputAmount.setFocusable(false);
            mDataBinding.inputAmount.setEnabled(false);
            mDataBinding.lastAmount.setVisibility(View.INVISIBLE);
        }
        mDataBinding.weiPayLayout.setOnClickListener(this);
        mDataBinding.aPayLayout.setOnClickListener(this);
        mDataBinding.price.setText(getString(R.string.text_buy_amount, new BigDecimal(orderAmountVo.getPrice()).stripTrailingZeros().toPlainString()));
        String totalPrice = isNoRotaly ? orderAmountVo.getPrice() :
                new BigDecimal(orderAmountVo.getPrice()).add(new BigDecimal(orderAmountVo.getPrice())
                        .multiply(new BigDecimal(rotalyRate)).setScale(ROUND, BigDecimal.ROUND_UP)).stripTrailingZeros().toPlainString();
        mDataBinding.priceTotal.setText(getString(R.string.text_buy_amount,
                totalPrice));
        mDataBinding.weichatPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && mDataBinding.aliPay.isChecked()) {
                payType = "wepay";
                mDataBinding.aliPay.setChecked(false);
            }
        });
        mDataBinding.buyNow.setOnClickListener(this);
        mDataBinding.aliPay.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked && mDataBinding.weichatPay.isChecked()) {
                payType = "alipay";
                mDataBinding.weichatPay.setChecked(false);
            }
        });
        mDataBinding.weiPayLayout.performClick();
    }

    private void showImage() {

        Glide.with(this).asBitmap().load(sellingArtVo.getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int height = DisplayUtils.px2dp(CreateOrderActivity.this, bitmap.getHeight());
                int width = DisplayUtils.px2dp(CreateOrderActivity.this, bitmap.getWidth());
                int imageViewWidth = 102;
                int imageViewHeigt = 76;
                BigDecimal width_ = new BigDecimal(String.valueOf(imageViewHeigt))
                        .divide(new BigDecimal(String.valueOf(height)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(width)));
                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(height)));
                if (width > height) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(CreateOrderActivity.this, imageViewWidth),
                            DisplayUtils.dp2px(CreateOrderActivity.this, height_.floatValue()));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mDataBinding.hotPicture.setLayoutParams(layoutParams);
                } else {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(CreateOrderActivity.this, width_.floatValue()),
                            DisplayUtils.dp2px(CreateOrderActivity.this, imageViewHeigt));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mDataBinding.hotPicture.setLayoutParams(layoutParams);
                }
            }
        });
        Glide.with(this).load(sellingArtVo.getImg_main_file1().getUrl()).into(mDataBinding.hotPicture);
    }

    private void initListener() {
        mDataBinding.inputAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputString = s.toString();
                if (TextUtils.isEmpty(inputString)) {
                    mDataBinding.inputAmount.setText(String.valueOf(0));
                    mDataBinding.inputAmount.setSelection(String.valueOf(0).length());
                    return;
                }
                BigDecimal inputAmount = new BigDecimal(inputString);
                BigDecimal minValue = new BigDecimal(MIN_VALUE);
                BigDecimal maxValue = new BigDecimal(MAX_VALUE);
                if (inputAmount.compareTo(minValue) < 0) {
                    mDataBinding.inputAmount.setText(minValue.toString());
                    mDataBinding.inputAmount.setSelection(minValue.toString().length());
                } else if (inputAmount.compareTo(maxValue) > 0) {
                    mDataBinding.inputAmount.setText(maxValue.toString());
                    mDataBinding.inputAmount.setSelection(maxValue.toPlainString().length());
                } else if (inputString.length() >= 1 && inputString.substring(0, 1).equals("0") && !inputString.equals("0")) {
                    mDataBinding.inputAmount.setText(inputAmount.toPlainString());
                    mDataBinding.inputAmount.setSelection(inputAmount.toPlainString().length());
                }
                if (TextUtils.isEmpty(mDataBinding.inputAmount.getText().toString())) {
                    buy_amount = MIN_VALUE;
                } else
                    buy_amount = Integer.parseInt(mDataBinding.inputAmount.getText().toString());
                powers_num = String.valueOf(buy_amount);
                String price = new BigDecimal(orderAmountVo.getPrice()).multiply(new BigDecimal(powers_num)).stripTrailingZeros().toPlainString();
                String rotayPrice = new BigDecimal(orderAmountVo.getPrice())
                        .multiply(new BigDecimal(powers_num))
                        .multiply(sellingArtVo.getRoyalty() == null ? BigDecimal.ZERO : sellingArtVo.getRoyalty()).setScale(ROUND, RoundingMode.UP).stripTrailingZeros().toPlainString();
                rotayPrice = isNoRotaly ? "0" : rotayPrice;
                String totalPrice = new BigDecimal(price).add(new BigDecimal(rotayPrice)).stripTrailingZeros().toPlainString();
                mDataBinding.price.setText(getString(R.string.text_buy_amount, price));
                mDataBinding.priceTotal.setText(getString(R.string.text_buy_amount, totalPrice));
                if (!rotayPrice.equals("0"))
                    mDataBinding.rotayPrice.setText(getString(R.string.text_buy_amount,
                            rotayPrice));
            }
        });

        mDataBinding.add.setOnClickListener(v -> {
            buy_amount++;
            mDataBinding.inputAmount.setText("" + buy_amount);
            mDataBinding.inputAmount.setSelection(String.valueOf(buy_amount).length());
        });
        mDataBinding.cut.setOnClickListener(v -> {
            buy_amount--;
            if (buy_amount < 0) {
                buy_amount = 0;
                return;
            }
            mDataBinding.inputAmount.setText("" + buy_amount);
            mDataBinding.inputAmount.setSelection(String.valueOf(buy_amount).length());
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
                performOrders();
                break;
        }
    }

    private void performOrders() {
        showLoading(getString(R.string.progress_loading));
        String amount = TextUtils.isEmpty(mDataBinding.inputAmount.getText().toString()) ? powers_num : mDataBinding.inputAmount.getText().toString();
        HashMap<String, String> params = new HashMap<>();
        params.put("art_order_sn", orderAmountVo.getSn());
        params.put("amount", amount);
        params.put("order_from", "android");
        params.put("pay_type", payType);
        RequestManager.instance().artOrders(params, new MinerCallback<BaseResponseVo<PayResyltVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<PayResyltVo>> call, Response<BaseResponseVo<PayResyltVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null) {
                        String url = response.body().getBody().getUrl();
                        String title = "支付";
                        Bundle bundle = new Bundle();
                        bundle.putString(WebViewActivity.TITLE, title);
                        bundle.putString(WebViewActivity.URL, url);
                        bundle.putString(WebViewActivity.TYPE, payType);
                        if (!TextUtils.isEmpty(url)) {
                            if (payType.equals(X5WebViewActivity.WECHAT)) {
                                startActivity(X5WebViewActivity.class, bundle);
                            } else startActivity(X5WebViewForAliPayActivity.class, bundle);
                            finish();
                        } else
                            ToastUtils.showShort("出错了");
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<PayResyltVo>> call, Response<BaseResponseVo<PayResyltVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });

    }
}