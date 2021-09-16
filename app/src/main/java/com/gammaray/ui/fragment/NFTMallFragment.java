package com.gammaray.ui.fragment;

import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.gammaray.R;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentNftMallLayoutBinding;
import com.gammaray.ui.activity.SearchActivity;

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
        mAdapter = new MyHomePageAdapter(getParentFragmentManager(), 3, Arrays.asList(getResources().getStringArray(R.array.mall_tabs)), requireContext());
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
        else if(position == 1){
            return AuctionSortFragment.newInstance();
        }else{
            return BlindBoxFragment.newInstance();
        }
    }
}
