package com.yunhualian.adapter;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.ArtTopicVo;
import com.yunhualian.ui.activity.ArtDetailActivity;

import java.util.List;

public class HomePageThemeAdapter extends BaseQuickAdapter<ArtTopicVo, BaseViewHolder> {
    CommonPictureAdapter homePageThemeAdapter;

    public HomePageThemeAdapter(List<ArtTopicVo> data) {
        super(R.layout.common_picture_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArtTopicVo item) {
        Glide.with(mContext).load(item.getApp_img_file().getUrl()).into((ImageView) helper.getView(R.id.picture));
        homePageThemeAdapter = new CommonPictureAdapter(item.getArts());
        helper.setText(R.id.title_text, item.getTitle());
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        sortLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        RecyclerView recyclerView = helper.getView(R.id.theme_list);
        recyclerView.setLayoutManager(sortLayoutManager);
        recyclerView.setAdapter(homePageThemeAdapter);
        homePageThemeAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, ArtDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ArtDetailActivity.ART_KEY, item.getArts().get(position));
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });
    }
}
