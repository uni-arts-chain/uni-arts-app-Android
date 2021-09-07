package com.gammaray.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.base.YunApplication;
import com.gammaray.constant.ExtraConstant;
import com.gammaray.entity.ArtTopicVo;
import com.gammaray.entity.EventBusMessageEvent;
import com.gammaray.ui.activity.art.ArtDetailActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class HomePageThemeAdapter extends BaseQuickAdapter<ArtTopicVo, BaseViewHolder> {
    CommonPictureAdapter homePageThemeAdapter;
    Context context;

    public HomePageThemeAdapter(List<ArtTopicVo> data, Context context) {
        super(R.layout.common_picture_item, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, ArtTopicVo item) {
        Glide.with(mContext).load(item.getApp_img_file().getUrl()).transition(withCrossFade()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL).into((ImageView) helper.getView(R.id.picture));
        homePageThemeAdapter = new CommonPictureAdapter(item.getArts(), context);
        TextView title = helper.getView(R.id.title_text);
        title.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        title.setText(item.getTitle());
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        sortLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        RecyclerView recyclerView = helper.getView(R.id.theme_list);
        recyclerView.setLayoutManager(sortLayoutManager);
        recyclerView.setAdapter(homePageThemeAdapter);
        homePageThemeAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == (item.getArts().size() - 1)) {
                EventBus.getDefault().post(new EventBusMessageEvent(ExtraConstant.EVENT_MORE_PICTUR_SELECT, ""));
                return;
            }
            Intent intent = new Intent(mContext, ArtDetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ArtDetailActivity.ART_KEY, item.getArts().get(position));

            bundle.putInt(ArtDetailActivity.ART_ID, item.getArts().get(position).getId());
            intent.putExtras(bundle);
            mContext.startActivity(intent);
        });
    }
}
