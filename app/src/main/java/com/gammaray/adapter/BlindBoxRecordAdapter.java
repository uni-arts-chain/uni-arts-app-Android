package com.gammaray.adapter;


import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.BlindBoxRecordVo;
import com.gammaray.utils.DateUtil;
import com.gammaray.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


public class BlindBoxRecordAdapter extends BaseQuickAdapter<BlindBoxRecordVo, BaseViewHolder> {
    private static final int TIME = 1000;
    private int orderType = 0;

    public BlindBoxRecordAdapter(List<BlindBoxRecordVo> data) {
        super(R.layout.activity_blindbox_record_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BlindBoxRecordVo item) {
        CardView cardView = helper.getView(R.id.picture_layout);
        helper.setText(R.id.order_time, DateUtil.dateToStringWith(item.getCreated_at() * TIME));
        helper.setVisible(R.id.order_cost, false);
        helper.setVisible(R.id.order_type, false);
        helper.setText(R.id.name, item.getName());
        helper.setGone(R.id.order_no, false);
        if (TextUtils.isEmpty(item.getAuthor()))
            helper.setText(R.id.art_name, mContext.getString(R.string.no_display_name));
        else
            helper.setText(R.id.art_name, item.getAuthor());
        helper.setText(R.id.addr, mContext.getString(R.string.nft_address, item.getNft_address()));

        ImageView imageView = helper.getView(R.id.hot_picture);

        Glide.with(mContext).asBitmap().load(item.getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int height = DisplayUtils.px2dp(mContext, bitmap.getHeight());
                int width = DisplayUtils.px2dp(mContext, bitmap.getWidth());
                int imageViewWidth = DisplayUtils.px2dp(mContext, cardView.getWidth());
                int imageViewHeigt = DisplayUtils.px2dp(mContext, cardView.getHeight());
                BigDecimal width_ = new BigDecimal(String.valueOf(imageViewHeigt))
                        .divide(new BigDecimal(String.valueOf(height)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(width)));
                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(height)));
                if (width > height) {
                    LogUtils.e("width > height " + height_.floatValue());
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(mContext, cardView.getWidth()),
                            DisplayUtils.dp2px(mContext, height_.floatValue()));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    imageView.setLayoutParams(layoutParams);
                } else {
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(mContext, width_.floatValue()),
                            DisplayUtils.dp2px(mContext, cardView.getHeight()));
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                    imageView.setLayoutParams(layoutParams);
                }
            }
        });
        Glide.with(mContext).load(item.getImg_main_file1().getUrl()) //图片地址
                .into(imageView);

    }
}
