package com.gammaray.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.OfferPriceBean;
import com.gammaray.utils.DateUtil;

import java.util.List;

public class OfferPriceListAdapter extends BaseQuickAdapter<OfferPriceBean, BaseViewHolder> {

    private List<OfferPriceBean> mData;

    private Context mContext;

    public OfferPriceListAdapter(Context context,List<OfferPriceBean> data) {
        super(R.layout.item_offer_price_layout, data);
        this.mData = data;
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, OfferPriceBean item) {
        helper.setText(R.id.tv_address, item.getMember().getAddress());
        helper.setText(R.id.tv_date, DateUtil.dateToStringWithAllDot(item.getCreated_at() * 1000));
        if(helper.getAdapterPosition() == 0){
            helper.setTextColor(R.id.tv_price,mContext.getColor(R.color._D70000));
            helper.setText(R.id.tv_price, "领先¥" + item.getPrice());
        }else{
            helper.setTextColor(R.id.tv_price,mContext.getColor(R.color._101010));
            helper.setText(R.id.tv_price, "出局¥" + item.getPrice());
        }

    }

//    @Override
//    public int getItemCount() {
//        return 3;
//    }
}
