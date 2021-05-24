package com.yunhualian.adapter;


import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.BlindBoxVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class BlindBoxDetailCardAdapter extends BaseQuickAdapter<BlindBoxVo.CardGroupsBean, BaseViewHolder> {

    public BlindBoxDetailCardAdapter(List<BlindBoxVo.CardGroupsBean> data) {
        super(R.layout.blindbox_card_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BlindBoxVo.CardGroupsBean item) {
        ImageView ivImage = helper.getView(R.id.hot_picture);
        if (TextUtils.isEmpty(item.getArt().getLive2d_file())) {
            helper.setVisible(R.id.live2d, false);
        } else helper.setVisible(R.id.live2d, true);

        if (TextUtils.isEmpty(item.getSpecial_attr())) {
            helper.setVisible(R.id.seldom, false);
        } else helper.setVisible(R.id.seldom, true);

        if (item.getArt().isIs_owner()) {
            helper.setVisible(R.id.already_have, true);
        } else helper.setVisible(R.id.already_have, false);

        Glide.with(mContext).asBitmap().load(item.getArt().getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int height = DisplayUtils.px2dp(mContext, bitmap.getHeight());
                int width = DisplayUtils.px2dp(mContext, bitmap.getWidth());
                int imageViewWidth = DisplayUtils.px2dp(mContext, ivImage.getWidth());
                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(height)));
                ivImage.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtils.dp2px(mContext, height_.floatValue())));
//                imageView.setImageBitmap(bitmap);
            }
        });
        Glide.with(mContext).load(item.getArt().getImg_main_file1().getUrl()).into(ivImage);

    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        ImageView ivImage = holder.getView(R.id.hot_picture);
        Glide.with(mContext).clear(ivImage);
    }
}
