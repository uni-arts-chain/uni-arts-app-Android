package com.gammaray.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gammaray.R;
import com.gammaray.adapter.HotSearchDAppAdapter;
import com.gammaray.adapter.SearchDAppsListAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.databinding.ActivityDappSearchLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.DAppSearchBean;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

//DApps搜索页面
public class DAppSearchActivity extends BaseActivity<ActivityDappSearchLayoutBinding> {

    private HotSearchDAppAdapter mHotSearchAdapter;

    private List<DAppSearchBean> mDApps = new ArrayList<>();

    private SearchDAppsListAdapter mSearchAdapter;

    private List<DAppSearchBean> mResults = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_dapp_search_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        initHotSearchDApp();

        queryHotSearchDApp();

        initSearchDApp();

        mDataBinding.rlSearchResult.setVisibility(View.GONE);
        mDataBinding.rlHotSearchResult.setVisibility(View.VISIBLE);
        mDataBinding.cancel.setOnClickListener(view -> finish());
        mDataBinding.rlClear.setOnClickListener(view -> clear());
        mDataBinding.searchEx.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    mDataBinding.rlClear.setVisibility(View.VISIBLE);
                    mDataBinding.rlHotSearchResult.setVisibility(View.GONE);
                    mDataBinding.rlSearchResult.setVisibility(View.VISIBLE);
                    searchKeyWords(charSequence.toString());
                } else {
                    mDataBinding.rlClear.setVisibility(View.GONE);
                    mDataBinding.rlHotSearchResult.setVisibility(View.VISIBLE);
                    mDataBinding.rlSearchResult.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //热门搜索列表初始化
    private void initHotSearchDApp() {
        mHotSearchAdapter = new HotSearchDAppAdapter(this, mDApps);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 5);
        mDataBinding.rvHotSearchResult.setLayoutManager(layoutManager);
        mDataBinding.rvHotSearchResult.setAdapter(mHotSearchAdapter);
        mHotSearchAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(DAppSearchActivity.this, DAppWebsActivity.class);
            intent.putExtra("dapp_title", mDApps.get(position).getTitle());
            intent.putExtra("dapp_id", mDApps.get(position).getId());
                intent.putExtra("dapp_collect", mDApps.get(position).isFavorite_by_me());
            try {
                intent.putExtra("dapp_icon_url", URLDecoder.decode(mDApps.get(position).getLogo().getUrl(), "UTF-8"));
                intent.putExtra("dapp_url", URLDecoder.decode(mDApps.get(position).getWebsite_url(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        });
    }

    private void initSearchDApp() {
        mSearchAdapter = new SearchDAppsListAdapter(this, mResults);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mDataBinding.rvSearchResult.setLayoutManager(layoutManager);
        mDataBinding.rvSearchResult.setAdapter(mSearchAdapter);
        mSearchAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(DAppSearchActivity.this, DAppWebsActivity.class);
            intent.putExtra("dapp_title", mResults.get(position).getTitle());
            intent.putExtra("dapp_id", mResults.get(position).getId());
            intent.putExtra("dapp_collect", mResults.get(position).isFavorite_by_me());
            try {
                intent.putExtra("dapp_icon_url", URLDecoder.decode(mResults.get(position).getLogo().getUrl(), "UTF-8"));
                intent.putExtra("dapp_url", URLDecoder.decode(mResults.get(position).getWebsite_url(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        });
    }

    //热门搜索数据
    private void queryHotSearchDApp() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryHotSearchDApps(new MinerCallback<BaseResponseVo<List<DAppSearchBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppSearchBean>>> call, Response<BaseResponseVo<List<DAppSearchBean>>> response) {
                dismissLoading();
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        mDApps.clear();
                        mDApps = response.body().getBody();
                        if (mDApps.size() > 0) {
                            mHotSearchAdapter.setNewData(mDApps);
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<DAppSearchBean>>> call, Response<BaseResponseVo<List<DAppSearchBean>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private void searchKeyWords(String keyWords) {
        showLoading(R.string.progress_loading);
        HashMap<String, String> params = new HashMap<>();
        params.put("q", keyWords);
        RequestManager.instance().searchDApps(params, new MinerCallback<BaseResponseVo<List<DAppSearchBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppSearchBean>>> call, Response<BaseResponseVo<List<DAppSearchBean>>> response) {
                dismissLoading();
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        mResults = response.body().getBody();
                        mSearchAdapter.setNewData(mResults);
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<DAppSearchBean>>> call, Response<BaseResponseVo<List<DAppSearchBean>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private void clear() {
        mDataBinding.searchEx.getText().clear();
    }
}
