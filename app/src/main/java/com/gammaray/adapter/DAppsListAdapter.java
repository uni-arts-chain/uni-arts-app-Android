package com.gammaray.adapter;

import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.DAppGroupBean;
import com.gammaray.entity.WalletLinkBean;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DAppsListAdapter extends BaseQuickAdapter<DAppGroupBean.DApps, BaseViewHolder> {


    private final Context mContext;

    public DAppsListAdapter(@Nullable @org.jetbrains.annotations.Nullable List<DAppGroupBean.DApps> data, Context context) {
        super(R.layout.item_dapp_list_layout, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DAppGroupBean.DApps item) {
        helper.setText(R.id.tv_app_name, item.getTitle());
        helper.setText(R.id.tv_app_detail, item.getDesc());
        Glide.with(mContext).load(item.getLogo().getUrl())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into((CircleImageView) helper.getView(R.id.img_app_icon));
    }
}
