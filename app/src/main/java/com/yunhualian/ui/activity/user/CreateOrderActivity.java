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
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.ui.activity.ArtDetailActivity;

import org.bouncycastle.asn1.x9.ValidationParams;

import java.math.BigDecimal;

public class CreateOrderActivity extends BaseActivity<ActivityCreateOrderBinding> implements View.OnClickListener {


    SellingArtVo sellingArtVo;

    private int MAX_VALUE = 1000;
    private int MAX_DAYS = 180;
    private int MIN_VALUE = 3;

    private int buy_amount = 3;

    private boolean softInputIsShowing = false;
    private static String powers_num = "1";

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

        sellingArtVo = (SellingArtVo) getIntent().getExtras().getSerializable(ArtDetailActivity.ART_KEY);

        if (sellingArtVo != null)
            initPageData();

        initListener();
    }

    public void initPageData() {

        Glide.with(this).load(sellingArtVo.getImg_main_file1().getUrl()).into(mDataBinding.hotPicture);

        mDataBinding.name.setText(sellingArtVo.getName());

        mDataBinding.artName.setText(sellingArtVo.getAuthor().getDisplay_name());

        mDataBinding.addr.setText(getString(R.string.nft_address, sellingArtVo.getItem_hash()));

        mDataBinding.weiPayLayout.setOnClickListener(this);
        mDataBinding.aPayLayout.setOnClickListener(this);

        mDataBinding.weichatPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && mDataBinding.aliPay.isChecked()) {
                mDataBinding.aliPay.setChecked(false);
            }
        });
        mDataBinding.aliPay.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked && mDataBinding.weichatPay.isChecked()) {
                mDataBinding.weichatPay.setChecked(false);
            }
        });
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
                    buy_amount = 3;
                } else
                    buy_amount = Integer.parseInt(mDataBinding.inputAmount.getText().toString());
                powers_num = String.valueOf(buy_amount);
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
        }
    }
}