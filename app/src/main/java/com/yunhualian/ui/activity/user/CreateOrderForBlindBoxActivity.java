package com.yunhualian.ui.activity.user;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityCreateOrderBlindBoxBinding;
import com.yunhualian.entity.AccountVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.BlindBoxVo;
import com.yunhualian.entity.PayResult;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.x5.WebViewActivity;
import com.yunhualian.ui.x5.X5WebViewActivity;
import com.yunhualian.ui.x5.X5WebViewForAliPayActivity;
import com.yunhualian.utils.SharedPreUtils;
import com.yunhualian.widget.BasePopupWindow;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

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
    private String payType = "";
    private String accountRemain;
    private PopupWindow mPasswordPopwindow;
    private String passwd;

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
        queryAccountInfo();
        initPasswordPopwindow();
        if (sellingArtVo != null) {
            initPageData();
        }
    }

    public void initPageData() {

        Glide.with(this).load(sellingArtVo.getApp_img_path()).into(mDataBinding.hotPicture);

        mDataBinding.name.setText(sellingArtVo.getTitle());

        mDataBinding.artAmount.setText(getString(R.string.blind_buy_amount, buy_amount));
        if (new BigDecimal(buy_amount).compareTo(BigDecimal.ONE) > 0) {
            totalPay = new BigDecimal(buy_amount).multiply(new BigDecimal(sellingArtVo.getPrice())).stripTrailingZeros().toPlainString();
        } else totalPay = (sellingArtVo.getPrice());
        mDataBinding.priceValue.setText(YunApplication.PAY_CURRENCY.concat(totalPay));
        mDataBinding.priceTotal.setText(getString(R.string.text_buy_amount, totalPay));
        mDataBinding.weiPayLayout.setOnClickListener(this);
        mDataBinding.aPayLayout.setOnClickListener(this);
        mDataBinding.buyNow.setOnClickListener(this);
        mDataBinding.remain.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                payType = "account";
                mDataBinding.weichatPay.setChecked(false);
                mDataBinding.aliPay.setChecked(false);
            } else {
                if (!mDataBinding.weichatPay.isChecked() && !mDataBinding.aliPay.isChecked()) {
                    mDataBinding.remain.setChecked(true);
                }
            }
        });
        mDataBinding.weichatPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                payType = "wepay";
                mDataBinding.aliPay.setChecked(false);
                mDataBinding.remain.setChecked(false);
            } else {
                if (!mDataBinding.aliPay.isChecked() && !mDataBinding.remain.isChecked()) {
                    mDataBinding.weichatPay.setChecked(true);
                }
            }
        });
        mDataBinding.aliPay.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                payType = "alipay";
                mDataBinding.weichatPay.setChecked(false);
                mDataBinding.remain.setChecked(false);
            } else {
                if (!mDataBinding.weichatPay.isChecked() && !mDataBinding.remain.isChecked()) {
                    mDataBinding.aliPay.setChecked(true);
                }
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
            case R.id.remainLayout:
                if (Double.parseDouble(accountRemain) < Double.parseDouble(totalPay)) {
                    ToastUtils.showShort("账户余额不足");
                    return;
                }
                mDataBinding.remain.performClick();
                break;
            case R.id.buy_now:
                if (payType.equals("account")) {
                    mPasswordPopwindow.showAtLocation(mDataBinding.parentLayout, Gravity.CENTER, 0, 0);
                } else {
                    orderPayRequest();
                }
                break;
        }
    }

    private void initPasswordPopwindow() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_passwd_layout, null);
        ImageView closeBtn = contentView.findViewById(R.id.img_close);
        TextView confirmBtn = contentView.findViewById(R.id.confirm);
        TextView passwdHintTv = contentView.findViewById(R.id.tv_passwd_len_hint);
        EditText passwdEd = contentView.findViewById(R.id.ed_password);
        mPasswordPopwindow = new BasePopupWindow(this);
        mPasswordPopwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPasswordPopwindow.setContentView(contentView);
        mPasswordPopwindow.setOutsideTouchable(false);
        mPasswordPopwindow.setTouchable(true);
        mPasswordPopwindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        passwdEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    passwdHintTv.setText("0/6");
                } else {
                    passwdHintTv.setText(editable.toString().length() + "/6");
                    passwd = editable.toString();
                }
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwd.equals(SharedPreUtils.getString(CreateOrderForBlindBoxActivity.this, SharedPreUtils.KEY_PIN))) {
                    orderPayRequest();
                } else {
                    ToastUtils.showShort("密码错误");
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwdEd.setText("");
                mPasswordPopwindow.dismiss();
            }
        });

    }

    private void queryAccountInfo() {
        RequestManager.instance().queryAccount(new MinerCallback<BaseResponseVo<List<AccountVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AccountVo>>> call, Response<BaseResponseVo<List<AccountVo>>> response) {
                if (response.isSuccessful()) {
                    List<AccountVo> accounts = response.body().getBody();
                    if (accounts != null && accounts.size() > 0) {
                        for (int i = 0; i < accounts.size(); i++) {
                            if (accounts.get(i).getCurrency_code().equals("rmb")) {
                                accountRemain = accounts.get(i).getBalance();
                                mDataBinding.tvRemainValue.setText(getString(R.string.account_remain_v, accountRemain));
                                if (Double.parseDouble(accountRemain) >= Double.parseDouble(totalPay)) {
                                    mDataBinding.remain.performClick();
                                } else {
                                    mDataBinding.weichatPay.performClick();
                                }
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<AccountVo>>> call, Response<BaseResponseVo<List<AccountVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
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
                    if (payType.equals("account")) {
                        finish();
                    } else {
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
                        } else {
                            ToastUtils.showShort("出错了");
                        }
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