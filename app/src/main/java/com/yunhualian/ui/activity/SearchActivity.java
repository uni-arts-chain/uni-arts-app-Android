package com.yunhualian.ui.activity;


import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.android.material.tabs.TabLayout;
import com.yunhualian.R;
import com.yunhualian.adapter.MyHomePageAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivitySearchBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.fragment.SearchResultFragment;
import com.yunhualian.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> implements MyHomePageAdapter.TabPagerListener {

    private List<String> historyList;

    private List<SellingArtVo> artList = new ArrayList<>();

    private MyHomePageAdapter pageAdapter;

    private SearchResultFragment sellFragment;

    private SearchResultFragment auctionFragment;

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
//                    search(s.toString());
                }
            }
        });
        mDataBinding.searchEx.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String searchText = mDataBinding.searchEx.getText().toString();
                    if (!TextUtils.isEmpty(searchText))
                        search(searchText);
                    return true;
                }

                return false;
            }
        });
        sellFragment = new SearchResultFragment(artList);
        auctionFragment = new SearchResultFragment(artList);
        pageAdapter = new MyHomePageAdapter(getSupportFragmentManager(), 2, Arrays.asList(getResources().getStringArray(R.array.mall_tabs)), this);
        pageAdapter.setListener(this);
        mDataBinding.viewpager.setAdapter(pageAdapter);
        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.viewpager);
        mDataBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        picturesAdapter = new PicturesAdapter(artList);
//        mDataBinding.resultList.setLayoutManager(layoutManager);
//        mDataBinding.resultList.setAdapter(picturesAdapter);
//        picturesAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.resultList);
//        picturesAdapter.setOnItemClickListener((adapter, view, position) -> {
//
//            if (artList != null && artList.size() > 0) {
//                Intent intent = new Intent(this, ArtDetailActivity.class);
//                intent.putExtra(ArtDetailActivity.ART_ID, artList.get(position).getId());
//                startActivity(intent);
//            }
//
//        });
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
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> params = new HashMap<>();
        params.put("q", keyWords);
        params.put("page", "1");
        params.put("per_page", "100");
        RequestManager.instance().searchArts(params, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    SharedPreUtils.saveSearchHistory(SearchActivity.this, mDataBinding.searchEx.getText().toString());
                    artList = response.body().getBody();
                    mDataBinding.searchHistoryLayout.setVisibility(View.GONE);
                    sellFragment.updateData(artList);
                    auctionFragment.updateData(artList);
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

    @Override
    public Fragment getFragment(int position) {
        if (position == 0) {
            return sellFragment;
        }
        return auctionFragment;
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