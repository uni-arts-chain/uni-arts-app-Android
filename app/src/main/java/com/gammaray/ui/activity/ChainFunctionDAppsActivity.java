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
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

//链-其他全部DApp列表
public class ChainFunctionDAppsActivity extends BaseActivity<ActivityDappsListLayoutBinding> {

    private String mTitle;

    private int mChainId;

    private int mPage = 1;

    private int mPerPage = 20;

    private List<DAppItemBean> mFunctions = new ArrayList<>();

    private ChainDAppsAdapter mChainFuncitonAdapter;

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

        queryCategoryDapps();

        mDataBinding.srlApps.setOnRefreshListener(() -> {
            mDataBinding.srlApps.setRefreshing(false);
            mPage = 1;
            queryCategoryDapps();
        });
    }

    private void initRecyclerView() {
        LinearLayoutManager recentLayoutManager = new LinearLayoutManager(this);
        mChainFuncitonAdapter = new ChainDAppsAdapter(this, mFunctions);
        mDataBinding.rvDapps.setLayoutManager(recentLayoutManager);
        mChainFuncitonAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.rvDapps);
        mChainFuncitonAdapter.setEnableLoadMore(true);
        mDataBinding.rvDapps.setAdapter(mChainFuncitonAdapter);
    }

    private void queryCategoryDapps() {
        showLoading(R.string.progress_loading);
        HashMap<String, String> params = new HashMap<>();
        params.put("chain_category_id", String.valueOf(mChainId));
        params.put("page", String.valueOf(mPage));
        params.put("per_page", String.valueOf(mPerPage));
        RequestManager.instance().queryChainDApps(params, new MinerCallback<BaseResponseVo<List<DAppItemBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppItemBean>>> call, Response<BaseResponseVo<List<DAppItemBean>>> response) {
               dismissLoading();
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        List<DAppItemBean> chainFctions = response.body().getBody();
                        if (chainFctions != null && chainFctions.size() > 0) {
                            if (mPage == 1) {
                                mFunctions.clear();
                                mFunctions = chainFctions;
                            } else {
                                mFunctions.addAll(chainFctions);
                            }
                            mPage++;
                            if (mFunctions.size() > 0) {
                                mChainFuncitonAdapter.setNewData(mFunctions);
                            }
                        } else {
                            if (mPage > 1) {
                                mChainFuncitonAdapter.loadMoreEnd();
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

//    private void queryRecommendDapps() {
//        RequestManager.instance().queryChainDApps(String.valueOf(mChainId), new MinerCallback<BaseResponseVo<List<DAppItemBean>>>() {
//            @Override
//            public void onSuccess(Call<BaseResponseVo<List<DAppItemBean>>> call, Response<BaseResponseVo<List<DAppItemBean>>> response) {
//                if (response != null && response.isSuccessful()) {
//                    if (response.body() != null) {
//                        List<DAppItemBean> recentApps = response.body().getBody();
//                        if (recentApps != null && recentApps.size() > 0) {
//                            if (mPage == 1) {
//                                mFunctions.clear();
//                                mFunctions = recentApps;
//                            } else {
//                                mFunctions.addAll(recentApps);
//                            }
//                            mPage++;
//                            if (mFunctions.size() > 0) {
//                                mRecommendAdapter.setNewData(mFunctions);
//                            }
//                        } else {
//                            if (mPage > 1) {
//                                mRecommendAdapter.loadMoreEnd();
//                            }
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onError(Call<BaseResponseVo<List<DAppItemBean>>> call, Response<BaseResponseVo<List<DAppItemBean>>> response) {
//                dismissLoading();
//            }
//
//            @Override
//            public void onFailure(Call<?> call, Throwable t) {
//                dismissLoading();
//            }
//        });
//    }
}
