package com.yunhualian.ui.fragment;

import android.content.Intent;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.AuctionPicturesAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentSearchResultLayoutBinding;
import com.yunhualian.entity.AuctionArtVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.art.ArtDetailActivity;
import com.yunhualian.ui.activity.art.AuctionArtDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SearchAuctionsFragment extends BaseFragment<FragmentSearchResultLayoutBinding> {

    private List<AuctionArtVo> mAuctionList = new ArrayList<>();
    private AuctionPicturesAdapter mAdapter;
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
        mAdapter = new AuctionPicturesAdapter(mAuctionList);
        mBinding.rvSearchResult.setLayoutManager(layoutManager);
        mAdapter.setEmptyView(R.layout.layout_entrust_empty, mBinding.rvSearchResult);
        mBinding.rvSearchResult.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mAuctionList != null && mAuctionList.size() > 0) {
                Intent intent = new Intent(requireContext(), AuctionArtDetailActivity.class);
                intent.putExtra(ArtDetailActivity.ART_ID, mAuctionList.get(position).getId());
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
            mAdapter = new AuctionPicturesAdapter(mAuctionList);
        }
        mAuctionList.clear();
        searchAuctionProduct(keyWords);
    }

    private void searchAuctionProduct(String keyWords) {
        showLoading(R.string.progress_loading);
        HashMap<String, String> params = new HashMap<>();
        params.put("q", keyWords);
        params.put("page", "1");
        params.put("per_page", "100");
        RequestManager.instance().searchAuctions(params, new MinerCallback<BaseResponseVo<List<AuctionArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        mAuctionList = response.body().getBody();
                        mAdapter.setNewData(mAuctionList);
                    }
                }

            }

            @Override
            public void onError(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}
