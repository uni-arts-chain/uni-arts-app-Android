package com.gammaray.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ScreenUtils;
import com.gammaray.R;
import com.gammaray.adapter.DAppsListAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentDappListLayoutBinding;
import com.gammaray.entity.WalletLinkBean;
import com.gammaray.ui.activity.DAppsListActivity;
import com.gammaray.utils.DisplayUtils;
import com.gammaray.widget.pager.PagerGridLayoutManager;
import com.gammaray.widget.pager.PagerGridSnapHelper;

import java.util.ArrayList;
import java.util.List;

public class DAppListFragment extends BaseFragment<FragmentDappListLayoutBinding> {

    private DAppsListAdapter mAdapter;

    private List<WalletLinkBean> mList = new ArrayList<>();

    private String mCoinType;


    static BaseFragment newInstance(String coinType) {
        DAppListFragment dAppListFragment = new DAppListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("coin_type", coinType);
        dAppListFragment.setArguments(bundle);
        return dAppListFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_dapp_list_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

        if (getArguments() != null) {
            mCoinType = getArguments().getString("coin_type");
        }
        for (int i = 0; i < 10; i++) {
            mList.add(new WalletLinkBean("ETH", "ETHETHETHETH", R.mipmap.icon_eth));
        }

        initRecommendDApp();
        initTransferDApp();
    }

    private void initRecommendDApp() {
        // 1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(3, 1, PagerGridLayoutManager.HORIZONTAL);
        mBinding.rvRecommend.setLayoutManager(layoutManager);

        int screenWidth = ScreenUtils.getScreenWidth() / 12 * 11;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, DisplayUtils.dp2px(requireContext(), 240));
        params.addRule(RelativeLayout.BELOW, R.id.rl_recommend_title);
        mBinding.rvRecommend.setLayoutParams(params);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mBinding.rvRecommend);

        mAdapter = new DAppsListAdapter(mList);
        mBinding.rvRecommend.setAdapter(mAdapter);

        mBinding.tvRecommendAll.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), DAppsListActivity.class);
            intent.putExtra("title", "推荐");
            if (mCoinType.equals("ETH")) {
                intent.putExtra("type", 2);
                startActivity(intent);
            } else if (mCoinType.equals("UART")) {
                intent.putExtra("type", 4);
                startActivity(intent);
            }
        });
    }

    private void initTransferDApp() {
        // 1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(3, 1, PagerGridLayoutManager.HORIZONTAL);
        mBinding.rvTransfer.setLayoutManager(layoutManager);

        int screenWidth = ScreenUtils.getScreenWidth() / 12 * 11;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenWidth, DisplayUtils.dp2px(requireContext(), 240));
        params.addRule(RelativeLayout.BELOW, R.id.rl_transfer_title);
        mBinding.rvTransfer.setLayoutParams(params);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(mBinding.rvTransfer);

        mAdapter = new DAppsListAdapter(mList);
        mBinding.rvTransfer.setAdapter(mAdapter);

        mBinding.tvTransferAll.setOnClickListener(view -> {
            Intent intent = new Intent(requireActivity(), DAppsListActivity.class);
            intent.putExtra("title", "交易");
            if (mCoinType.equals("ETH")) {
                intent.putExtra("type", 3);
            } else if (mCoinType.equals("UART")) {
                intent.putExtra("type", 5);
            }
            startActivity(intent);
        });
    }

}
