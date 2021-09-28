package com.gammaray.ui.activity;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gammaray.R;
import com.gammaray.adapter.DAppsListAdapter;
import com.gammaray.adapter.HotSearchDAppAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.databinding.ActivityDappSearchLayoutBinding;
import com.gammaray.entity.DAppBean;
import com.gammaray.entity.WalletLinkBean;

import java.util.ArrayList;
import java.util.List;

public class DAppSearchActivity extends BaseActivity<ActivityDappSearchLayoutBinding> {

    private HotSearchDAppAdapter mHotSearchAdapter;

    private List<DAppBean> mDApps = new ArrayList<>();

    private DAppsListAdapter mSearchAdapter;

    private List<WalletLinkBean> mResults = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_dapp_search_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        for (int i = 0; i < 10; i++) {
            mDApps.add(new DAppBean(R.mipmap.icon_eth, "ETH"));
        }

        for (int i = 0; i < 10; i++) {
            mResults.add(new WalletLinkBean("BTC", "BitCoin", R.mipmap.icon_eth));
        }

//        mDataBinding.rlHotSearchResult.setVisibility(View.VISIBLE);
        initHotSearchDApp();
        initSearchDApp();

        mDataBinding.cancel.setOnClickListener(view -> finish());
    }

    private void initHotSearchDApp() {
        mHotSearchAdapter = new HotSearchDAppAdapter(mDApps);
        GridLayoutManager layoutManager = new GridLayoutManager(this,5);
        mDataBinding.rvHotSearchResult.setLayoutManager(layoutManager);
        mDataBinding.rvHotSearchResult.setAdapter(mHotSearchAdapter);
    }

    private void initSearchDApp() {
        mSearchAdapter = new DAppsListAdapter(mResults);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mDataBinding.rvSearchResult.setLayoutManager(layoutManager);
        mDataBinding.rvSearchResult.setAdapter(mSearchAdapter);
    }
}
