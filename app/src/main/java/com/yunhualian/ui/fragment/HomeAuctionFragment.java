package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunhualian.R;
import com.yunhualian.adapter.AuctionPicturesAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentHomeAuctionLayoutBinding;
import com.yunhualian.entity.AuctionArtVo;
import com.yunhualian.ui.activity.art.ArtDetailActivity;

import java.util.List;

public class HomeAuctionFragment extends BaseFragment<FragmentHomeAuctionLayoutBinding> implements BaseQuickAdapter.OnItemClickListener {

    private List<AuctionArtVo> mList;

    private AuctionPicturesAdapter mAdater;

    public HomeAuctionFragment(List<AuctionArtVo> list) {
        this.mList = list;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home_auction_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        mAdater = new AuctionPicturesAdapter(mList);
        StaggeredGridLayoutManager auctionManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mBinding.rvHomeAuction.setLayoutManager(auctionManager);
        auctionManager.invalidateSpanAssignments();
        mBinding.rvHomeAuction.setAdapter(mAdater);
        mAdater.setOnItemClickListener(this);
    }

    public void updateData(List<AuctionArtVo> list) {
        mList = list;
        mAdater.setNewData(mList);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ArtDetailActivity.ART_KEY, mList.get(position));
        bundle.putInt(ArtDetailActivity.ART_ID, mList.get(position).getId());
        startActivity(ArtDetailActivity.class, bundle);
    }
}
