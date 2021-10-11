package com.gammaray.adapter;

import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.DAppGroupBean;
import com.gammaray.ui.activity.DAppWebsActivity;
import com.gammaray.utils.DisplayUtils;
import com.gammaray.utils.ToastManager;
import com.gammaray.widget.pager.PagerGridLayoutManager;
import com.gammaray.widget.pager.PagerGridSnapHelper;

import java.util.List;

public class DAppGroupsAdapter extends BaseQuickAdapter<DAppGroupBean, BaseViewHolder> {

    private Context mContext;

    public DAppGroupsAdapter(List<DAppGroupBean> data, Context context) {
        super(R.layout.item_dapps_group_layout, data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, DAppGroupBean item) {

        helper.setText(R.id.tv_dapp_list_name, item.getTitle());
        helper.setText(R.id.tv_dapp_list_all, "全部");
        helper.addOnClickListener(R.id.tv_dapp_list_all);

        RecyclerView dappRV = helper.itemView.findViewById(R.id.rv_dapp_list);
        int count = item.getDapps().size();
        int itemHeight = 0;
        int itemRows = 1;
        if (count > 0) {
            if (count == 1) {
                itemHeight = 80;
                itemRows = 1;
            } else if (count == 2) {
                itemHeight = 160;
                itemRows = 2;
            } else {
                itemHeight = 240;
                itemRows = 3;
            }
        } else {
            helper.setVisible(R.id.rl_title, false);
        }

        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(itemRows, 1, PagerGridLayoutManager.HORIZONTAL);
        dappRV.setLayoutManager(layoutManager);

        int screenWidth = ScreenUtils.getScreenWidth() / 12 * 11;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, DisplayUtils.dp2px(mContext, itemHeight));
        params.addRule(RelativeLayout.BELOW, R.id.rl_title);
        dappRV.setLayoutParams(params);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(dappRV);

        DAppsListAdapter mAdapter = new DAppsListAdapter(item.getDapps(), mContext);
        dappRV.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(mContext, DAppWebsActivity.class);
            intent.putExtra("dapp_title",item.getDapps().get(position).getTitle());
            mContext.startActivity(intent);
        });
    }
}
