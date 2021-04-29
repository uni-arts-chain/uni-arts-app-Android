package com.yunhualian.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.yunhualian.R;
import com.yunhualian.databinding.PopupwindowBlindBoxBinding;
import com.yunhualian.entity.BlindBoxCheckVO;
import com.yunhualian.entity.BlindBoxOpenVo;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;


public class BlindBoxOpenPop extends BasePopupWindow {


    private PopupwindowBlindBoxBinding mBinding;
    private List<BlindBoxOpenVo> lists;
    private OnPopItemClickListener listener;
    MZBannerView bannerView;
    public static final String Live2d = "3";

    public BlindBoxOpenPop(Context context, List<BlindBoxOpenVo> lists, OnPopItemClickListener listener) {
        super(context);
        this.lists = lists;
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

    public void setLists(List<BlindBoxOpenVo> lists) {
        mBinding.bannerView.setPages(lists, BannerViewHolder::new);
    }

    public static class BannerViewHolder implements MZViewHolder<BlindBoxOpenVo> {
        private ImageView mImageView;
        private TextView live2d;
        private TextView seldom;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            live2d = view.findViewById(R.id.live2d);
            seldom = view.findViewById(R.id.seldom);
            return view;
        }

        @Override
        public void onBind(Context context, int position, BlindBoxOpenVo data) {
            Glide.with(context).load(data.getImg_main_file1().getUrl()).into(mImageView);

            if (data.getResource_type().equals(BlindBoxOpenPop.Live2d)) {
                live2d.setVisibility(View.VISIBLE);
            } else live2d.setVisibility(View.GONE);

            if (TextUtils.isEmpty(data.getSpecial_attr())) {
                seldom.setVisibility(View.GONE);
            } else seldom.setVisibility(View.VISIBLE);


            mImageView.setOnClickListener(v -> {
                ToastUtils.showLong("click" + position);
            });
        }
    }
}
