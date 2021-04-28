package com.yunhualian.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.SellingArtVo;

import java.util.List;

public class HomePagePopularAdapter extends BaseQuickAdapter<SellingArtVo, BaseViewHolder> {

    public HomePagePopularAdapter(List<SellingArtVo> data) {
        super(R.layout.fragment_sort_pictures_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, SellingArtVo item) {
        ImageView imageView = helper.getView(R.id.hot_picture);
        Glide.with(mContext).clear(imageView);
        if (item.getImg_main_file1() != null)
            Glide.with(mContext).load(item.getImg_main_file1().getUrl()).into(imageView);

        helper.setText(R.id.picture_name, item.getName());
        helper.setText(R.id.picture_prize, YunApplication.PAY_CURRENCY.concat(" " + item.getPrice()));

//        Glide.with(mContext).asBitmap().load(item.getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//                int height = DisplayUtils.px2dp(mContext, bitmap.getHeight());
//                int width = DisplayUtils.px2dp(mContext, bitmap.getWidth());
//                int imageViewWidth = DisplayUtils.px2dp(mContext, imageView.getWidth());
//                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
//                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
//                        .multiply(new BigDecimal(String.valueOf(height)));
//                imageView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        DisplayUtils.dp2px(mContext, height_.floatValue())));
//                imageView.setImageBitmap(bitmap);
//            }
//        });
//        if (position  == 0) {
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(mContext, 110)));
//        } else {
//            imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(mContext, 240)));
//        }

    }
}
