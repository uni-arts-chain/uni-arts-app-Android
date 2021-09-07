package com.gammaray.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gammaray.R;
import com.gammaray.databinding.PopupwindowBlindBoxBinding;
import com.gammaray.entity.BlindBoxOpenVo;
import com.gammaray.utils.DisplayUtils;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class BlindBoxOpenPop extends BasePopupWindow {


    private PopupwindowBlindBoxBinding mBinding;
    private List<BlindBoxOpenVo> lists;
    private OnPopItemClickListener listener;
    MZBannerView bannerView;
    public static final String Live2d = "3";

    public BlindBoxOpenPop(Context context, OnPopItemClickListener listener) {
        super(context);
        this.listener = listener;
    }


    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.popupwindow_blind_box, null, false);


        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
        List<String> bannersVoList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            bannersVoList.add(i + "page");
        }

        if (bannersVoList.size() > 0) {
            List<String> lists = new ArrayList<>();

            for (String bannersVo : bannersVoList) {
                lists.add(bannersVo);
            }
            mBinding.bannerView.setIndicatorVisible(false);
            mBinding.bannerView.start();
        }
        mBinding.bannerView.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(lists != null && lists.size() > 0){
                    mBinding.cardName.setText(lists.get(position).getName());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mBinding.goMainPage.setOnClickListener(v -> {
            listener.onPopItemClick(v, 0);
        });
        mBinding.close.setOnClickListener(v -> dismiss());
    }

    public interface OnPopItemClickListener {
        void onPopItemClick(View view, int position);
    }

    public interface OnBottomTextviewClickListener {
        void onBottomClick();
    }

    public class ClickHandlers {

        public void onClickDeposit() {
            dismiss();

        }

        public void onClickWithdraw() {
            dismiss();

        }

        public void onClickTransfer() {
            dismiss();
        }
    }

    public void setCardName(String cardName) {
        mBinding.cardName.setText(cardName);
    }

    public void setLists(List<BlindBoxOpenVo> lists) {
        this.lists = lists;
        if(lists != null && lists.size() > 0){
            mBinding.cardName.setText(lists.get(0).getName());
        }
        mBinding.bannerView.setPages(lists, BannerViewHolder::new);
    }

    public static class BannerViewHolder implements MZViewHolder<BlindBoxOpenVo> {
        private ImageView mImageView;
        private TextView live2d;
        private TextView seldom;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.blind_box_banner_item, null);
            mImageView = view.findViewById(R.id.banner_image);
            live2d = view.findViewById(R.id.live2d);
            seldom = view.findViewById(R.id.seldom);
            return view;
        }

        @Override
        public void onBind(Context context, int position, BlindBoxOpenVo data) {
            Glide.with(context).asBitmap().load(data.getImg_main_file1().getUrl()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    int height = DisplayUtils.px2dp(context, bitmap.getHeight());
                    int width = DisplayUtils.px2dp(context, bitmap.getWidth());

                    int imageViewWidth = 270;
                    int imageViewHeight = DisplayUtils.px2dp(context, ScreenUtils.getScreenWidth() - 100);
                    BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                            .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                            .multiply(new BigDecimal(String.valueOf(height)));

                    BigDecimal width_ = new BigDecimal(String.valueOf(imageViewHeight))
                            .divide(new BigDecimal(String.valueOf(height)), 2, RoundingMode.HALF_DOWN)
                            .multiply(new BigDecimal(String.valueOf(width)));
                    LogUtils.e("height =  " + height_);
                    if (height > width) {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DisplayUtils.dp2px(context, width_.floatValue()),
                                ViewGroup.LayoutParams.MATCH_PARENT);
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                        mImageView.setLayoutParams(layoutParams);
                    } else {
                        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                DisplayUtils.dp2px(context, height_.floatValue())));
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                        mImageView.setLayoutParams(layoutParams);
                        mImageView.setLayoutParams(layoutParams);
                    }
//                    mImageView.setImageBitmap(bitmap);
                }
            });

            Glide.with(context).load(data.getImg_main_file1().getUrl()).into(mImageView);
            if (data.getResource_type().equals(BlindBoxOpenPop.Live2d)) {
                live2d.setVisibility(View.VISIBLE);
            } else live2d.setVisibility(View.GONE);

            if (TextUtils.isEmpty(data.getSpecial_attr())) {
                seldom.setVisibility(View.GONE);
            } else seldom.setVisibility(View.VISIBLE);

            mImageView.setOnClickListener(v -> {
//                ToastUtils.showLong("click" + position);
            });
        }
    }
}
