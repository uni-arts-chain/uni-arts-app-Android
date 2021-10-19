package com.gammaray.ui.fragment;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.gammaray.R;
import com.gammaray.adapter.WalletNFTsAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentPersonalNftsLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.SellingArtVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class PersonalNFTSFragment extends BaseFragment<FragmentPersonalNftsLayoutBinding> {

    private List<SellingArtVo> mArtSellVos = new ArrayList<>();

    private WalletNFTsAdapter mAdapter;

    public static Fragment newInstance() {
        return new PersonalNFTSFragment();
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
        initData();
        queryArts();
    }

    private void initData() {
        mAdapter = new WalletNFTsAdapter(mArtSellVos);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        mBinding.rvAsserts.setLayoutManager(layoutManager);
        mBinding.rvAsserts.setAdapter(mAdapter);

        mBinding.srlNfts.setOnRefreshListener(() -> {
            mBinding.srlNfts.setRefreshing(false);
            queryArts();
        });
    }

    private void queryArts() {
        showLoading(R.string.progress_loading);
        HashMap<String, String> param = new HashMap<>();
        param.put("aasm_state", "online");
        param.put("page", "1");
        param.put("per_page", "100");
        RequestManager.instance().queryMine(param, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        if (mArtSellVos != null && mArtSellVos.size() > 0) {
                            mArtSellVos.clear();
                        }
                        mArtSellVos = response.body().getBody();
                        if (mArtSellVos.size() > 0) {
                            mAdapter.setNewData(mArtSellVos);
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
