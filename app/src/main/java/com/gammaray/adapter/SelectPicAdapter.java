package com.gammaray.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;

import java.util.List;

public class SelectPicAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private int clickPosition = 10000;

    public void setClickPosition(int position) {
        this.clickPosition = position;
    }

    public SelectPicAdapter(List<String> data) {
        super(R.layout.select_pics_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        if (helper.getAdapterPosition() == clickPosition) {
            helper.setImageResource(R.id.select_status, R.mipmap.icon_selected);
        } else helper.setImageResource(R.id.select_status, R.mipmap.icon_unselected);
    }


}
