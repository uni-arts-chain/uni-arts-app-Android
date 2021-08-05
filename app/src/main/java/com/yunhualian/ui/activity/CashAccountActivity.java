package com.yunhualian.ui.activity;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.AccountHistoryAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityCashAccountLayoutBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.HistoriesBean;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.wallet.WithdrawActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * @Date:2021/8/5
 * @Author:Created by peter_ben
 */
public class CashAccountActivity extends BaseActivity<ActivityCashAccountLayoutBinding> implements View.OnClickListener {

    private String accountRemain = "0";
    private AccountHistoryAdapter adapter;
    private List<HistoriesBean> list = new ArrayList<>();
    private int page = 1;
    private HashMap<String, String> params = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_cash_account_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions toolBarOptions = new ToolBarOptions();
        toolBarOptions.titleId = R.string.cash_account_;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);

        if (getIntent() != null) {
            accountRemain = getIntent().getStringExtra("account_remain");
            mDataBinding.tvTotalCash.setText(getString(R.string.total_cash, accountRemain));
        }

        adapter = new AccountHistoryAdapter(list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDataBinding.rvBills.setLayoutManager(layoutManager);
        adapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.rvBills);
        mDataBinding.rvBills.setAdapter(adapter);
        adapter.setOnLoadMoreListener(this::queryHistories, mDataBinding.rvBills);
        initRefresh();
        queryHistories();

        mDataBinding.btnWithdraw.setOnClickListener(this);
    }

    private void queryHistories() {
        showLoading(getString(R.string.progress_loading));
        RequestManager.instance().queryAccountHistory(page, new MinerCallback<BaseResponseVo<List<HistoriesBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<HistoriesBean>>> call, Response<BaseResponseVo<List<HistoriesBean>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<HistoriesBean> datas = response.body().getBody();
                        if (datas != null && datas.size() > 0) {
                            if (page == 1) {
                                list.clear();
                                list = datas;
                            } else {
                                list.addAll(datas);
                            }
                            page++;
                            if (list.size() > 0) {
                                adapter.setNewData(list);
                            }
                            if (page > 1) {
                                adapter.loadMoreEnd();
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<HistoriesBean>>> call, Response<BaseResponseVo<List<HistoriesBean>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private void initRefresh() {
        mDataBinding.srlBills.setOnRefreshListener(() -> {
            page = 1;
            queryHistories();
            mDataBinding.srlBills.setRefreshing(false);
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_withdraw) {
            startActivity(WithdrawActivity.class);
        }
    }
}
