package com.yunhualian.ui.activity;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.yunhualian.R;
import com.yunhualian.adapter.MyHomePageAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityAuctionRecordLayoutBinding;
import com.yunhualian.ui.fragment.AuctionRecordsFragment;

import java.util.Arrays;

public class AuctionRecordsActivity extends BaseActivity<ActivityAuctionRecordLayoutBinding> implements MyHomePageAdapter.TabPagerListener {

    private MyHomePageAdapter pageAdapter;
    private int index;

    @Override
    public int getLayoutId() {
        return R.layout.activity_auction_record_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleString = "拍卖记录";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        if (getIntent() != null) {
            index = getIntent().getIntExtra("page_index", 0);
        }
        initViewPager();
    }

    private void initViewPager() {
        pageAdapter = new MyHomePageAdapter(getSupportFragmentManager(), 4, Arrays.asList(getResources().getStringArray(R.array.auctions_tabs)), this);
        pageAdapter.setListener(this);
        mDataBinding.vpRecords.setAdapter(pageAdapter);
        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.vpRecords);
        mDataBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mDataBinding.vpRecords.setCurrentItem(index);
    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0) {
            return AuctionRecordsFragment.newInstance("attend");
        } else if (position == 1) {
            return AuctionRecordsFragment.newInstance("bid");
        } else if (position == 2) {
            return AuctionRecordsFragment.newInstance("win");
        } else {
            return AuctionRecordsFragment.newInstance("finish");
        }
    }
}
