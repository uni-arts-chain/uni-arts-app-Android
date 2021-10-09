package com.gammaray.ui.fragment;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gammaray.R;
import com.gammaray.adapter.DAppGroupsAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentDappListLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.DAppGroupBean;
import com.gammaray.entity.DAppItemBean;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.utils.ToastManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class DAppListFragment extends BaseFragment<FragmentDappListLayoutBinding> {

    private DAppGroupsAdapter mAdapter;

    private List<DAppItemBean> mDAppItemBeans = new ArrayList<>();

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
        //若ViewPager嵌套的RecyclerView，且NestscrollView需要整体滑动，则需要重写ViewPager并使用下方LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
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
        RequestManager.instance().queryRecommendDApps(mChainId, new MinerCallback<BaseResponseVo<List<DAppItemBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppItemBean>>> call, Response<BaseResponseVo<List<DAppItemBean>>> response) {
                if (response != null) {
                    if (response.body() != null) {
                        mDAppItemBeans = response.body().getBody();
                        if (mDAppItemBeans.size() > 0) {
                            int id = mDAppItemBeans.get(0).getChain_category().getId(); //最外层的ID
                            String title = mDAppItemBeans.get(0).getChain_category().getTitle(); //最外层的标题
                            //将推荐数据重新整合到Group数据中
                            DAppGroupBean dAppGroupBean = new DAppGroupBean();
                            dAppGroupBean.setId(id);
                            dAppGroupBean.setTitle(title);
                            List<DAppGroupBean.DApps> dApps = new ArrayList<>();
                            for (int i = 0; i < mDAppItemBeans.size(); i++) {
                                DAppGroupBean.DApps dapp = new DAppGroupBean.DApps();
                                dapp.setId(mDAppItemBeans.get(i).getId());
                                dapp.setTitle(mDAppItemBeans.get(i).getTitle());
                                dapp.setDesc(mDAppItemBeans.get(i).getDesc());
                                dapp.setWebsite_url(mDAppItemBeans.get(i).getWebsite_url());
                                dapp.setLogo(mDAppItemBeans.get(i).getLogo());
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
            public void onError(Call<BaseResponseVo<List<DAppItemBean>>> call, Response<BaseResponseVo<List<DAppItemBean>>> response) {

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
