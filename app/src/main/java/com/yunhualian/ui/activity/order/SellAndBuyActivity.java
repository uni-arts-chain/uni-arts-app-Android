package com.yunhualian.ui.activity.order;


import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.SellAndBuyAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityMessagesBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.BoughtArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/*
 * 买入卖出
 * */
public class SellAndBuyActivity extends BaseActivity<ActivityMessagesBinding> {

    SellAndBuyAdapter messagesAdapter;
    public static final String SELL = "sell";
    public static final String BUY = "buy";
    private List<BoughtArtVo> artVoList;
    private int sell = 0;
    private int bought = 1;
    int page = 1;
    String from;
    int default_perpage = 20;
    private boolean isSold = false;//是否卖出订单

    @Override
    public int getLayoutId() {
        return R.layout.activity_messages;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        artVoList = new ArrayList<>();
        from = getIntent().getExtras().getString("from");
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = from.equals(SELL) ? R.string.sell_title : R.string.buy_title;
        isSold = from.equals(SELL);
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        messagesAdapter = new SellAndBuyAdapter(artVoList, from.equals(SELL) ? sell : bought);
        mDataBinding.messageList.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.messageList);
        mDataBinding.messageList.setAdapter(messagesAdapter);
        messagesAdapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(OrderDetailActivity.BOUGHT_KEY, artVoList.get(position));
            bundle.putInt(OrderDetailActivity.ORDER_TYPE, from.equals(SELL) ? sell : bought);
            startActivity(OrderDetailActivity.class, bundle);
        });
        messagesAdapter.setOnLoadMoreListener(isSold ? this::querySold : this::queryBought, mDataBinding.messageList);
        mDataBinding.swipeRefresh.setOnRefreshListener(() -> {
            mDataBinding.swipeRefresh.setRefreshing(false);
            page = BigDecimal.ONE.intValue();
            if (isSold) querySold();
            else
                queryBought();
        });

        if (isSold) querySold();
        else
            queryBought();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void queryBought() {
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(default_perpage));
        RequestManager.instance().queryBrought(param, new MinerCallback<BaseResponseVo<List<BoughtArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<BoughtArtVo>>> call, Response<BaseResponseVo<List<BoughtArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    List<BoughtArtVo> boughtArtVos = response.body().getBody();
                    if (boughtArtVos.size() > BigDecimal.ZERO.intValue()) {
                        if (page == BigDecimal.ONE.intValue()) {
                            artVoList.clear();
                            artVoList = boughtArtVos;
                        } else {
                            artVoList.addAll(boughtArtVos);
                        }
                        messagesAdapter.setNewData(artVoList);
                        page++;
                    }
                    if (page > BigDecimal.ONE.intValue())
                        messagesAdapter.loadMoreEnd();
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<BoughtArtVo>>> call, Response<BaseResponseVo<List<BoughtArtVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });

    }

    public void querySold() {
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(page));
        param.put("per_page", String.valueOf(default_perpage));
        RequestManager.instance().querySold(param, new MinerCallback<BaseResponseVo<List<BoughtArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<BoughtArtVo>>> call, Response<BaseResponseVo<List<BoughtArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    List<BoughtArtVo> boughtArtVos = response.body().getBody();
                    if (boughtArtVos.size() > BigDecimal.ZERO.intValue()) {
                        if (page == BigDecimal.ONE.intValue()) {
                            artVoList.clear();
                            artVoList = boughtArtVos;
                        } else {
                            artVoList.addAll(boughtArtVos);
                        }
                        messagesAdapter.setNewData(artVoList);
                        page++;
                    }
                    if (page > BigDecimal.ONE.intValue())
                        messagesAdapter.loadMoreEnd();
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<BoughtArtVo>>> call, Response<BaseResponseVo<List<BoughtArtVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}