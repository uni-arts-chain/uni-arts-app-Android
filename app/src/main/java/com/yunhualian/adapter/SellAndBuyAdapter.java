package com.yunhualian.adapter;


import android.graphics.Bitmap;
import android.media.Image;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.BoughtArtVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


/*collection_model = 3 为可拆分订单*/
public class SellAndBuyAdapter extends BaseQuickAdapter<BoughtArtVo, BaseViewHolder> {
    private static final int TIME = 1000;
    private static final int COLLECTION_MODEL = 3;
    private int orderType = 0;//0:卖 1：买

    public SellAndBuyAdapter(List<BoughtArtVo> data, int orderType) {
        super(R.layout.activity_sell_and_buy_tiem, data);
        this.orderType = orderType;
    }

    @Override
    protected void convert(BaseViewHolder helper, BoughtArtVo item) {
        helper.setText(R.id.order_time, DateUtil.dateToStringWithoutYear(item.getFinished_at() * TIME));
        String price;
        double royaltyValue = 0;
        double royalty = 0;
        if (orderType == 0) { //卖出订单
            helper.setGone(R.id.rotailRate, false);
        } else {
            if (item.getTrade_refer().equals("Auction")) {
                if (item.getArt().getRoyalty() == null || Double.parseDouble(item.getArt().getRoyalty()) == 0) {
                    helper.setGone(R.id.rotailRate, false);
                } else {
                    helper.setGone(R.id.rotailRate, true);
                    royalty = Double.parseDouble(item.getArt().getRoyalty());
                    double winPrice = Double.parseDouble(item.getAuction().getWin_price());
                    royaltyValue = winPrice * royalty;
                    BigDecimal bigDecimal = new BigDecimal(royaltyValue);
                    double royaltyDecimal = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    helper.setText(R.id.rotailRate, mContext.getString(R.string.text_contain_royalty, String.valueOf(royaltyDecimal)));
                }
            } else {
                if (item.getRoyalty() == null || Double.parseDouble(item.getRoyalty()) == 0) {
                    helper.setGone(R.id.rotailRate, false);
                } else {
                    helper.setGone(R.id.rotailRate, true);
                    helper.setText(R.id.rotailRate, mContext.getString(R.string.text_contain_royalty, String.valueOf(item.getRoyalty())));
                }
            }
        }

        if (item.getTrade_refer().equals("Auction")) {
            helper.setVisible(R.id.img_auction_tag, true);
            if (orderType == BigDecimal.ZERO.intValue()) {
                price = item.getAuction().getWin_price();
            } else {
                double winPrice = Double.parseDouble(item.getAuction().getWin_price());
                royalty = Double.parseDouble(item.getAuction().getRoyalty());
                price = new BigDecimal(winPrice + royalty).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
            }
            helper.setText(R.id.order_cost, YunApplication.PAY_CURRENCY.concat(price));
        } else {
            helper.setVisible(R.id.img_auction_tag, false);
            if (orderType == BigDecimal.ZERO.intValue()) {
                price = new BigDecimal(item.getAmount()).multiply(new BigDecimal(item.getPrice())).stripTrailingZeros().toPlainString();
                helper.setText(R.id.order_cost, YunApplication.PAY_CURRENCY.concat(price));
            } else {
                helper.setText(R.id.order_cost, YunApplication.PAY_CURRENCY.concat(item.getTotal_price()));
            }
        }
        helper.setText(R.id.name, item.getArt().getName());
        helper.setText(R.id.order_no, item.getSn());
        helper.setText(R.id.order_type, orderType == BigDecimal.ZERO.intValue() ?
                mContext.getString(R.string.order_sell) :
                mContext.getString(R.string.order_bought));
        if (item.getArt().getCollection_mode() == COLLECTION_MODEL) {
            helper.setVisible(R.id.prize, true);
            helper.setText(R.id.prize, "x".concat(new BigDecimal(item.getAmount()).stripTrailingZeros().toPlainString()));
        } else
            helper.setVisible(R.id.prize, false);
        if (TextUtils.isEmpty(item.getArt().getAuthor().getDisplay_name()))
            helper.setText(R.id.art_name, "");
        else
            helper.setText(R.id.art_name, item.getArt().getAuthor().getDisplay_name());
        helper.setText(R.id.addr, mContext.getString(R.string.nft_address, item.getArt().getItem_hash()));

        ImageView imageView = helper.getView(R.id.hot_picture);

        Glide.with(mContext).asBitmap().load(item.getArt().getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int height = DisplayUtils.px2dp(mContext, bitmap.getHeight());
                int width = DisplayUtils.px2dp(mContext, bitmap.getWidth());
                int imageViewWidth = 102;
                int imageViewHeigt = 76;
                BigDecimal width_ = new BigDecimal(String.valueOf(imageViewHeigt))
                        .divide(new BigDecimal(String.valueOf(height)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(width)));
                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(height)));
                if (width > height) {
                    LogUtils.e("width > height " + height_.floatValue());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(mContext, imageViewWidth),
                            DisplayUtils.dp2px(mContext, height_.floatValue()));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    imageView.setLayoutParams(layoutParams);
                } else {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(mContext, width_.floatValue()),
                            DisplayUtils.dp2px(mContext, imageViewHeigt));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    imageView.setLayoutParams(layoutParams);
                }
            }
        });
        Glide.with(mContext).load(item.getArt().getImg_main_file1().getUrl()) //图片地址
                .into(imageView);

    }
}
