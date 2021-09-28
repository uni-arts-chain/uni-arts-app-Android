package com.gammaray.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.WalletLinkBean;

import java.util.List;

public class DAppsListAdapter extends BaseQuickAdapter<WalletLinkBean, BaseViewHolder> {


    public DAppsListAdapter(@Nullable @org.jetbrains.annotations.Nullable List<WalletLinkBean> data) {
        super(R.layout.item_dapp_list_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletLinkBean item) {
        helper.setBackgroundRes(R.id.img_app_icon, item.getImgId());
        helper.setText(R.id.tv_app_name, item.getName());
        helper.setText(R.id.tv_app_detail, item.getShortName());
    }
}
