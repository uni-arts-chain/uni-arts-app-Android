package com.gammaray.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.SellingArtVo;
import com.gammaray.entity.WalletTokenBean;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class WalletNFTsAdapter extends BaseQuickAdapter<SellingArtVo, BaseViewHolder> {

    List<SellingArtVo> list;

    public WalletNFTsAdapter(List<SellingArtVo> list) {
        super(R.layout.item_wallet_nft_layout, list);
        this.list = list;
    }

    @Override
    protected void convert(BaseViewHolder helper, SellingArtVo item) {
        Glide.with(mContext).
                load(item.getImg_main_file1().getUrl()).
                skipMemoryCache(true).
                diskCacheStrategy(DiskCacheStrategy.ALL).
                transition(withCrossFade()).
                into((ImageView) helper.getView(R.id.img_links_icon));

        helper.setText(R.id.tv_links_name, item.getName());
        helper.setText(R.id.tv_remain, String.valueOf(item.getHas_amount()));
    }
}
