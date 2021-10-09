package com.gammaray.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gammaray.R;
import com.gammaray.adapter.ChainDAppsAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityDappsListLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.DAppItemBean;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

//链-收藏全部DApp列表
public class ChainRecommendDAppsActivity extends BaseActivity<ActivityDappsListLayoutBinding> {

    private String mTitle;

    private int mChainId;

    private int mPage = 1;

    private int mPerPage = 20;

    private List<DAppItemBean> mRecommendDApps = new ArrayList<>();

    private ChainDAppsAdapter mRecommendAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_dapps_list_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        if (getIntent() != null) {
            mTitle = getIntent().getStringExtra("title");
            mChainId = getIntent().getIntExtra("chain_id", 0);
            ToolBarOptions toolBarOptions = new ToolBarOptions();
            toolBarOptions.titleString = mTitle;
            setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);
        }

        initRecyclerView();

        queryRecommendDapps();

        mDataBinding.srlApps.setOnRefreshListener(() -> {
            mDataBinding.srlApps.setRefreshing(false);
            mPage = 1;
            queryRecommendDapps();
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager recentLayoutManager = new LinearLayoutManager(this);
        mRecommendAdapter = new ChainDAppsAdapter(this, mRecommendDApps);
        mDataBinding.rvDapps.setLayoutManager(recentLayoutManager);
        mRecommendAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.rvDapps);
        mRecommendAdapter.setEnableLoadMore(true);
        mDataBinding.rvDapps.setAdapter(mRecommendAdapter);
    }

    private void queryRecommendDapps() {
        RequestManager.instance().queryRecommendDApps(String.valueOf(mChainId), new MinerCallback<BaseResponseVo<List<DAppItemBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppItemBean>>> call, Response<BaseResponseVo<List<DAppItemBean>>> response) {
                    if (response != null && response.isSuccessful()) {
                        if (response.body() != null) {
                            List<DAppItemBean> recentApps = response.body().getBody();
                            if (recentApps != null && recentApps.size() > 0) {
                                if (mPage == 1) {
                                    mRecommendDApps.clear();
                                    mRecommendDApps = recentApps;
                                } else {
                                    mRecommendDApps.addAll(recentApps);
                                }
                                mPage++;
                                if (mRecommendDApps.size() > 0) {
                                    mRecommendAdapter.setNewData(mRecommendDApps);
                                }
                            } else {
                                if (mPage > 1) {
                                    mRecommendAdapter.loadMoreEnd();
                                }
                            }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<DAppItemBean>>> call, Response<BaseResponseVo<List<DAppItemBean>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}
