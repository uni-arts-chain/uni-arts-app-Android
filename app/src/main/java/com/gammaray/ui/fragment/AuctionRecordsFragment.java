package com.gammaray.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gammaray.R;
import com.gammaray.adapter.AuctionRecordsAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentAuctionRecordLayoutBinding;
import com.gammaray.entity.AuctionArtVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.art.AuctionArtDetailActivity;
import com.gammaray.ui.activity.user.CreateAuctionOrderActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AuctionRecordsFragment extends BaseFragment<FragmentAuctionRecordLayoutBinding> {

    private int page = 1;
    private AuctionRecordsAdapter mAdapter;
    private List<AuctionArtVo> mList = new ArrayList<>();
    private String mState;

    public AuctionRecordsFragment(String state) {
        this.mState = state;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_auction_record_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        initRecyclerView();
        initRefresh();
    }

    private void initRecyclerView() {
        mAdapter = new AuctionRecordsAdapter(requireContext(), mList, mState);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        mBinding.rvAuctions.setLayoutManager(layoutManager);
        mAdapter.setEmptyView(R.layout.layout_entrust_empty, mBinding.rvAuctions);
        mBinding.rvAuctions.setAdapter(mAdapter);
        if (mState.equals("attend")) {
            mAdapter.setOnLoadMoreListener(this::queryAttendAuctions, mBinding.rvAuctions);
        } else if (mState.equals("bid")) {
            mAdapter.setOnLoadMoreListener(this::queryBidAuctions, mBinding.rvAuctions);
        } else if (mState.equals("win")) {
            mAdapter.setOnLoadMoreListener(this::queryWinAuctions, mBinding.rvAuctions);
        } else if (mState.equals("finish")) {
            mAdapter.setOnLoadMoreListener(this::queryFinishedAuctions, mBinding.rvAuctions);
        }
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mList.size() > 0) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(AuctionArtDetailActivity.ART_KEY, mList.get(position));
                bundle.putInt(AuctionArtDetailActivity.ART_ID, mList.get(position).getId());
                startActivity(AuctionArtDetailActivity.class, bundle);
            }
        });

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.tv_to_pay) {
                TextView textView = (TextView) view;
                if (textView.getText().toString().contains("去支付")) {
                    Intent intent = new Intent(requireActivity(), CreateAuctionOrderActivity.class);
                    intent.putExtra("id", String.valueOf(mList.get(position).getId()));
                    startActivity(intent);
                }
            }
        });
    }

    private void initRefresh() {
        mBinding.srlLayout.setOnRefreshListener(() -> {
            if (mAdapter != null) {
                mAdapter.clearAllTimer();
            }
            page = 1;
            if (mState.equals("attend")) {
                queryAttendAuctions();
            } else if (mState.equals("bid")) {
                queryBidAuctions();
            } else if (mState.equals("win")) {
                queryWinAuctions();
            } else if (mState.equals("finish")) {
                queryFinishedAuctions();
            }
            mBinding.srlLayout.setRefreshing(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mState.equals("attend")) {
            queryAttendAuctions();
        } else if (mState.equals("bid")) {
            queryBidAuctions();
        } else if (mState.equals("win")) {
            queryWinAuctions();
        } else if (mState.equals("finish")) {
            queryFinishedAuctions();
        }
    }

    private void queryAttendAuctions() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryAttendAuctions(page, 20, new MinerCallback<BaseResponseVo<List<AuctionArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<AuctionArtVo> list = response.body().getBody();
                        if (list != null && list.size() > 0) {
                            if (page == 1) {
                                mList.clear();
                                mList = list;
                            } else {
                                mList.addAll(list);
                            }
                            mAdapter.setNewData(mList);
                            page++;
                        } else {
                            if (page > 1) {
                                mAdapter.loadMoreEnd();
                            }
                        }
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

    private void queryBidAuctions() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryBidAuctions(page, 20, new MinerCallback<BaseResponseVo<List<AuctionArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<AuctionArtVo> list = response.body().getBody();
                        if (list != null && list.size() > 0) {
                            if (page == 1) {
                                mList.clear();
                                mList = list;
                            } else {
                                mList.addAll(list);
                            }
                            mAdapter.setNewData(mList);
                            page++;
                        } else {
                            if (page > 1) {
                                mAdapter.loadMoreEnd();
                            }
                        }
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

    private void queryWinAuctions() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryWinAuctions(page, 20, new MinerCallback<BaseResponseVo<List<AuctionArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<AuctionArtVo> list = response.body().getBody();
                        if (list != null && list.size() > 0) {
                            if (page == 1) {
                                mList.clear();
                                mList = list;
                            } else {
                                mList.addAll(list);
                            }
                            mAdapter.setNewData(mList);
                            page++;
                        } else {
                            if (page > 1) {
                                mAdapter.loadMoreEnd();
                            }
                        }
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

    private void queryFinishedAuctions() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryFinishAuctions(page, 20, new MinerCallback<BaseResponseVo<List<AuctionArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<AuctionArtVo> list = response.body().getBody();
                        if (list != null && list.size() > 0) {
                            if (page == 1) {
                                mList.clear();
                                mList = list;
                            } else {
                                mList.addAll(list);
                            }
                            mAdapter.setNewData(mList);
                            page++;
                        } else {
                            if (page > 1) {
                                mAdapter.loadMoreEnd();
                            }
                        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAdapter != null) {
            mAdapter.clearAllTimer();
        }
    }
}
