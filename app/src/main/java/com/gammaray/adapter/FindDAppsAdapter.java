package com.gammaray.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.DAppBean;

import java.util.List;

public class FindDAppsAdapter extends BaseQuickAdapter<DAppBean, BaseViewHolder> {

    public FindDAppsAdapter(List<DAppBean> data) {
        super(R.layout.item_recent_dapp_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DAppBean item) {
        helper.setBackgroundRes(R.id.img_app_icon, item.getAppIcon());
        helper.setText(R.id.tv_app_name, item.getAppName());
    }
}
