package com.gammaray.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.NetworkInfos;

import java.util.List;

public class NetWorkListAdapter extends BaseQuickAdapter<NetworkInfos.ChainNetWork, BaseViewHolder> {


    public NetWorkListAdapter(@Nullable @org.jetbrains.annotations.Nullable List<NetworkInfos.ChainNetWork> data) {
        super(R.layout.item_network_infos_layout,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NetworkInfos.ChainNetWork item) {
        helper.setText(R.id.tv_network_name,item.getName());
        if(item.isChoose()){
            helper.setVisible(R.id.img_choose_network,true);
        }else{
            helper.setVisible(R.id.img_choose_network,false);
        }
    }
}
