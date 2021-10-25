package com.gammaray.adapter;

import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.base.YunApplication;
import com.gammaray.entity.NetworkInfos;
import com.gammaray.utils.SharedPreUtils;
import com.gammaray.widget.BasePopupWindow;

import java.util.List;

public class NetWorkTypeAdapter extends BaseQuickAdapter<NetworkInfos, BaseViewHolder> {

    private List<NetworkInfos> mDatas;

    private BasePopupWindow mNetworkInfoPopWindow;

    private TextView mTitleTv;

    public NetWorkTypeAdapter(TextView mTitle,PopupWindow window, @Nullable @org.jetbrains.annotations.Nullable List<NetworkInfos> data) {
        super(R.layout.item_network_types_layout, data);
        this.mDatas = data;
        this.mNetworkInfoPopWindow = (BasePopupWindow) window;
        this.mTitleTv = mTitle;
    }

    @Override
    protected void convert(BaseViewHolder helper, NetworkInfos item) {
        helper.setText(R.id.tv_network_type, item.getTitle());

        RecyclerView rvNetWork = helper.itemView.findViewById(R.id.rv_networks);
        NetWorkListAdapter adapter = new NetWorkListAdapter(item.getChain_networks());
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvNetWork.setLayoutManager(layoutManager);
        rvNetWork.setAdapter(adapter);
        String defaultNetwork = mTitleTv.getText().toString();
        setDefaultNetWorks(defaultNetwork);
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            resetNetWorks();
            item.getChain_networks().get(position).setChoose(true);
            mTitleTv.setText(item.getChain_networks().get(position).getName());
            YunApplication.NETWORK_RPC_URL = item.getChain_networks().get(position).getRpc_url() + YunApplication.NETWORK_API_KEY;
            YunApplication.NETWORK_CHAIN_ID = item.getChain_networks().get(position).getChain_id();
            SharedPreUtils.setString(mContext,SharedPreUtils.KEY_RPC_NAME,item.getChain_networks().get(position).getName());
            SharedPreUtils.setString(mContext,SharedPreUtils.KEY_RPC_URL,YunApplication.NETWORK_RPC_URL);
            SharedPreUtils.setInteger(mContext,SharedPreUtils.KEY_CHAIN_ID,YunApplication.NETWORK_CHAIN_ID);
            notifyDataSetChanged();
            mNetworkInfoPopWindow.dismiss();
        });
    }

    private void resetNetWorks() {
        if (mDatas != null && mDatas.size() > 0) {
            for (int i = 0; i < mDatas.size(); i++) {
                List<NetworkInfos.ChainNetWork> chainNetWorks = mDatas.get(i).getChain_networks();
                if (chainNetWorks != null && chainNetWorks.size() > 0) {
                    for (int j = 0; j < chainNetWorks.size(); j++) {
                        chainNetWorks.get(j).setChoose(false);
                    }
                }
            }
        }
    }

    private void setDefaultNetWorks(String selectNetWork) {
        if (mDatas != null && mDatas.size() > 0) {
            for (int i = 0; i < mDatas.size(); i++) {
                List<NetworkInfos.ChainNetWork> chainNetWorks = mDatas.get(i).getChain_networks();
                if (chainNetWorks != null && chainNetWorks.size() > 0) {
                    for (int j = 0; j < chainNetWorks.size(); j++) {
                        if(chainNetWorks.get(j).getName().equals(selectNetWork)){
                            chainNetWorks.get(j).setChoose(true);
                        }else{
                            chainNetWorks.get(j).setChoose(false);
                        }
                    }
                }
            }
        }
    }
}
