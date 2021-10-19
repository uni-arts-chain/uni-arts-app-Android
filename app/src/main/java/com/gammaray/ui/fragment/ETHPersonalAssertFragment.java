package com.gammaray.ui.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gammaray.R;
import com.gammaray.adapter.ETHWalletNFTsAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentPersonalNftsLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.NFTSBean;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class ETHPersonalAssertFragment extends BaseFragment<FragmentPersonalNftsLayoutBinding> {

    private ETHWalletNFTsAdapter mAdapter;

    private List<NFTSBean> mWalletNFTTokens = new ArrayList<>();

    private String mAddress;

    public static BaseFragment newInstance(String address) {
        ETHPersonalAssertFragment fragment = new ETHPersonalAssertFragment();
        Bundle args = new Bundle();
        args.putString("address", address);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_personal_nfts_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

        if (getArguments() != null) {
            mAddress = getArguments().getString("address");
        }

        mAdapter = new ETHWalletNFTsAdapter(mWalletNFTTokens);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.rvAsserts.setLayoutManager(layoutManager);
        mAdapter.setEmptyView(R.layout.layout_entrust_empty_for_homepage, mBinding.rvAsserts);
        mBinding.rvAsserts.setAdapter(mAdapter);
        queryNFTsInETH(mAddress);

        mBinding.srlNfts.setOnRefreshListener(() -> {
            mBinding.srlNfts.setRefreshing(false);
            queryNFTsInETH(mAddress);
        });
    }

    private void queryNFTsInETH(String address) {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryNFTsInETH(address, new MinerCallback<BaseResponseVo<List<NFTSBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<NFTSBean>>> call, Response<BaseResponseVo<List<NFTSBean>>> response) {
                dismissLoading();
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        mWalletNFTTokens.clear();
                        mWalletNFTTokens = response.body().getBody();
                        if (mWalletNFTTokens != null && mWalletNFTTokens.size() > 0) {
                            mAdapter.setNewData(mWalletNFTTokens);
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<NFTSBean>>> call, Response<BaseResponseVo<List<NFTSBean>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
               dismissLoading();
            }
        });
    }
}
