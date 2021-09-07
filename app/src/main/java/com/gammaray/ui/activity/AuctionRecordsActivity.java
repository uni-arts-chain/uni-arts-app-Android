package com.gammaray.ui.activity;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.gammaray.R;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityAuctionRecordLayoutBinding;
import com.gammaray.ui.fragment.AuctionRecordsFragment;

import java.util.Arrays;

public class AuctionRecordsActivity extends BaseActivity<ActivityAuctionRecordLayoutBinding> implements MyHomePageAdapter.TabPagerListener {

    private MyHomePageAdapter pageAdapter;
    private int index;
    private AuctionRecordsFragment attendAuctionRecordFragment, bidAuctionRecordFragment, winAuctionRecordFragment, finishAuctionRecordFragment;

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
        attendAuctionRecordFragment = new AuctionRecordsFragment("attend");
        bidAuctionRecordFragment = new AuctionRecordsFragment("bid");
        winAuctionRecordFragment = new AuctionRecordsFragment("win");
        finishAuctionRecordFragment = new AuctionRecordsFragment("finish");
        initViewPager();
    }

    private void initViewPager() {
        pageAdapter = new MyHomePageAdapter(getSupportFragmentManager(), 4, Arrays.asList(getResources().getStringArray(R.array.auctions_tabs)), this);
        pageAdapter.setListener(this);
        mDataBinding.vpRecords.setAdapter(pageAdapter);
        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.vpRecords);
        mDataBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mDataBinding.vpRecords.setOffscreenPageLimit(4);
        mDataBinding.vpRecords.setCurrentItem(index);
    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0) {
            return attendAuctionRecordFragment;
        } else if (position == 1) {
            return bidAuctionRecordFragment;
        } else if (position == 2) {
            return winAuctionRecordFragment;
        } else {
            return finishAuctionRecordFragment;
        }
    }
}
