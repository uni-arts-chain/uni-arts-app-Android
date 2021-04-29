package com.yunhualian.ui.activity.user;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.bumptech.glide.Glide;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityCreateOrderBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.OrderAmountVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.ArtDetailActivity;

import org.bouncycastle.asn1.x9.ValidationParams;

import java.math.BigDecimal;
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
    private static String powers_num = "1";
    private String payType = "wepay";

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
        if (sellingArtVo != null && orderAmountVo != null)
            initPageData();

        initListener();
    }

    public void initPageData() {
        MAX_VALUE = orderAmountVo.getAmount();
        Glide.with(this).load(sellingArtVo.getImg_main_file1().getUrl()).into(mDataBinding.hotPicture);

        mDataBinding.name.setText(sellingArtVo.getName());

        mDataBinding.artName.setText(sellingArtVo.getAuthor().getDisplay_name());

        mDataBinding.addr.setText(getString(R.string.nft_address, sellingArtVo.getItem_hash()));

        mDataBinding.weiPayLayout.setOnClickListener(this);
        mDataBinding.aPayLayout.setOnClickListener(this);
        mDataBinding.price.setText(getString(R.string.text_buy_amount, orderAmountVo.getPrice()));
        mDataBinding.priceTotal.setText(getString(R.string.text_buy_amount, orderAmountVo.getPrice()));
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
                mDataBinding.price.setText(price);
                mDataBinding.priceTotal.setText(price);
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
        HashMap<String, String> params = new HashMap<>();
        params.put("art_order_sn", orderAmountVo.getSn());
        params.put("amount", powers_num);
        params.put("order_from", "android");
        params.put("pay_type", payType);
        RequestManager.instance().artOrders(params, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {

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