package com.gammaray.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gammaray.R;
import com.gammaray.adapter.CollectDAppsAdapter;
import com.gammaray.adapter.CollectedDAppsAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityDappsListLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.DAppFavouriteBean;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

//收藏全部DApp列表
public class CollectedDAppsActivity extends BaseActivity<ActivityDappsListLayoutBinding> {

    private String mTitle;

    private int mPage = 1;

    private int mPerPage = 20;

    private List<DAppFavouriteBean> mCollectApps = new ArrayList<>();

    private CollectDAppsAdapter mCollectAdapter;

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
            ToolBarOptions toolBarOptions = new ToolBarOptions();
            toolBarOptions.titleString = mTitle;
            setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);
        }

        initRecyclerView();

        getCollectedDApps();

        mDataBinding.srlApps.setOnRefreshListener(() -> {
            mDataBinding.srlApps.setRefreshing(false);
            mPage = 1;
            getCollectedDApps();
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager recentLayoutManager = new LinearLayoutManager(this);
        mCollectAdapter = new CollectDAppsAdapter(this, mCollectApps);
        mDataBinding.rvDapps.setLayoutManager(recentLayoutManager);
        mCollectAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.rvDapps);
        mCollectAdapter.setEnableLoadMore(true);
        mDataBinding.rvDapps.setAdapter(mCollectAdapter);
    }

    private void getCollectedDApps() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryCollectedDApps(mPage, mPerPage, new MinerCallback<BaseResponseVo<List<DAppFavouriteBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppFavouriteBean>>> call, Response<BaseResponseVo<List<DAppFavouriteBean>>> response) {
                dismissLoading();
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        List<DAppFavouriteBean> recentApps = response.body().getBody();
                        if (recentApps != null && recentApps.size() > 0) {
                            if (mPage == 1) {
                                mCollectApps.clear();
                                mCollectApps = recentApps;
                            } else {
                                mCollectApps.addAll(recentApps);
                            }
                            mPage++;
                            if (mCollectApps.size() > 0) {
                                mCollectAdapter.setNewData(mCollectApps);
                            }
                        } else {
                            if (mPage > 1) {
                                mCollectAdapter.loadMoreEnd();
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<DAppFavouriteBean>>> call, Response<BaseResponseVo<List<DAppFavouriteBean>>> response) {
                dismissLoading();
                mCollectAdapter.loadMoreEnd();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
                mCollectAdapter.loadMoreEnd();
            }
        });
    }
}
