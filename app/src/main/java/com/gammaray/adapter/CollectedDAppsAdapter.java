package com.gammaray.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.DAppFavouriteBean;

import java.util.List;

//发现页面-收藏列表
public class CollectedDAppsAdapter extends BaseQuickAdapter<DAppFavouriteBean, BaseViewHolder> {

    private final Context mContext;

    public CollectedDAppsAdapter(Context context, List<DAppFavouriteBean> data) {
        super(R.layout.item_recent_dapp_layout, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DAppFavouriteBean item) {
        if (item.getFavoritable() != null) {
            if (!TextUtils.isEmpty(item.getFavoritable().getTitle())) {
                helper.setText(R.id.tv_app_name, item.getFavoritable().getTitle());
            }
            if (item.getFavoritable().getLogo() != null) {
                if (!TextUtils.isEmpty(item.getFavoritable().getLogo().getUrl())) {
                    Glide.with(mContext).load(item.getFavoritable().getLogo().getUrl())
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into((de.hdodenhof.circleimageview.CircleImageView) helper.getView(R.id.img_app_icon));
                }
            }
        }
    }
}
