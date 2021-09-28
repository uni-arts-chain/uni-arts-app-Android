package com.gammaray.ui.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gammaray.R;
import com.gammaray.adapter.RecentDAppsAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentRecentDappLayoutBinding;
import com.gammaray.entity.DAppBean;

import java.util.ArrayList;
import java.util.List;

public class RecentDAppFragment extends BaseFragment<FragmentRecentDappLayoutBinding> {

    private RecentDAppsAdapter mAdapter;

    private List<DAppBean> mDApps = new ArrayList<>();

    private String mType;

    public static BaseFragment newInstance(String type) {
        RecentDAppFragment recentDAppFragment = new RecentDAppFragment();
        Bundle args = new Bundle();
        args.putString("type", type);
        recentDAppFragment.setArguments(args);
        return recentDAppFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_recent_dapp_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

        if (getArguments() != null) {
            mType = getArguments().getString("type");
        }
        for (int i = 0; i < 10; i++) {
            mDApps.add(new DAppBean(R.mipmap.icon_eth, "ETH"));
        }
        mAdapter = new RecentDAppsAdapter(mDApps);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBinding.rvRecentDapp.setLayoutManager(layoutManager);
        mAdapter.setEmptyView(R.layout.dapps_empty_layout,mBinding.rvRecentDapp);
        mBinding.rvRecentDapp.setAdapter(mAdapter);
    }
}
