package com.gammaray.ui.activity;

import android.content.Intent;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gammaray.R;
import com.gammaray.adapter.AccountHistoryAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityCashAccountLayoutBinding;
import com.gammaray.entity.AccountVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.HistoriesBean;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.wallet.WithdrawActivity;

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
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                queryHistories();
            }
        },mDataBinding.rvBills);
        mDataBinding.rvBills.setAdapter(adapter);
        initRefresh();
        queryHistories();

        mDataBinding.btnWithdraw.setOnClickListener(this);
    }

    private void queryHistories() {
        showLoading(getString(R.string.progress_loading));
        RequestManager.instance().queryAccountHistory(page, 20,new MinerCallback<BaseResponseVo<List<HistoriesBean>>>() {
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
                        }else{
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
                adapter.loadMoreEnd();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
                adapter.loadMoreEnd();
            }
        });
    }

    private void initRefresh() {
        mDataBinding.srlBills.setOnRefreshListener(() -> {
            page = 1;
            queryHistories();
            queryAccountInfo();
            mDataBinding.srlBills.setRefreshing(false);
        });
    }

    private void queryAccountInfo() {
        RequestManager.instance().queryAccount(new MinerCallback<BaseResponseVo<List<AccountVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AccountVo>>> call, Response<BaseResponseVo<List<AccountVo>>> response) {
                if (response.isSuccessful()) {
                    List<AccountVo> accounts = response.body().getBody();
                    if (accounts != null && accounts.size() > 0) {
                        for (int i = 0; i < accounts.size(); i++) {
                            if (accounts.get(i).getCurrency_code().equals("rmb")) {
                                accountRemain = accounts.get(i).getBalance();
                                mDataBinding.tvTotalCash.setText(getString(R.string.total_cash, accountRemain));
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<AccountVo>>> call, Response<BaseResponseVo<List<AccountVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_withdraw) {
            if (Double.parseDouble(accountRemain) == 0) {
                ToastUtils.showShort("没有可用余额");
                return;
            }
            Intent intent = new Intent(CashAccountActivity.this,WithdrawActivity.class);
            intent.putExtra("remains",accountRemain);
            startActivity(intent);
        }
    }
}
