package com.gammaray.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.gammaray.R;
import com.gammaray.adapter.MyHomePageAuctionsAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentMyPageAuctionsBinding;
import com.gammaray.entity.AuctionArtVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.art.AuctionArtDetailActivity;
import com.gammaray.widget.ConfirmOrCancelPopwindow;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MyHomePageAuctionFragment extends BaseFragment<FragmentMyPageAuctionsBinding> {

    MyHomePageAuctionsAdapter picturesAdapter;
    List<AuctionArtVo> artVoList;
    ConfirmOrCancelPopwindow confirmOrCancelPopwindow;

    public static BaseFragment newInstance() {
        return new MyHomePageAuctionFragment();
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my_page_auctions;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        picturesAdapter = new MyHomePageAuctionsAdapter(artVoList);
        StaggeredGridLayoutManager sortLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mBinding.pictures.setLayoutManager(sortLayoutManager);
        picturesAdapter.setEmptyView(R.layout.layout_entrust_empty_for_homepage, mBinding.pictures);
        mBinding.pictures.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                sortLayoutManager.invalidateSpanAssignments();
            }
        });
        mBinding.pictures.setAdapter(picturesAdapter);
        picturesAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //去下架
            AuctionArtVo sellingArtVo = null;
            if (artVoList.size() > 0) {
                sellingArtVo = artVoList.get(position);
            }
            if (view.getId() == R.id.cancelAuction) {
                if (sellingArtVo != null) {
                    if (sellingArtVo.isCan_cancel()) {
                        showCancelDepositDialog(sellingArtVo.getId());
                    }
                }
            }
        });

        mBinding.swipeRefresh.setOnRefreshListener(this::queryArts);
        picturesAdapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id", artVoList.get(position).getId());
            startActivity(AuctionArtDetailActivity.class, bundle);

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        queryArts();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (picturesAdapter != null) {
            picturesAdapter.clearAllTimer();
        }
    }


    private void queryArts() {
        if (picturesAdapter != null) {
            picturesAdapter.clearAllTimer();
        }
        if (mBinding.swipeRefresh.isRefreshing()) {
            mBinding.swipeRefresh.setRefreshing(false);
        }
        showLoading("");
        RequestManager.instance().queryMyAuctions(new MinerCallback<BaseResponseVo<List<AuctionArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        artVoList = response.body().getBody();
                        picturesAdapter.setNewData(artVoList);
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private void showCancelDepositDialog(int artId) {
        confirmOrCancelPopwindow = new ConfirmOrCancelPopwindow(requireContext(), new ConfirmOrCancelPopwindow.OnBottomTextviewClickListener() {
            @Override
            public void onCancleClick() {
                confirmOrCancelPopwindow.dismiss();
            }

            @Override
            public void onPerformClick() {
                confirmOrCancelPopwindow.dismiss();
                goToCancelAuction(artId);
            }
        });
        confirmOrCancelPopwindow.setConfirmText("确定");
        confirmOrCancelPopwindow.setCancleText("取消");
        confirmOrCancelPopwindow.setContent(getString(R.string.cancel_auction_hint));
        confirmOrCancelPopwindow.showAtLocation(mBinding.parentLayout, Gravity.CENTER, 0, 0);
    }

    //取消拍卖
    private void goToCancelAuction(int artId) {
        showLoading(R.string.progress_loading);
        RequestManager.instance().cancelAuction(String.valueOf(artId), new MinerCallback<BaseResponseVo<AuctionArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<AuctionArtVo>> call, Response<BaseResponseVo<AuctionArtVo>> response) {
                if (response.isSuccessful()) {
                    queryArts();
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<AuctionArtVo>> call, Response<BaseResponseVo<AuctionArtVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

}