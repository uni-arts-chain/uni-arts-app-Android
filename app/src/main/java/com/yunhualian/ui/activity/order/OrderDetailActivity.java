package com.yunhualian.ui.activity.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityOrderDetailBinding;
import com.yunhualian.entity.BoughtArtVo;
import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OrderDetailActivity extends BaseActivity<ActivityOrderDetailBinding> {
    public static final String BOUGHT_KEY = "bought_vo";
    public static final String ORDER_TYPE = "order_type";
    BoughtArtVo boughtArtVo;
    private int orderType = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.order_info_detail;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        boughtArtVo = (BoughtArtVo) getIntent().getSerializableExtra(BOUGHT_KEY);
        orderType = getIntent().getIntExtra(ORDER_TYPE, 0);
        if (boughtArtVo != null)
            initPageData();
    }

    private void initPageData() {
        showImage();
        mDataBinding.memoTv.setText("商品数量");
        mDataBinding.orderCreateTime.setText(getString(R.string.order_create_time, DateUtil.dateToStringWith(boughtArtVo.getFinished_at() * 1000)));
        mDataBinding.name.setText(boughtArtVo.getArt().getName());
        mDataBinding.orderInfo.setText(
                boughtArtVo.getSn());

        double royaltyValue;
        double royalty = 0;
        if(orderType == 0){
            mDataBinding.rotailRate.setVisibility(View.GONE);
        }else{
            if (boughtArtVo.getArt().getRoyalty() == null) {
                mDataBinding.rotailRate.setVisibility(View.GONE);
            } else {
                if (Double.parseDouble(boughtArtVo.getArt().getRoyalty()) == 0) {
                    mDataBinding.rotailRate.setVisibility(View.GONE);
                } else {
                    royalty = Double.parseDouble(boughtArtVo.getArt().getRoyalty());
                    mDataBinding.rotailRate.setVisibility(View.VISIBLE);
                    if (boughtArtVo.getTrade_refer().equals("Auction")) {
                        double winPrice = Double.parseDouble(boughtArtVo.getAuction().getWin_price());
                        royaltyValue = winPrice * royalty;
                    } else {
                        double totalPrice = Double.parseDouble(boughtArtVo.getTotal_price());
                        royaltyValue = totalPrice * royalty;
                    }
                    BigDecimal bigDecimal = new BigDecimal(royaltyValue);
                    double royaltyDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    mDataBinding.rotailRate.setText(getString(R.string.text_contain_royalty, String.valueOf(royaltyDecimal)));
                }
            }
        }


        if (orderType == BigDecimal.ZERO.intValue()) {
            mDataBinding.artPrize.setText(YunApplication.PAY_CURRENCY.concat(new BigDecimal(boughtArtVo.getPrice()).multiply(new BigDecimal(boughtArtVo.getAmount())).stripTrailingZeros().toPlainString()));
        } else {
            mDataBinding.artPrize.setText(YunApplication.PAY_CURRENCY.concat(boughtArtVo.getTotal_price()));
        }
        if (TextUtils.isEmpty(boughtArtVo.getArt().getAuthor().getDisplay_name()))
            mDataBinding.artName.setText(getString(R.string.no_display_name));
        else mDataBinding.artName.setText(boughtArtVo.getArt().getAuthor().getDisplay_name());
        String amount = TextUtils.isEmpty(boughtArtVo.getAmount()) ? "0" : new BigDecimal(boughtArtVo.getAmount()).stripTrailingZeros().toPlainString();
        mDataBinding.memo.setText(amount.concat("份"));
        mDataBinding.addr.setText(getString(R.string.nft_address, boughtArtVo.getArt().getItem_hash()));

        mDataBinding.copyAction.setOnClickListener(v -> {
            if (TextUtils.isEmpty(boughtArtVo.getSn())) {
                ToastUtils.showLongToast(OrderDetailActivity.this, getString(R.string.no_order_sn));
                return;
            }
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("", boughtArtVo.getSn());
            cm.setPrimaryClip(mClipData);
            ToastUtils.showLongToast(this, getString(R.string.copy_order_sn));
        });
    }

    private void showImage() {
        RequestOptions options = new RequestOptions().bitmapTransform(new RoundedCorners(10));
        Glide.with(this).asBitmap().load(boughtArtVo.getArt().getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int height = DisplayUtils.px2dp(OrderDetailActivity.this, bitmap.getHeight());
                int width = DisplayUtils.px2dp(OrderDetailActivity.this, bitmap.getWidth());
                int imageViewWidth = 102;
                int imageViewHeigt = 76;
                BigDecimal width_ = new BigDecimal(String.valueOf(imageViewHeigt))
                        .divide(new BigDecimal(String.valueOf(height)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(width)));
                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(height)));
                if (width > height) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(OrderDetailActivity.this, imageViewWidth),
                            DisplayUtils.dp2px(OrderDetailActivity.this, height_.floatValue()));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mDataBinding.hotPicture.setLayoutParams(layoutParams);
                } else {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(OrderDetailActivity.this, width_.floatValue()),
                            DisplayUtils.dp2px(OrderDetailActivity.this, imageViewHeigt));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mDataBinding.hotPicture.setLayoutParams(layoutParams);
                }
            }
        });
        Glide.with(this)
                .load(boughtArtVo.getArt().getImg_main_file1().getUrl()).apply(options)
                .into(mDataBinding.hotPicture);
    }
}