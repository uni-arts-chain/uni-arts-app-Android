package com.yunhualian.adapter;


import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.entity.BoughtArtVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.utils.DateUtil;

import java.util.List;

public class SellAndBuyAdapter extends BaseQuickAdapter<BoughtArtVo, BaseViewHolder> {

    public SellAndBuyAdapter(List<BoughtArtVo> data) {
        super(R.layout.activity_sell_and_buy_tiem, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BoughtArtVo item) {
        helper.setText(R.id.order_time, DateUtil.dateToStringWith(item.getFinished_at() * 1000));
        helper.setText(R.id.order_cost, item.getPrice());
        helper.setText(R.id.name, item.getArt().getName());
        helper.setText(R.id.order_no, item.getSn());
        if (TextUtils.isEmpty(item.getArt().getAuthor().getDisplay_name()))
            helper.setText(R.id.art_name, "未设置昵称");
        else
            helper.setText(R.id.art_name, item.getArt().getAuthor().getDisplay_name());
        helper.setText(R.id.addr, mContext.getString(R.string.nft_address, item.getArt().getItem_hash()));
        Glide.with(mContext).load(item.getArt().getImg_main_file1().getUrl()) //图片地址
                .into((ImageView) helper.getView(R.id.hot_picture));
    }
}
