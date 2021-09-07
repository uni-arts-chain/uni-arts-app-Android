package com.gammaray.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.base.YunApplication;
import com.gammaray.entity.AuctionArtVo;
import com.gammaray.utils.DateUtil;
import com.gammaray.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class AuctionRecordsAdapter extends BaseQuickAdapter<AuctionArtVo, AuctionRecordsAdapter.CountDownTimeViewHolder> {
    private static final int TIME = 1000;
    private String mState;
    private static final String ATTEND = "attend";
    private static final String BID = "bid";
    private static final String WIN = "win";
    private static final String FINISH = "finish";
    private Context mContext;
    private List<CountDownTimer> mTimerList = new ArrayList<>();

    public AuctionRecordsAdapter(Context context, List<AuctionArtVo> data, String state) {
        super(R.layout.item_auction_record_layout, data);
        this.mState = state;
        this.mContext = context;
    }

    private String getTv(long l) {
        if (l >= 10) {
            return l + "";
        } else {
            return "0" + l;//小于10,,前面补位一个"0"
        }
    }

    @Override
    protected void convert(CountDownTimeViewHolder helper, AuctionArtVo item) {
        helper.setText(R.id.order_time, DateUtil.dateToStringWithoutYear(item.getCreated_at() * TIME));
        if (mState.equals(ATTEND)) {
            helper.setVisible(R.id.order_cost_layout, true);
            helper.setGone(R.id.tv_to_pay, false);
            helper.setGone(R.id.tv_count_time_hint, false);
            helper.setVisible(R.id.order_cost, true);
            helper.setText(R.id.order_type, "已缴纳保证金");
            helper.setText(R.id.order_cost, "¥" + item.getDeposit_amount());
        } else if (mState.equals(BID)) {
            helper.setVisible(R.id.order_cost_layout, true);
            helper.setVisible(R.id.order_cost, true);
            helper.setGone(R.id.tv_to_pay, false);
            helper.setGone(R.id.tv_count_time_hint, false);
            helper.setText(R.id.order_type, "已出价");
            helper.setText(R.id.order_cost, "¥" + item.getCurrent_user_highest_price());
        } else if (mState.equals(WIN)) {
            helper.setGone(R.id.order_cost_layout, false);
            helper.setGone(R.id.order_cost, false);
            helper.setVisible(R.id.tv_to_pay, true);
            String totalPrice;
            if (item.getRoyalty() == null || Double.parseDouble(item.getRoyalty()) == 0) {
                totalPrice = (new BigDecimal(item.getWin_price()).
                        subtract(new BigDecimal(item.getDeposit_amount()))).
                        setScale(2, BigDecimal.ROUND_UP).
                        stripTrailingZeros().
                        toPlainString();
            } else {
                totalPrice = (new BigDecimal(item.getWin_price()).
                        subtract(new BigDecimal(item.getDeposit_amount()))
                        .add(new BigDecimal(item.getRoyalty()))).
                        setScale(2, BigDecimal.ROUND_UP).
                        stripTrailingZeros().
                        toPlainString();
            }
            helper.setText(R.id.tv_to_pay, "去支付 ¥" + totalPrice);
            helper.setVisible(R.id.tv_count_time_hint, true);
            helper.countDownTimer = new CountDownTimer((item.getEnd_time() + item.getPay_timeout() - item.getServer_timestamp()) * 1000, 1000) {
                @Override
                public void onTick(long seconds) {
                    long hours = seconds / (3600 * 1000);            //转换小时
                    seconds = seconds % (3600 * 1000);
                    long minutes = seconds / (60 * 1000);            //转换分钟
                    seconds = seconds % (60 * 1000);
                    seconds = seconds / 1000;                       //转换秒钟
                    String countTime = mContext.getString(R.string.time_holder, getTv(hours), getTv(minutes), getTv(seconds));
//                    helper.setText(R.id.tv_auction_time, mContext.getString(R.string.time_holder, getTv(hours), getTv(minutes), getTv(seconds)));
                    helper.setText(R.id.tv_count_time_hint, mContext.getString(R.string.count_time_hint, countTime));
                }

                @Override
                public void onFinish() {
                    if (helper.countDownTimer != null) {
                        helper.countDownTimer.cancel();
                    }
                    helper.setText(R.id.tv_to_pay, "超时未支付，已扣除保证金");
                    helper.setBackgroundRes(R.id.tv_to_pay, R.drawable.shape_bg_gray);
                }
            }.start();
            mTimerList.add(helper.countDownTimer);
        } else if (mState.equals(FINISH)) {
            helper.setVisible(R.id.order_cost_layout, true);
            helper.setGone(R.id.tv_to_pay, false);
            helper.setGone(R.id.tv_count_time_hint, false);
            helper.setGone(R.id.order_cost, false);
            if (item.isIs_settlement()) {
                helper.setText(R.id.order_type, "正在结算中");
            } else {
                if (item.getBuyer() != null && item.getBuyer().getId() == YunApplication.getmUserVo().getId()) {
                    if (item.isIs_paying()) {
                        helper.setText(R.id.order_type, "正在支付中");
                    } else {
                        if (item.isBuyer_paid()) {
                            helper.setText(R.id.order_type, "中标已支付");
                        } else {
                            helper.setText(R.id.order_type, "中标未支付，已扣除保证金¥" + item.getDeposit_amount());
                        }
                    }
                } else {
                    helper.setText(R.id.order_type, "未中标");
                }
            }
        }

        if (item.getArt() != null) {
            if (item.getArt().getName() != null) {
                helper.setText(R.id.name, item.getArt().getName());
            }
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
        helper.addOnClickListener(R.id.tv_to_pay);
    }

    public void clearAllTimer() {
        if (mTimerList != null && mTimerList.size() > 0) {
            for (CountDownTimer timer : mTimerList) {
                timer.cancel();
            }
            mTimerList.clear();
        }
    }

    static class CountDownTimeViewHolder extends BaseViewHolder {

        CountDownTimer countDownTimer = null;

        public CountDownTimeViewHolder(View view) {
            super(view);
        }
    }
}
