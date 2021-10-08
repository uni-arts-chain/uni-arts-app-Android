package com.gammaray.ui.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gammaray.R;
import com.gammaray.adapter.DAppGroupsAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentDappListLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.DAppGroupBean;
import com.gammaray.entity.DAppRecommendBean;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DAppListFragment extends BaseFragment<FragmentDappListLayoutBinding> {

    private DAppGroupsAdapter mAdapter;

    private List<DAppRecommendBean> mDAppRecommendBeans = new ArrayList<>();

    private List<DAppGroupBean> mDAppGroupBeans = new ArrayList<>();

    private String mChainId;


    static BaseFragment newInstance(String chainId) {
        DAppListFragment dAppListFragment = new DAppListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("chain_id", chainId);
        dAppListFragment.setArguments(bundle);
        return dAppListFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_dapp_list_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

        if (getArguments() != null) {
            mChainId = getArguments().getString("chain_id");
        }
        initData();
        queryRecommendDapps();
    }

    private void initData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        mAdapter = new DAppGroupsAdapter(mDAppGroupBeans, requireContext());
        mBinding.rvDappGroups.setLayoutManager(layoutManager);
        mBinding.rvDappGroups.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.tv_dapp_list_all) {
                //todo 每个组的全部点击事件
                ToastManager.showShort("全部---" + position);
            }
        });
    }

    private void queryRecommendDapps() {
        RequestManager.instance().queryRecommendDApps(mChainId, new MinerCallback<BaseResponseVo<List<DAppRecommendBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppRecommendBean>>> call, Response<BaseResponseVo<List<DAppRecommendBean>>> response) {
                if (response != null) {
                    if (response.body() != null) {
                        mDAppRecommendBeans = response.body().getBody();
                        if (mDAppRecommendBeans.size() > 0) {
                            int id = mDAppRecommendBeans.get(0).getChain_category().getId(); //最外层的ID
                            String title = mDAppRecommendBeans.get(0).getChain_category().getTitle(); //最外层的标题
                            //将推荐数据重新整合到Group数据中
                            DAppGroupBean dAppGroupBean = new DAppGroupBean();
                            dAppGroupBean.setId(id);
                            dAppGroupBean.setTitle(title);
                            List<DAppGroupBean.DApps> dApps = new ArrayList<>();
                            for (int i = 0; i < mDAppRecommendBeans.size(); i++) {
                                DAppGroupBean.DApps dapp = new DAppGroupBean.DApps();
                                dapp.setId(mDAppRecommendBeans.get(i).getId());
                                dapp.setTitle(mDAppRecommendBeans.get(i).getTitle());
                                dapp.setDesc(mDAppRecommendBeans.get(i).getDesc());
                                dapp.setWebsite_url(mDAppRecommendBeans.get(i).getWebsite_url());
                                dapp.setLogo(mDAppRecommendBeans.get(i).getLogo());
                                dApps.add(dapp);
                            }
                            dAppGroupBean.setDapps(dApps);
                            mDAppGroupBeans.add(dAppGroupBean);
                        }
                        queryCategoryDapps();
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<DAppRecommendBean>>> call, Response<BaseResponseVo<List<DAppRecommendBean>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    private void queryCategoryDapps() {
        RequestManager.instance().queryCategoryDApps(mChainId, new MinerCallback<BaseResponseVo<List<DAppGroupBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppGroupBean>>> call, Response<BaseResponseVo<List<DAppGroupBean>>> response) {
                if (response != null) {
                    if (response.body() != null) {
                        List<DAppGroupBean> dAppGroupBeans = response.body().getBody();
                        mDAppGroupBeans.addAll(dAppGroupBeans);
                        mAdapter.setNewData(mDAppGroupBeans);
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<DAppGroupBean>>> call, Response<BaseResponseVo<List<DAppGroupBean>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }
}
