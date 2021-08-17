package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.AuctionRecordsAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentAuctionRecordLayoutBinding;
import com.yunhualian.entity.AuctionArtVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class AuctionRecordsFragment extends BaseFragment<FragmentAuctionRecordLayoutBinding> {

    private int page = 1;
    private AuctionRecordsAdapter mAdapter;
    private List<AuctionArtVo> mList = new ArrayList<>();
    private String mState;

    public static BaseFragment newInstance(String state) {
        AuctionRecordsFragment fragment = new AuctionRecordsFragment();
        Bundle args = new Bundle();
        args.putString("state", state);
        fragment.setArguments(args);
        return fragment;
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
        if (getArguments() != null) {
            mState = getArguments().getString("state");
        }

        initRecyclerView();
        if (mState.equals("attend")) {
            queryAttendAuctions();
        } else if (mState.equals("bid")) {
            queryBidAuctions();
        } else if (mState.equals("win")) {
            queryWinAuctions();
        } else if (mState.equals("finish")) {
            queryFinishedAuctions();
        }
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
    }

    private void initRefresh() {
        mBinding.srlLayout.setOnRefreshListener(() -> {
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
                            page++;
                            if (mList.size() > 0) {
                                mAdapter.setNewData(mList);
                            }
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
                            page++;
                            if (mList.size() > 0) {
                                mAdapter.setNewData(mList);
                            }
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
                            page++;
                            if (mList.size() > 0) {
                                mAdapter.setNewData(mList);
                            }
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
                            page++;
                            if (mList.size() > 0) {
                                mAdapter.setNewData(mList);
                            }
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

}
