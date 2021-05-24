package com.yunhualian.adapter;



import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.entity.OrderAmountVo;

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
        helper.addOnClickListener(R.id.action);
    }
}
