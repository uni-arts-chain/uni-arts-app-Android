package com.gammaray.ui.fragment;

import android.content.Intent;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.gammaray.R;
import com.gammaray.adapter.PicturesAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentSearchResultLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.SellingArtVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.art.ArtDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SearchResultFragment extends BaseFragment<FragmentSearchResultLayoutBinding> {

    private List<SellingArtVo> mArtList = new ArrayList<>();
    private PicturesAdapter mAdapter;
    private String mKeyWord;

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
        mBinding.rvSearchResult.setLayoutManager(layoutManager);
        mAdapter.setEmptyView(R.layout.layout_entrust_empty, mBinding.rvSearchResult);
        mBinding.rvSearchResult.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mArtList != null && mArtList.size() > 0) {
                Intent intent = new Intent(requireContext(), ArtDetailActivity.class);
                intent.putExtra(ArtDetailActivity.ART_ID, mArtList.get(position).getId());
                startActivity(intent);
            }
        });

        mBinding.srlSearch.setOnRefreshListener(() -> {
            searchKeyWords(mKeyWord);
            mBinding.srlSearch.setRefreshing(false);
        });

    }

    public void searchKeyWords(String keyWords) {
        mKeyWord = keyWords;
        if (mAdapter == null) {
            mAdapter = new PicturesAdapter(mArtList);
        }
        mArtList.clear();
        searchSellProduct(mKeyWord);
    }

    private void searchSellProduct(String keyWords) {
        showLoading(R.string.progress_loading);
        HashMap<String, String> params = new HashMap<>();
        params.put("q", keyWords);
        params.put("page", "1");
        params.put("per_page", "100");
        RequestManager.instance().searchArts(params, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    {
                        if (response.body() != null) {
                            mArtList = response.body().getBody();
                            mAdapter.setNewData(mArtList);
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}
