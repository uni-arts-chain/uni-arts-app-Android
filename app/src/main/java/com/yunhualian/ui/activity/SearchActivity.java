package com.yunhualian.ui.activity;


import android.text.TextUtils;
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
import com.yunhualian.ui.fragment.SearchAuctionsFragment;
import com.yunhualian.ui.fragment.SearchResultFragment;
import com.yunhualian.utils.SharedPreUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends BaseActivity<ActivitySearchBinding> implements MyHomePageAdapter.TabPagerListener {

    private List<String> historyList;

    private HistoryListAdapter historyListAdapter;

    private MyHomePageAdapter pageAdapter;

    private SearchResultFragment sellFragment;

    private SearchAuctionsFragment auctionFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        sellFragment = new SearchResultFragment();
        auctionFragment = new SearchAuctionsFragment();
        pageAdapter = new MyHomePageAdapter(getSupportFragmentManager(), 2, Arrays.asList(getResources().getStringArray(R.array.mall_tabs)), this);
        pageAdapter.setListener(this);
        mDataBinding.viewpager.setAdapter(pageAdapter);
        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.viewpager);
        mDataBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);

        historyList = SharedPreUtils.getSearchHistory(this);
        historyListAdapter = new HistoryListAdapter(historyList);
        mDataBinding.searchHistory.setLayoutManager(new LinearLayoutManager(this));
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

        mDataBinding.clearHistory.setOnClickListener(v -> {
            SharedPreUtils.clearAll(this);
            historyListAdapter.setNewData(new ArrayList<>());
        });

        mDataBinding.searchEx.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String searchText = mDataBinding.searchEx.getText().toString();
                if (!TextUtils.isEmpty(searchText)) {
                    toSearch(searchText);
                    mDataBinding.searchHistoryLayout.setVisibility(View.GONE);
                    SharedPreUtils.saveSearchHistory(SearchActivity.this, mDataBinding.searchEx.getText().toString());
                }
                return true;
            }
            return false;
        });

        mDataBinding.cancle.setOnClickListener(v -> finish());
    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0) {
            return sellFragment;
        }
        return auctionFragment;
    }

    private void toSearch(String keyWord) {
        if (sellFragment != null) {
            sellFragment.searchKeyWords(keyWord);
        }
        if (auctionFragment != null) {
            auctionFragment.searchKeyWords(keyWord);
        }
    }

    private static class HistoryListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public HistoryListAdapter(@Nullable List<String> data) {
            super(R.layout.item_search_history_text, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.searchTx, item);
            helper.addOnClickListener(R.id.clear);
        }
    }
}