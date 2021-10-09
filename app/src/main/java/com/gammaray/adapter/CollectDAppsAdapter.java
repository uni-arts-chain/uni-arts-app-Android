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
import com.gammaray.entity.DAppFavouriteBean;
import com.gammaray.entity.DAppRecentlyBean;

import java.util.List;

//全部-收藏列表
public class CollectDAppsAdapter extends BaseQuickAdapter<DAppFavouriteBean, BaseViewHolder> {

    private final Context mContext;

    public CollectDAppsAdapter(Context context, @Nullable @org.jetbrains.annotations.Nullable List<DAppFavouriteBean> data) {
        super(R.layout.item_dapp_list_layout, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DAppFavouriteBean item) {
        if (item.getFavoritable() != null) {
            if (!TextUtils.isEmpty(item.getFavoritable().getTitle())) {
                helper.setText(R.id.tv_app_name, item.getFavoritable().getTitle());
            }
            if (!TextUtils.isEmpty(item.getFavoritable().getDesc())) {
                helper.setText(R.id.tv_app_detail, item.getFavoritable().getDesc());
            }
            if (item.getFavoritable().getLogo() != null) {
                if (!TextUtils.isEmpty(item.getFavoritable().getLogo().getUrl())) {
                    Glide.with(mContext).load(item.getFavoritable().getLogo().getUrl())
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into((ImageView) helper.getView(R.id.img_app_icon));
                }
            }
        }
    }
}
