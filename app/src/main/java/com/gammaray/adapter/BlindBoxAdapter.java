package com.gammaray.adapter;


import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.base.YunApplication;
import com.gammaray.entity.BlindBoxVo;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BlindBoxAdapter extends BaseQuickAdapter<BlindBoxVo, BaseViewHolder> {

    public BlindBoxAdapter(List<BlindBoxVo> data) {
        super(R.layout.fragment_blind_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BlindBoxVo item) {
        ImageView ivImage = helper.getView(R.id.hot_picture);
        Glide.with(mContext).clear(ivImage);
        Glide.with(mContext).load(item.getApp_img_path()).transition(withCrossFade()).into(ivImage);
        helper.setText(R.id.picture_name, item.getTitle());
        helper.setText(R.id.desc, Html.fromHtml(item.getDesc()));
        helper.setText(R.id.picture_prize, YunApplication.PAY_CURRENCY.concat(" " + item.getPrice()));


//        Glide.with(mContext).asBitmap().load(item.getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//                int height = DisplayUtils.px2dp(mContext, bitmap.getHeight());
//                int width = DisplayUtils.px2dp(mContext, bitmap.getWidth());
//
//                int imageViewWidth = DisplayUtils.px2dp(mContext, ivImage.getWidth());
//                LogUtils.e("height =  " + height + "||width = " + width + "||imgview width = " + imageViewWidth + "||height = "
//                        + ((int) (imageViewWidth / width)) * height);
//                BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
//                        .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
//                        .multiply(new BigDecimal(String.valueOf(height)));
//                LogUtils.e("height =  " + height_);
//                ivImage.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        DisplayUtils.dp2px(mContext, height_.floatValue())));
//
//                bitmap.recycle();
//            }
//        });

//        Glide.with(mContext)
//                .asBitmap().load(item.getImg_main_file1().getUrl())
//                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//                        //这个bitmap就是你图片url加载得到的结果
//                        //获取bitmap信息，可赋值给外部变量操作，也可在此时行操作。
//                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();//获取你要填充图片的布局的layoutParam
//                        layoutParams.height = (int) (((float) bitmap.getHeight()) / bitmap.getWidth() * ScreenUtils.getScreenWidth() / 2);
//                        //因为是2列,所以宽度是屏幕的一半,高度是根据bitmap的高/宽*屏幕宽的一半
//                        layoutParams.width = ScreenUtils.getScreenWidth() / 2;//这个是布局的宽度
//                        ivImage.setLayoutParams(layoutParams);//容器的宽高设置好了
//                        bitmap = zoomImg(bitmap, layoutParams.width, layoutParams.height);
//                        // 然后在改变一下bitmap的宽高
//                        ivImage.setImageBitmap(bitmap);
//                    }
//                });
    }
}
