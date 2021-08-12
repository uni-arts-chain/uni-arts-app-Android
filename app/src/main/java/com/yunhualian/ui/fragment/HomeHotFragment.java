package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunhualian.R;
import com.yunhualian.adapter.HomePagePopularAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentHomeHotLayoutBinding;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.ui.activity.art.ArtDetailActivity;

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
