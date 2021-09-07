package com.gammaray.adapter;


import android.content.Intent;
import android.widget.ImageView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.base.YunApplication;
import com.gammaray.entity.ArtistListVo;
import com.gammaray.ui.activity.art.ArtDetailActivity;

import java.math.BigDecimal;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CreatorParentAdapter extends BaseQuickAdapter<ArtistListVo, BaseViewHolder> {
    CreatorChildPictureAdapter homePageThemeAdapter;

    public CreatorParentAdapter(List<ArtistListVo> data) {
        super(R.layout.fragment_creator_parent_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArtistListVo item) {
        Glide.with(mContext)
                .load(item.getMember().getAvatar().getUrl()).dontAnimate()
                .apply(new RequestOptions().placeholder(R.mipmap.icon_default_head))
                .into((ImageView) helper.getView(R.id.picture));
        helper.setText(R.id.name, item.getMember().getDisplay_name());
        helper.setText(R.id.profile, item.getMember().getDesc());
        helper.setText(R.id.artist_amount, String.valueOf(item.getMember().getArt_size()));
        homePageThemeAdapter = new CreatorChildPictureAdapter(item.getArts());
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        sortLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        RecyclerView recyclerView = helper.getView(R.id.picture_list);
        recyclerView.setLayoutManager(sortLayoutManager);
        recyclerView.setAdapter(homePageThemeAdapter);
        helper.addOnClickListener(R.id.headLayout);
        homePageThemeAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (item.getArts().size() > BigDecimal.ZERO.intValue()) {
                Intent intent = new Intent(mContext, ArtDetailActivity.class);
                intent.putExtra(ArtDetailActivity.ART_ID, item.getArts().get(position).getId());
                mContext.startActivity(intent);

            }
        });
    }
}
