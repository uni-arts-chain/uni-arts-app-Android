package com.yunhualian.ui.activity.order;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

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
import com.yunhualian.databinding.ActivityAuctionOrderDetailBinding;
import com.yunhualian.databinding.ActivityOrderDetailBinding;
import com.yunhualian.entity.BoughtArtVo;
import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AuctionOrderDetailActivity extends BaseActivity<ActivityAuctionOrderDetailBinding> {
    public static final String BOUGHT_KEY = "bought_vo";
    public static final String ORDER_TYPE = "order_type";
    BoughtArtVo boughtArtVo;
    private int orderType = 0; //1 是买入，0是卖出
    double rotalyPrice;
    double depositPrice;
    double winPrice;
    double realPrice;

    @Override
    public int getLayoutId() {
        return R.layout.activity_auction_order_detail;
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
        mDataBinding.orderCreateTime.setText(getString(R.string.order_create_time, DateUtil.dateToStringWith(boughtArtVo.getFinished_at() * 1000)));
        mDataBinding.name.setText(boughtArtVo.getArt().getName());
        mDataBinding.orderInfo.setText(boughtArtVo.getSn());
        double royaltyValue;
        double royalty = 0;
        if (orderType == 0) {
            mDataBinding.rlRotateLayout.setVisibility(View.GONE);
        } else {
            if (boughtArtVo.getAuction().getRoyalty() == null) {
                mDataBinding.rlRotateLayout.setVisibility(View.GONE);
            } else {
                if (Double.parseDouble(boughtArtVo.getAuction().getRoyalty()) == 0) {
                    mDataBinding.rlRotateLayout.setVisibility(View.GONE);
                } else {
                    mDataBinding.rlRotateLayout.setVisibility(View.VISIBLE);
                    royalty = Double.parseDouble(boughtArtVo.getArt().getRoyalty());
                    int royaltyPercent = (int) (royalty * 100);
                    mDataBinding.rotayRate.setText("(" + royaltyPercent + "%)");
                    rotalyPrice = Double.valueOf(boughtArtVo.getAuction().getRoyalty());
//                    double winPrice = Double.parseDouble(boughtArtVo.getAuction().getWin_price());
//                    royaltyValue = winPrice * royalty;
//                    BigDecimal bigDecimal = new BigDecimal(royaltyValue);
//                    rotalyPrice = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    mDataBinding.rotayPrice.setText(getString(R.string.text_contain_royalty, boughtArtVo.getAuction().getRoyalty()));
                }
            }
        }


        if (boughtArtVo.getAuction() != null) {
            depositPrice = Double.parseDouble(boughtArtVo.getDeposit()); //保证金
            winPrice = Double.parseDouble(boughtArtVo.getAuction().getWin_price()); //拍中价
            realPrice = winPrice + rotalyPrice; //实付款
        }

        if (orderType == 1) {
            mDataBinding.llBuyIn.setVisibility(View.VISIBLE);
            mDataBinding.llSoldOut.setVisibility(View.GONE);
            mDataBinding.memo.setText("1");
            mDataBinding.tvAuctionPriceV.setText("¥" + new BigDecimal(winPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
            mDataBinding.tvDepositValue.setText("¥" + boughtArtVo.getDeposit());
            mDataBinding.tvTotalPriceValue.setText("¥" + new BigDecimal(realPrice).setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            mDataBinding.llBuyIn.setVisibility(View.GONE);
            mDataBinding.llSoldOut.setVisibility(View.VISIBLE);
            mDataBinding.tvSoldCount.setText("1");
            mDataBinding.tvTotalPriceValue.setText("¥" + boughtArtVo.getAuction().getWin_price());
            mDataBinding.artPrize.setText("¥" + boughtArtVo.getAuction().getWin_price());
        }
        if (TextUtils.isEmpty(boughtArtVo.getArt().getAuthor().getDisplay_name()))
            mDataBinding.artName.setText(getString(R.string.no_display_name));
        else mDataBinding.artName.setText(boughtArtVo.getArt().getAuthor().getDisplay_name());
        String amount = TextUtils.isEmpty(boughtArtVo.getAmount()) ? "0" : new BigDecimal(boughtArtVo.getAmount()).stripTrailingZeros().toPlainString();
        mDataBinding.memo.setText(amount.concat("份"));
        mDataBinding.addr.setText(getString(R.string.nft_address, boughtArtVo.getArt().getItem_hash()));

        mDataBinding.copyAction.setOnClickListener(v -> {
            if (TextUtils.isEmpty(boughtArtVo.getSn())) {
                ToastUtils.showLongToast(AuctionOrderDetailActivity.this, getString(R.string.no_order_sn));
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
                int height = DisplayUtils.px2dp(AuctionOrderDetailActivity.this, bitmap.getHeight());
                int width = DisplayUtils.px2dp(AuctionOrderDetailActivity.this, bitmap.getWidth());
                int imageViewWidth = 102;
                int imageViewHeigt = 76;
                BigDecimal width_ = new BigDecimal(String.valueOf(imageViewHeigt))
                        .divide(new BigDecimal(String.valueOf(height)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(width)));
                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(height)));
                if (width > height) {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(AuctionOrderDetailActivity.this, imageViewWidth),
                            DisplayUtils.dp2px(AuctionOrderDetailActivity.this, height_.floatValue()));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    mDataBinding.hotPicture.setLayoutParams(layoutParams);
                } else {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(AuctionOrderDetailActivity.this, width_.floatValue()),
                            DisplayUtils.dp2px(AuctionOrderDetailActivity.this, imageViewHeigt));
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