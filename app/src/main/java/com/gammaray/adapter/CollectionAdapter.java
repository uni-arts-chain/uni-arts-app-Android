package com.gammaray.adapter;


import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.base.YunApplication;
import com.gammaray.entity.CollectArtVo;
import com.gammaray.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CollectionAdapter extends BaseQuickAdapter<CollectArtVo, BaseViewHolder> {

    public CollectionAdapter(List<CollectArtVo> data) {
        super(R.layout.fragment_sort_pictures_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, CollectArtVo item) {
        ImageView imageView = helper.getView(R.id.hot_picture);

        helper.setText(R.id.picture_name, item.getFavoritable().getName());
        helper.setText(R.id.picture_prize, YunApplication.PAY_CURRENCY.concat(" " + item.getFavoritable().getPrice()));
        if (TextUtils.isEmpty(item.getFavoritable().getLive2d_file())) {
            helper.setVisible(R.id.live2d, false);
        } else{
            helper.setText(R.id.live2d,"Live 2D");
            helper.setVisible(R.id.live2d, true);
        }
        if(!TextUtils.isEmpty(item.getFavoritable().getResource_type())){
            if (item.getFavoritable().getResource_type().equals("4")) {
                helper.setVisible(R.id.tv_video_tag, true);
            } else {
                helper.setVisible(R.id.tv_video_tag, false);
            }
        }else{
            helper.setVisible(R.id.tv_video_tag, false);
        }
        Glide.with(mContext).asBitmap().load(item.getFavoritable().getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                int height = DisplayUtils.px2dp(mContext, bitmap.getHeight());
                int width = DisplayUtils.px2dp(mContext, bitmap.getWidth());

                float realWidth = ScreenUtils.getScreenWidth() / 2f - 50;
                int imageViewWidth = DisplayUtils.px2dp(mContext, (int) realWidth);
                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                        .multiply(new BigDecimal(String.valueOf(height)));
                imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        DisplayUtils.dp2px(mContext, height_.floatValue())));
//                imageView.setImageBitmap(bitmap);
            }
        });
        Glide.with(mContext).load(item.getFavoritable().getImg_main_file1().getUrl()).transition(withCrossFade()).into(imageView);
//        if (position  == 0) {
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(mContext, 110)));
//        } else {
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(mContext, 240)));
//        }

    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder holder) {
        super.onViewRecycled(holder);
//        ImageView imageView = holder.getView(R.id.hot_picture);
//        Glide.with(mContext).clear(imageView);
    }
}
