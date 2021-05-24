package com.yunhualian.adapter;


import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.ArtistListVo;
import com.yunhualian.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CreatorChildPictureAdapter extends BaseQuickAdapter<ArtistListVo.ArtsBean, BaseViewHolder> {

    public CreatorChildPictureAdapter(List<ArtistListVo.ArtsBean> data) {
        super(R.layout.fragment_creater_child_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ArtistListVo.ArtsBean item) {
        helper.setText(R.id.picture_name, item.getName());
        helper.setText(R.id.picture_prize, YunApplication.PAY_CURRENCY.concat(item.getPrice()));

//        Glide.with(mContext).asBitmap().load(item.getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//                int height = DisplayUtils.px2dp(mContext, bitmap.getHeight());
//                int width = DisplayUtils.px2dp(mContext, bitmap.getWidth());
//                float default_width = (ScreenUtils.getScreenWidth() - 15f * 2 - 10f * 2) / 3f;
//                int imageViewWidth = DisplayUtils.px2dp(mContext, default_width);
//                int imageViewHeigt = DisplayUtils.dp2px(mContext, 110);
//                BigDecimal width_ = new BigDecimal(String.valueOf(imageViewHeigt))
//                        .divide(new BigDecimal(String.valueOf(height)), 2, RoundingMode.HALF_DOWN)
//                        .multiply(new BigDecimal(String.valueOf(width)));
//                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
//                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
//                        .multiply(new BigDecimal(String.valueOf(height)));
//                if (width > height) {
//                    LogUtils.e("width > height " + height_.floatValue());
//                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) default_width,
//                            DisplayUtils.dp2px(mContext, height_.floatValue()));
//                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
//                    imageView.setLayoutParams(params);
//                } else {
//                    LogUtils.e("height > width " + width_.floatValue());
//                    imageView.setLayoutParams(new RelativeLayout.LayoutParams((int) width_.floatValue(),
//                            ViewGroup.LayoutParams.MATCH_PARENT));
//                }
//            }
//        });

        Glide.with(mContext)
                .load(item.getImg_main_file1().getUrl()) //图片地址
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(withCrossFade())
                .into((ImageView) helper.getView(R.id.picture));
    }
}
