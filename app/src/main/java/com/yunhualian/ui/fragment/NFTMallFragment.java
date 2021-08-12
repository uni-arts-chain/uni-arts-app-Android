package com.yunhualian.ui.fragment;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.yunhualian.R;
import com.yunhualian.adapter.MyHomePageAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentNftMallLayoutBinding;
import com.yunhualian.ui.activity.SearchActivity;

import java.util.Arrays;

public class NFTMallFragment extends BaseFragment<FragmentNftMallLayoutBinding> implements MyHomePageAdapter.TabPagerListener {

    private MyHomePageAdapter mAdapter;

    public static BaseFragment newInstance() {
        return new NFTMallFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_nft_mall_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mAdapter = new MyHomePageAdapter(getParentFragmentManager(), 2, Arrays.asList(getResources().getStringArray(R.array.mall_tabs)), requireContext());
        mAdapter.setListener(this);

        mBinding.viewpager.setAdapter(mAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewpager);
        mBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        mBinding.imgSearch.setOnClickListener(v -> startActivity(SearchActivity.class));
    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0)
            return PictureSortFragment.newInstance();
        else
            return AuctionSortFragment.newInstance();
    }
}
