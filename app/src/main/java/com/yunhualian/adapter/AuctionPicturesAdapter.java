package com.yunhualian.adapter;


import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

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
import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.DisplayUtils;

import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class AuctionPicturesAdapter extends BaseQuickAdapter<AuctionArtVo, AuctionPicturesAdapter.TaskNewViewHolder> {

    public AuctionPicturesAdapter(List<AuctionArtVo> data) {
        super(R.layout.fragment_auction_sort_pictures_item, data);
    }

    @Override
    protected void convert(TaskNewViewHolder helper, AuctionArtVo item) {
        long countTime = 0;
        if (item.getServer_timestamp() < item.getStart_time()) {
            countTime = item.getStart_time() - item.getServer_timestamp();
        } else if (item.getServer_timestamp() < item.getEnd_time()) {
            countTime = item.getEnd_time() - item.getServer_timestamp();
        }
        helper.countDownTimer = new CountDownTimer(countTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                helper.setText(R.id.tv_auction_time, DateUtil.dateToTime(millisUntilFinished));
            }

            @Override
            public void onFinish() {
                helper.countDownTimer.cancel();
            }
        };

        if (countTime != 0) {
            helper.countDownTimer.start();
        }


        ImageView ivImage = helper.getView(R.id.hot_picture);
        helper.setText(R.id.picture_name, item.getArt().getName());

        helper.setText(R.id.picture_prize, YunApplication.PAY_CURRENCY.concat(" " + item.getArt().getPrice()));
        if (!TextUtils.isEmpty(item.getArt().getResource_type())) {
            if (item.getArt().getResource_type().equals("4")) {
                helper.setVisible(R.id.tv_video_tag, true);
            } else if (item.getArt().getResource_type().equals("3")) {
                helper.setText(R.id.live2d, "Live 2D");
                helper.setVisible(R.id.live2d, true);
            } else {
                helper.setVisible(R.id.tv_video_tag, false);
                helper.setVisible(R.id.live2d, false);
            }
        } else {
            helper.setVisible(R.id.tv_video_tag, false);
            helper.setVisible(R.id.live2d, false);
        }

        Glide.with(mContext).asBitmap().load(item.getArt().getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int height = DisplayUtils.px2dp(mContext, bitmap.getHeight());
                int width = DisplayUtils.px2dp(mContext, bitmap.getWidth());

                float realWidth = ScreenUtils.getScreenWidth() / 2f - 30;
                int imageViewWidth = DisplayUtils.px2dp(mContext, (int) realWidth);
                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(height)));
                ivImage.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtils.dp2px(mContext, height_.floatValue())));
            }
        });
        Glide.with(mContext)
                .load(item.getArt().getImg_main_file1().getUrl())
                .skipMemoryCache(true).transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImage);
    }

    @Override
    public void onViewRecycled(@NonNull @NotNull TaskNewViewHolder holder) {
        super.onViewRecycled(holder);
        ImageView imageView = holder.getView(R.id.hot_picture);
        if (imageView != null)
            Glide.with(mContext).clear(imageView);
    }

    static class TaskNewViewHolder extends BaseViewHolder {

        CountDownTimer countDownTimer = null;

        public TaskNewViewHolder(View view) {
            super(view);
        }
    }

}
