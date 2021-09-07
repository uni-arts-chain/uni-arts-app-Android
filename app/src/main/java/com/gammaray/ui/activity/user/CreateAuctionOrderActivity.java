package com.gammaray.ui.activity.user;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityCreateAuctionOrderBinding;
import com.gammaray.entity.AccountVo;
import com.gammaray.entity.AuctionArtVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.PayResyltVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.x5.WebViewActivity;
import com.gammaray.ui.x5.X5WebViewActivity;
import com.gammaray.ui.x5.X5WebViewForAliPayActivity;
import com.gammaray.utils.DisplayUtils;
import com.gammaray.utils.SharedPreUtils;
import com.gammaray.widget.BasePopupWindow;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CreateAuctionOrderActivity extends BaseActivity<ActivityCreateAuctionOrderBinding> implements View.OnClickListener {
    public static final String ARTINFO = "art";
    AuctionArtVo sellingArtVo;
    private String payType = "";
    private final int ROUND = 2;
    private String accountRemain;
    private String totalPrice;
    private String auction_id;
    private CountDownTimer mCountDownTimer;
    private PopupWindow mPasswordPopwindow;
    private String passwd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_create_auction_order;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.form_order;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        if (getIntent() != null) {
            auction_id = getIntent().getStringExtra("id");
        }
        initPasswordPopwindow();
        requestArtInfo();
    }

    public void requestArtInfo() {
        showLoading(getString(R.string.progress_loading));
        RequestManager.instance().auctionArtInfo(auction_id, new MinerCallback<BaseResponseVo<AuctionArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<AuctionArtVo>> call, Response<BaseResponseVo<AuctionArtVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        sellingArtVo = response.body().getBody();
                        initPageData();
                        queryAccountInfo();
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<AuctionArtVo>> call, Response<BaseResponseVo<AuctionArtVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private String getTv(long l) {
        if (l >= 10) {
            return l + "";
        } else {
            return "0" + l;//小于10,,前面补位一个"0"
        }
    }

    public void initPageData() {

        if (sellingArtVo == null) {
            return;
        }
        mCountDownTimer = new CountDownTimer((sellingArtVo.getEnd_time() + sellingArtVo.getPay_timeout() - sellingArtVo.getServer_timestamp()) * 1000, 1000) {
            @Override
            public void onTick(long seconds) {
                long hours = seconds / (3600 * 1000);            //转换小时
                seconds = seconds % (3600 * 1000);
                long minutes = seconds / (60 * 1000);            //转换分钟
                seconds = seconds % (60 * 1000);
                seconds = seconds / 1000;                       //转换秒钟

                String time = getString(R.string.time_holder, getTv(hours), getTv(minutes), getTv(seconds));
                mDataBinding.tvCountTimeHint.setText(getString(R.string.count_time_hint, time));
            }

            @Override
            public void onFinish() {
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                }
                mDataBinding.tvBuy.setText("超时未支付");
                mDataBinding.buyNow.setEnabled(false);
                mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(CreateAuctionOrderActivity.this, R.drawable.shape_btn_gray));
            }
        }.start();
        showImage();

        mDataBinding.name.setText(sellingArtVo.getArt().getName());
        mDataBinding.tvAuctionAmountValue.setText(String.valueOf(sellingArtVo.getAmount()));
        if (sellingArtVo.getRoyalty() != null && Double.parseDouble(sellingArtVo.getRoyalty()) != 0) {
            mDataBinding.rlRotateLayout.setVisibility(View.VISIBLE);
            String royaltyRate = sellingArtVo.getArt().getRoyalty() == null ? "0" : sellingArtVo.getArt().getRoyalty().toPlainString();
            mDataBinding.rotayRate.setText(getString(R.string.royalty_rate_value,
                    TextUtils.isEmpty(royaltyRate) ?
                            "0%" :
                            new BigDecimal(royaltyRate).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString().concat("%")));
            mDataBinding.rotayPrice.setText(getString(R.string.text_buy_amount, sellingArtVo.getRoyalty()));
        } else {
            mDataBinding.rlRotateLayout.setVisibility(View.GONE);
        }
        mDataBinding.tvDepositValue.setText("¥" + sellingArtVo.getDeposit_amount());
        mDataBinding.artName.setText(sellingArtVo.getArt().getAuthor().getDisplay_name());

        mDataBinding.addr.setText(getString(R.string.nft_address, sellingArtVo.getArt().getItem_hash()));
        mDataBinding.weiPayLayout.setOnClickListener(this);
        mDataBinding.aPayLayout.setOnClickListener(this);
        mDataBinding.remainLayout.setOnClickListener(this);
        mDataBinding.price.setText(getString(R.string.text_buy_amount, new BigDecimal(sellingArtVo.getWin_price()).stripTrailingZeros().toPlainString()));
        if (sellingArtVo.getRoyalty() == null || Double.parseDouble(sellingArtVo.getRoyalty()) == 0) {
            totalPrice = (new BigDecimal(sellingArtVo.getWin_price()).
                    subtract(new BigDecimal(sellingArtVo.getDeposit_amount()))).
                    setScale(2, BigDecimal.ROUND_UP).
                    stripTrailingZeros().
                    toPlainString();
        } else {
            totalPrice = (new BigDecimal(sellingArtVo.getWin_price()).
                    subtract(new BigDecimal(sellingArtVo.getDeposit_amount()))
                    .add(new BigDecimal(sellingArtVo.getRoyalty()))).
                    setScale(2, BigDecimal.ROUND_UP).
                    stripTrailingZeros().
                    toPlainString();
        }
        mDataBinding.priceTotal.setText(getString(R.string.text_buy_amount,
                totalPrice));
        mDataBinding.buyNow.setOnClickListener(this);
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
    }

    private void showImage() {

        Glide.with(this).asBitmap().load(sellingArtVo.getArt().getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int height = DisplayUtils.px2dp(CreateAuctionOrderActivity.this, bitmap.getHeight());
                int width = DisplayUtils.px2dp(CreateAuctionOrderActivity.this, bitmap.getWidth());
                int imageViewWidth = 102;
                int imageViewHeigt = 76;
                BigDecimal width_ = new BigDecimal(String.valueOf(imageViewHeigt))
                        .divide(new BigDecimal(String.valueOf(height)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(width)));
                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(height)));
                if (width > height) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(CreateAuctionOrderActivity.this, imageViewWidth),
                            DisplayUtils.dp2px(CreateAuctionOrderActivity.this, height_.floatValue()));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mDataBinding.hotPicture.setLayoutParams(layoutParams);
                } else {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(CreateAuctionOrderActivity.this, width_.floatValue()),
                            DisplayUtils.dp2px(CreateAuctionOrderActivity.this, imageViewHeigt));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mDataBinding.hotPicture.setLayoutParams(layoutParams);
                }
            }
        });
        Glide.with(this).load(sellingArtVo.getArt().getImg_main_file1().getUrl()).into(mDataBinding.hotPicture);
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
                if (TextUtils.isEmpty(charSequence.toString())) {
                    passwdHintTv.setText("0/6");
                } else {
                    passwdHintTv.setText(charSequence.toString().length() + "/6");
                    passwd = charSequence.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwd.equals(SharedPreUtils.getString(CreateAuctionOrderActivity.this, SharedPreUtils.KEY_PIN))) {
                    performOrders();
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
                if (Double.parseDouble(accountRemain) < Double.parseDouble(totalPrice)) {
                    ToastUtils.showShort("账户余额不足");
                    return;
                }
                mDataBinding.remain.performClick();
                break;
            case R.id.buy_now:
                if (payType.equals("account")) {
                    mPasswordPopwindow.showAtLocation(mDataBinding.parentLayout, Gravity.CENTER, 0, 0);
                } else {
                    performOrders();
                }
                break;
        }
    }

    private void queryAccountInfo() {
        RequestManager.instance().queryAccount(new MinerCallback<BaseResponseVo<List<AccountVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AccountVo>>> call, Response<BaseResponseVo<List<AccountVo>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<AccountVo> accounts = response.body().getBody();
                        if (accounts != null && accounts.size() > 0) {
                            for (int i = 0; i < accounts.size(); i++) {
                                if (accounts.get(i).getCurrency_code().equals("rmb")) {
                                    accountRemain = accounts.get(i).getBalance();
                                    mDataBinding.tvRemainValue.setText(getString(R.string.account_remain_v, accountRemain));
                                    if (Double.parseDouble(accountRemain) >= Double.parseDouble(totalPrice)) {
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
            }

            @Override
            public void onError(Call<BaseResponseVo<List<AccountVo>>> call, Response<BaseResponseVo<List<AccountVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    private void performOrders() {
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> params = new HashMap<>();
        params.put("auction_id", String.valueOf(sellingArtVo.getId()));
        params.put("order_from", "android");
        params.put("pay_type", payType);
        RequestManager.instance().artOrders(params, new MinerCallback<BaseResponseVo<PayResyltVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<PayResyltVo>> call, Response<BaseResponseVo<PayResyltVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (payType.equals("account")) {
                        finish();
                    } else {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}