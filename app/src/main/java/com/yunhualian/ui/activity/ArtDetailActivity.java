package com.yunhualian.ui.activity;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.yunhualian.R;
import com.yunhualian.adapter.PicDetailAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivityArtDetailBinding;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Observer;

public class ArtDetailActivity extends BaseActivity<ActivityArtDetailBinding> {

    PicDetailAdapter picDetailAdapter;
    int totalAmount;
    List<String> lists = Arrays.asList("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1573022417628&di=2c1a1a2b8de14cb8721c761cfdbd189a&imgtype=0&src=http%3A%2F%2Fpic27.nipic.com%2F20130313%2F9252150_092049419327_2.jpg",
            "https://i3.wenshen520.com/25257_0.jpg",
            "https://truth.bahamut.com.tw/s01/201601/88f5d73bb1e77e536bdd3e619bb041aa.JPG",
            "http://i2.bvimg.com/607307/5d1d51c2d25e5c5ct.jpg");

    @Override
    public int getLayoutId() {
        return R.layout.activity_art_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

//        List<Integer> lists = Arrays.asList(R.mipmap.shanshui, R.mipmap.shanshui, R.mipmap.shanshui);
        totalAmount = lists.size();
        mDataBinding.cursorTxt.setText("1/".concat("" + totalAmount));
        mDataBinding.banner.setPages(lists, BannerViewHolder::new);
        mDataBinding.largeAction.setOnClickListener(v -> {
            showBigImg();
        });
        mDataBinding.banner.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mDataBinding.cursorTxt.setText(position + 1 + "/" + totalAmount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.popular));
        picDetailAdapter = new PicDetailAdapter(list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mDataBinding.artDeatils.setLayoutManager(linearLayoutManager);
        mDataBinding.artDeatils.setAdapter(picDetailAdapter);
    }

    private void showBigImg() {

    }

    public static class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
//            mImageView.setImageResource(data);
            Glide.with(context).load(data).into(mImageView);
        }
    }
}