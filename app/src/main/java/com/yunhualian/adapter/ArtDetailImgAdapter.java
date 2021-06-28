package com.yunhualian.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;

import java.util.List;

/**
 * @Date:2021/6/15
 * @Author:Created by peter_ben
 */
public class ArtDetailImgAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private List<String> imgUrls;
    private Context mContext;

    public ArtDetailImgAdapter(List<String> data, Context context) {
        super(R.layout.item_art_detail_layout, data);
        this.mContext = context;
        this.imgUrls = data;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        Glide.with(mContext).load(item)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((ImageView) helper.getView(R.id.art_detail_img));
    }
}
