package com.gammaray.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.DAppItemBean;

import java.util.List;

//全部-最近列表
public class ChainDAppsAdapter extends BaseQuickAdapter<DAppItemBean, BaseViewHolder> {


    private final Context mContext;

    public ChainDAppsAdapter(Context context, @Nullable @org.jetbrains.annotations.Nullable List<DAppItemBean> data) {
        super(R.layout.item_dapp_list_layout, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DAppItemBean item) {
        if (!TextUtils.isEmpty(item.getTitle())) {
            helper.setText(R.id.tv_app_name, item.getTitle());
        }
        if (!TextUtils.isEmpty(item.getDesc())) {
            helper.setText(R.id.tv_app_detail, item.getDesc());
        }
        if (item.getLogo() != null) {
            if (!TextUtils.isEmpty(item.getLogo().getUrl())) {
                Glide.with(mContext).load(item.getLogo().getUrl())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into((ImageView) helper.getView(R.id.img_app_icon));
            }
        }
    }
}
