package com.gammaray.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.DAppRecentlyBean;

import java.util.List;

//发现页面-最近列表
public class RecentlyDAppsAdapter extends BaseQuickAdapter<DAppRecentlyBean, BaseViewHolder> {

    private final Context mContext;

    public RecentlyDAppsAdapter(Context context, List<DAppRecentlyBean> data) {
        super(R.layout.item_recent_dapp_layout, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DAppRecentlyBean item) {
        if (item.getDapp() != null) {
            if (!TextUtils.isEmpty(item.getDapp().getTitle())) {
                helper.setText(R.id.tv_app_name, item.getDapp().getTitle());
            }
            if (item.getDapp().getLogo() != null) {
                if (!TextUtils.isEmpty(item.getDapp().getLogo().getUrl())) {
                    Glide.with(mContext).load(item.getDapp().getLogo().getUrl())
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into((ImageView) helper.getView(R.id.img_app_icon));
                }
            }
        }
    }
}
