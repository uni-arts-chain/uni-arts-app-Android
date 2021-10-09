package com.gammaray.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gammaray.R;
import com.gammaray.adapter.RecentDAppsAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityDappsListLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.DAppRecentlyBean;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

//最近全部DApp列表
public class RecentlyDAppsActivity extends BaseActivity<ActivityDappsListLayoutBinding> {

    private String mTitle;

    private int mPage = 1;

    private int mPerPage = 20;

    private List<DAppRecentlyBean> mRecentApps = new ArrayList<>();

    private RecentDAppsAdapter mRecentAdapter;

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

        getRecentlyDApps();

        mDataBinding.srlApps.setOnRefreshListener(() -> {
            mDataBinding.srlApps.setRefreshing(false);
            mPage = 1;
            getRecentlyDApps();
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager recentLayoutManager = new LinearLayoutManager(this);
        mRecentAdapter = new RecentDAppsAdapter(this, mRecentApps);
        mDataBinding.rvDapps.setLayoutManager(recentLayoutManager);
        mRecentAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.rvDapps);
        mRecentAdapter.setEnableLoadMore(true);
        mDataBinding.rvDapps.setAdapter(mRecentAdapter);
    }

    private void getRecentlyDApps() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryRecentlyDApps(mPage, mPerPage, new MinerCallback<BaseResponseVo<List<DAppRecentlyBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppRecentlyBean>>> call, Response<BaseResponseVo<List<DAppRecentlyBean>>> response) {
                dismissLoading();
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        List<DAppRecentlyBean> recentApps = response.body().getBody();
                        if (recentApps != null && recentApps.size() > 0) {
                            if (mPage == 1) {
                                mRecentApps.clear();
                                mRecentApps = recentApps;
                            } else {
                                mRecentApps.addAll(recentApps);
                            }
                            mPage++;
                            if (mRecentApps.size() > 0) {
                                mRecentAdapter.setNewData(mRecentApps);
                            }
                        } else {
                            if (mPage > 1) {
                                mRecentAdapter.loadMoreEnd();
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<DAppRecentlyBean>>> call, Response<BaseResponseVo<List<DAppRecentlyBean>>> response) {
                dismissLoading();
                mRecentAdapter.loadMoreEnd();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
                mRecentAdapter.loadMoreEnd();
            }
        });
    }
}
