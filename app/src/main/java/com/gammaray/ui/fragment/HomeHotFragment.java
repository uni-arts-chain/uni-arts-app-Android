package com.gammaray.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gammaray.R;
import com.gammaray.adapter.HomePagePopularAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentHomeHotLayoutBinding;
import com.gammaray.entity.SellingArtVo;
import com.gammaray.ui.activity.art.ArtDetailActivity;

import java.util.List;

public class HomeHotFragment extends BaseFragment<FragmentHomeHotLayoutBinding> implements BaseQuickAdapter.OnItemClickListener{


    private List<SellingArtVo> mList;
    private HomePagePopularAdapter popularAdapter;

    public HomeHotFragment(List<SellingArtVo> list){
        this.mList = list;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home_hot_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

        popularAdapter = new HomePagePopularAdapter(mList);
        StaggeredGridLayoutManager hotManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mBinding.rvHomeHot.setLayoutManager(hotManager);
        hotManager.invalidateSpanAssignments();
        popularAdapter.setEmptyView(R.layout.layout_entrust_empty,mBinding.rvHomeHot);
        mBinding.rvHomeHot.setAdapter(popularAdapter);
        popularAdapter.setOnItemClickListener(this);
    }

    public void updateData(List<SellingArtVo> list){
        mList = list;
        popularAdapter.setNewData(mList);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ArtDetailActivity.ART_KEY, mList.get(position));
        bundle.putInt(ArtDetailActivity.ART_ID, mList.get(position).getId());
        startActivity(ArtDetailActivity.class, bundle);
    }
}
