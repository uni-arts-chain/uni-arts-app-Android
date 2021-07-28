package com.yunhualian.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.PicturesAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentSearchResultLayoutBinding;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.ui.activity.art.ArtDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchResultFragment extends BaseFragment<FragmentSearchResultLayoutBinding> {

    private List<SellingArtVo> mArtList;
    private PicturesAdapter mAdapter;

    public SearchResultFragment(List<SellingArtVo> artList) {
        this.mArtList = artList;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_search_result_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new PicturesAdapter(mArtList);
        mAdapter.setEmptyView(R.layout.layout_entrust_empty, mBinding.rvSearchResult);
        mBinding.rvSearchResult.setLayoutManager(layoutManager);
        mBinding.rvSearchResult.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mArtList != null && mArtList.size() > 0) {
                Intent intent = new Intent(requireContext(), ArtDetailActivity.class);
                intent.putExtra(ArtDetailActivity.ART_ID, mArtList.get(position).getId());
                startActivity(intent);
            }
        });
    }

    public void updateData(List<SellingArtVo> artList){
        mAdapter.setNewData(artList);
    }
}
