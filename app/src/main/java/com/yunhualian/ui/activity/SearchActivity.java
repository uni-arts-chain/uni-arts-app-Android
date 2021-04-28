package com.yunhualian.ui.activity;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.adapter.PicturesAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivitySearchBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.DTRequest;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.OnResultListener;
import com.yunhualian.net.RequestManager;
import com.yunhualian.service.API;
import com.yunhualian.service.AsynCommon;
import com.yunhualian.service.Head;
import com.yunhualian.service.MarketService;
import com.yunhualian.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> {

    private List<String> historyList;

    private PicturesAdapter picturesAdapter;
    private List<SellingArtVo> artList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        mDataBinding.cancle.setOnClickListener(v -> finish());
        historyList = SharedPreUtils.getSearchHistory(this);
        mDataBinding.searchHistory.setLayoutManager(new LinearLayoutManager(this));
        historyList = SharedPreUtils.getSearchHistory(this);
        HistoryListAdapter historyListAdapter = new HistoryListAdapter(historyList);
        mDataBinding.searchHistory.setAdapter(historyListAdapter);
        historyListAdapter.setOnItemClickListener((adapter, view, position) -> {
            mDataBinding.searchEx.setText(historyList.get(position));
        });
        historyListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            SharedPreUtils.deleteSearchHistory(SearchActivity.this, historyList.get(position));
            historyList.clear();
            historyList = SharedPreUtils.getSearchHistory(this);
            historyListAdapter.setNewData(historyList);
        });
        mDataBinding.searchEx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    search(s.toString());
                }
            }
        });
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        picturesAdapter = new PicturesAdapter(artList);
        mDataBinding.resultList.setLayoutManager(layoutManager);
        mDataBinding.resultList.setAdapter(picturesAdapter);
        initRefresh();
        mDataBinding.clearHistory.setOnClickListener(v -> {
            SharedPreUtils.clearAll(this);
            historyListAdapter.setNewData(new ArrayList<>());
        });
    }

    private void initRefresh() {
        mDataBinding.srlShoopingMall.setColorSchemeResources(R.color.colorAccent);
        mDataBinding.srlShoopingMall.setDistanceToTriggerSync(500);
        mDataBinding.srlShoopingMall.setOnRefreshListener(() -> {
            search(mDataBinding.searchEx.getText().toString());
            mDataBinding.srlShoopingMall.setRefreshing(false);
        });
    }

    public void search(String keyWords) {
        showLoading("加载中...");
        HashMap<String, String> params = new HashMap<>();
        params.put("q", keyWords);
        params.put("page", "1");
        params.put("per_page", "100");
        RequestManager.instance().searchArts(params, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    artList = response.body().getBody();
                    if (artList.size() > 0) {
                        SharedPreUtils.saveSearchHistory(SearchActivity.this, mDataBinding.searchEx.getText().toString());
                        picturesAdapter.setNewData(artList);
                        mDataBinding.searchHistoryLayout.setVisibility(View.GONE);
                    } else mDataBinding.searchHistoryLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
//        sendRequest(MarketService.getInstance().MarketDepth, params, false, false, false);

    }

//    @Override
//    public void OnResult(DTRequest request, Head head, Object response) {
//
//        if (head.isSuccess()) {
//            dismissLoading();
//            artList = (List<SellingArtVo>) response;
//            picturesAdapter.setNewData(artList);
//        }
//    }

    public class HistoryListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public HistoryListAdapter(@Nullable List<String> data) {
            super(R.layout.item_search_history_text, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.searchTx, item);
            helper.addOnClickListener(R.id.clear);
        }

    }

//    private AsynCommon sendRequest(API api, HashMap<String, String> params, boolean showprogressDialog, boolean showErrorMsgOneTime, boolean showErrorMsg) {
//        if (showprogressDialog)
//            showLoading("");
//
//        return AsynCommon.SendRequest(api, params, showErrorMsgOneTime, showErrorMsg, this, this);
//    }
}