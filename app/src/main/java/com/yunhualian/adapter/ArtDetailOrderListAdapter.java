package com.yunhualian.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.OrderAmountVo;
import com.yunhualian.entity.SellingArtVo;

import java.util.List;

public class ArtDetailOrderListAdapter extends BaseQuickAdapter<OrderAmountVo, BaseViewHolder> {

    public ArtDetailOrderListAdapter(List<OrderAmountVo> data) {
        super(R.layout.art_detail_order_list_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, OrderAmountVo item) {
        helper.setText(R.id.seller, item.getAddress());
        helper.setText(R.id.price, mContext.getString(R.string.text_buy_amount, item.getPrice()));
        helper.setText(R.id.amount, String.valueOf(item.getAmount()).concat("/").concat(String.valueOf(item.getTotal_amount())));
        if (item.isIs_mine()) {
            helper.setText(R.id.action, "下架");
        } else helper.setText(R.id.action, "购买");
    }
}
