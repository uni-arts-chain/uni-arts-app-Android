package com.yunhualian.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.utils.BlurTransformation;
import com.yunhualian.utils.DisplayUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class CommonPictureAdapter extends BaseQuickAdapter<SellingArtVo, BaseViewHolder> {
    List<SellingArtVo> lists;
    private int radius = 20;
    Context context;

    public CommonPictureAdapter(List<SellingArtVo> data, Context context) {
        super(R.layout.fragment_home_theme_item, data);
        this.lists = data;
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, SellingArtVo item) {
        RelativeLayout relativeLayout = helper.getView(R.id.seeMoreLayout);
        View overPage = helper.getView(R.id.zhe);
        helper.setText(R.id.picture_name, item.getName());
        helper.setText(R.id.picture_prize, YunApplication.PAY_CURRENCY.concat(item.getPrice()));
        if(!TextUtils.isEmpty(item.getResource_type())){
            if (item.getResource_type().equals("4")) {
                helper.setVisible(R.id.img_video_tag, true);
            } else {
                helper.setVisible(R.id.img_video_tag, false);
            }
        }else{
            helper.setVisible(R.id.img_video_tag, false);
        }
//        Glide.with(mContext).asBitmap().load(item.getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//                int height = DisplayUtils.px2dp(mContext, bitmap.getHeight());
//                int width = DisplayUtils.px2dp(mContext, bitmap.getWidth());
//                float default_width = (ScreenUtils.getScreenWidth() - 15f * 2 - 10f * 3) / 3.2f;
//                int imageViewWidth = DisplayUtils.px2dp(mContext, default_width);
//                int imageViewHeigt = DisplayUtils.dp2px(mContext, 146);
//                BigDecimal width_ = new BigDecimal(String.valueOf(imageViewHeigt))
//                        .divide(new BigDecimal(String.valueOf(height)), 2, RoundingMode.HALF_DOWN)
//                        .multiply(new BigDecimal(String.valueOf(width)));
//                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
//                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
//                        .multiply(new BigDecimal(String.valueOf(height)));
////                if (width > height) {
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) default_width,
//                        DisplayUtils.dp2px(mContext, 146));
//                params.addRule(RelativeLayout.CENTER_IN_PARENT);
//                imageView.setLayoutParams(params);
////                } else {
////                    imageView.setLayoutParams(new RelativeLayout.LayoutParams((int) width_.floatValue(),
////                            ViewGroup.LayoutParams.MATCH_PARENT));
////                }
//            }
//        });
        if (helper.getPosition() == (lists.size() - 1)) {
            Glide.with(context).load(item.getImg_main_file1().getUrl()) //图片地址
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(context, radius)))
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(withCrossFade())
                    .into((ImageView) helper.getView(R.id.picture));
            relativeLayout.setVisibility(View.VISIBLE);
            overPage.setVisibility(View.VISIBLE);
            helper.setText(R.id.picture_name, "");
            helper.setText(R.id.picture_prize, "");
        } else {
            relativeLayout.setVisibility(View.GONE);
            overPage.setVisibility(View.GONE);
            Glide.with(context)
                    .load(item.getImg_main_file1().getUrl()) //图片地址
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(withCrossFade())
                    .into((ImageView) helper.getView(R.id.picture));
        }
    }
}
