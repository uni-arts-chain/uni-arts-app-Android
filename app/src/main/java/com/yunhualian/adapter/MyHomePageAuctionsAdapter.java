package com.yunhualian.adapter;


import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.AuctionArtVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class MyHomePageAuctionsAdapter extends BaseQuickAdapter<AuctionArtVo, MyHomePageAuctionsAdapter.TaskNewViewHolder> {

    private List<CountDownTimer> mTimerList = new ArrayList<>();
    private String type;

    private String getTv(long l) {
        if (l >= 10) {
            return l + "";
        } else {
            return "0" + l;//小于10,,前面补位一个"0"
        }
    }

    public void clearAllTimer() {
        if (mTimerList != null && mTimerList.size() > 0) {
            for (CountDownTimer timer : mTimerList) {
                timer.cancel();
            }
            mTimerList.clear();
        }
    }

    public MyHomePageAuctionsAdapter(List<AuctionArtVo> data) {
        super(R.layout.my_homepage_auction_item, data);
    }

    @Override
    protected void convert(TaskNewViewHolder helper, AuctionArtVo item) {

        long countTime = 0;
        if (item.getServer_timestamp() < item.getStart_time()) {
            countTime = item.getStart_time() - item.getServer_timestamp();
            type = "yet";
            helper.setVisible(R.id.img_clock, false);
            helper.setGone(R.id.tv_status, true);
            helper.setText(R.id.tv_status, "距开始：");
        } else if (item.getServer_timestamp() < item.getEnd_time()) {
            countTime = item.getEnd_time() - item.getServer_timestamp();
            type = "start";
            helper.setGone(R.id.tv_status, false);
            helper.setVisible(R.id.img_clock, true);
            helper.setText(R.id.tv_status, "");
        } else {
            type = "end";
            countTime = 0;
            helper.setGone(R.id.tv_status, false);
            helper.setVisible(R.id.img_clock, true);
            helper.setText(R.id.tv_status, "");
        }

        helper.countDownTimer = new CountDownTimer(countTime * 1000, 1000) {
            @Override
            public void onTick(long seconds) {
                long hours = seconds / (3600 * 1000);            //转换小时
                seconds = seconds % (3600 * 1000);
                long minutes = seconds / (60 * 1000);            //转换分钟
                seconds = seconds % (60 * 1000);
                seconds = seconds / 1000;                       //转换秒钟
                helper.setText(R.id.tv_auction_time, mContext.getString(R.string.time_holder, getTv(hours), getTv(minutes), getTv(seconds)));
            }

            @Override
            public void onFinish() {
                helper.setText(R.id.tv_auction_time, mContext.getString(R.string.time_holder, getTv(0), getTv(0), getTv(0)));
                if (helper.countDownTimer != null) {
                    helper.countDownTimer.cancel();
                }
            }
        }.start();
        mTimerList.add(helper.countDownTimer);

        ImageView ivImage = helper.getView(R.id.hot_picture);
        helper.setText(R.id.picture_name, item.getArt().getName());
        helper.setText(R.id.picture_prize, YunApplication.PAY_CURRENCY.concat(" " + item.getStart_price()));

        if (!TextUtils.isEmpty(item.getArt().getResource_type())) {
            if (item.getArt().getResource_type().equals("4")) {
                helper.setVisible(R.id.tv_video_tag, true);
            } else if (item.getArt().getResource_type().equals("3")) {
                helper.setVisible(R.id.tv_live2d_tag, true);
            } else {
                helper.setVisible(R.id.tv_video_tag, false);
                helper.setVisible(R.id.tv_live2d_tag, false);
            }
        } else {
            helper.setVisible(R.id.tv_video_tag, false);
            helper.setVisible(R.id.tv_live2d_tag, false);
        }

        Glide.with(mContext).asBitmap().load(item.getArt().getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int height = DisplayUtils.px2dp(mContext, bitmap.getHeight());
                int width = DisplayUtils.px2dp(mContext, bitmap.getWidth());
                float realWidth = ScreenUtils.getScreenWidth() / 2f - 50;
                int imageViewWidth = DisplayUtils.px2dp(mContext, (int) realWidth);
                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(height)));
                ivImage.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtils.dp2px(mContext, height_.floatValue())));
                LogUtils.e("width = " + width + "||height = " + height + "||ImageViewWidth" + imageViewWidth);
            }
        });
        Glide.with(mContext).load(item.getArt().getImg_main_file1().getUrl()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into(ivImage);
        if (item.isCan_cancel()) {
            helper.setTextColor(R.id.cancelAuction, mContext.getResources().getColor(R.color._101010));
            helper.setBackgroundRes(R.id.cancelAuction, R.drawable.shape_bg_black);
        } else {
            helper.setTextColor(R.id.cancelAuction, mContext.getResources().getColor(R.color._999999));
            helper.setBackgroundRes(R.id.cancelAuction, R.drawable.shape_bg_gray);
        }
        helper.addOnClickListener(R.id.cancelAuction);
    }

    @Override
    public void onViewRecycled(@NonNull TaskNewViewHolder holder) {
        super.onViewRecycled(holder);
        ImageView ivImage = holder.getView(R.id.hot_picture);
        if (ivImage != null)
            Glide.with(mContext).clear(ivImage);
    }

    static class TaskNewViewHolder extends BaseViewHolder {

        CountDownTimer countDownTimer = null;

        public TaskNewViewHolder(View view) {
            super(view);
        }
    }
}
