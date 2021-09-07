package com.gammaray.adapter;


import android.graphics.Bitmap;
import android.text.TextUtils;
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
import com.gammaray.R;
import com.gammaray.base.YunApplication;
import com.gammaray.entity.SellingArtVo;
import com.gammaray.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class PicturesAdapter extends BaseQuickAdapter<SellingArtVo, BaseViewHolder> {

    public PicturesAdapter(List<SellingArtVo> data) {
        super(R.layout.fragment_sort_pictures_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SellingArtVo item) {
        ImageView ivImage = helper.getView(R.id.hot_picture);
        helper.setText(R.id.picture_name, item.getName());

        helper.setText(R.id.picture_prize, YunApplication.PAY_CURRENCY.concat(" " + item.getPrice()));
        if (!TextUtils.isEmpty(item.getResource_type())) {
            if (item.getResource_type().equals("4")) {
                helper.setVisible(R.id.tv_video_tag, true);
            } else if (item.getResource_type().equals("3")) {
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

        Glide.with(mContext).asBitmap().load(item.getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
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
                .load(item.getImg_main_file1().getUrl())
                .skipMemoryCache(true).transition(withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImage);
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
        ImageView imageView = holder.getView(R.id.hot_picture);
        if (imageView != null)
            Glide.with(mContext).clear(imageView);
    }
}
