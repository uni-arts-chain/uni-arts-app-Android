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
import com.yunhualian.ui.activity.art.AuctionArtDetailActivity;

import java.util.List;

public class HomeAuctionFragment extends BaseFragment<FragmentHomeAuctionLayoutBinding> implements BaseQuickAdapter.OnItemClickListener {

    private List<AuctionArtVo> mList;

    private AuctionPicturesAdapter mAdapter;

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
        mAdapter = new AuctionPicturesAdapter(mList);
        StaggeredGridLayoutManager auctionManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mBinding.rvHomeAuction.setLayoutManager(auctionManager);
        auctionManager.invalidateSpanAssignments();
        mAdapter.setEmptyView(R.layout.layout_entrust_empty,mBinding.rvHomeAuction);
        mBinding.rvHomeAuction.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    public void updateData(List<AuctionArtVo> list) {
        mAdapter.clearAllTimer();
        mList = list;
        mAdapter.setNewData(mList);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AuctionArtDetailActivity.ART_KEY, mList.get(position));
        bundle.putInt(AuctionArtDetailActivity.ART_ID, mList.get(position).getId());
        startActivity(AuctionArtDetailActivity.class, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAdapter != null){
            mAdapter.clearAllTimer();
        }
    }
}
