package com.yunhualian.ui.activity.order;


import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.qmuiteam.qmui.util.QMUIStatusBarHelper;
import com.yunhualian.R;
import com.yunhualian.adapter.SellAndBuyAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityMessagesBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.BoughtArtVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;


import org.web3j.crypto.Hash;

import java.util.Arrays;
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
    int page = 1;
    String from;

    @Override
    public int getLayoutId() {
        return R.layout.activity_messages;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        from = getIntent().getExtras().getString("from");
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = from.equals(SELL) ? R.string.sell_title : R.string.buy_title;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        messagesAdapter = new SellAndBuyAdapter(artVoList);
        mDataBinding.messageList.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.messageList);
        mDataBinding.messageList.setAdapter(messagesAdapter);
        messagesAdapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(OrderDetailActivity.BOUGHT_KEY, artVoList.get(position));
            startActivity(OrderDetailActivity.class, bundle);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        queryBought();
    }

    public void queryBought() {
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(page));
        param.put("per_page", "20");
        RequestManager.instance().queryBrought(param, new MinerCallback<BaseResponseVo<List<BoughtArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<BoughtArtVo>>> call, Response<BaseResponseVo<List<BoughtArtVo>>> response) {
                if (response.isSuccessful()) {
                    artVoList = response.body().getBody();
                    if (artVoList.size() > 0)
                        messagesAdapter.setNewData(artVoList);
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<BoughtArtVo>>> call, Response<BaseResponseVo<List<BoughtArtVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }
}