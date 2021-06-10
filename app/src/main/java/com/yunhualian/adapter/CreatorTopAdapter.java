package com.yunhualian.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.entity.ArtistVo;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CreatorTopAdapter extends BaseQuickAdapter<ArtistVo, BaseViewHolder> {

    public CreatorTopAdapter(List<ArtistVo> data) {
        super(R.layout.fragment_creator_top_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ArtistVo item) {
        Glide.with(mContext).load(item.getRecommend_image().getUrl()).transition(withCrossFade()).into((ImageView) helper.getView(R.id.artist_pic));
        helper.setText(R.id.artist_name, item.getDisplay_name());
//        helper.setVisible(R.id.platform_push, helper.getPosition() == 0);
    }
}
