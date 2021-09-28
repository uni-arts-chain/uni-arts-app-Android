package com.gammaray.ui.activity;

import android.util.Log;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityDappsListLayoutBinding;

public class DAppsListActivity extends BaseActivity<ActivityDappsListLayoutBinding> {

    private String mTitle;

    private int mType;//0：收藏，1：最近，2：ETH-推荐，3：ETH-交易，4：UART-推荐，5：UART-交易

    @Override
    public int getLayoutId() {
        return R.layout.activity_dapps_list_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra("title");
            mType = getIntent().getIntExtra("type", 0);
            Log.e("Type","cur type--" + mType);
            ToolBarOptions toolBarOptions = new ToolBarOptions();
            toolBarOptions.titleString = mTitle;
            setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);
        }

        mDataBinding.srlApps.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
    }
}
