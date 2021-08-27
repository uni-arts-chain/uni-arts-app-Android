package com.yunhualian.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunhualian.R;
import com.yunhualian.adapter.OfferPriceListAllAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityOfferPriceRecordLayoutBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.HistoriesBean;
import com.yunhualian.entity.OfferPriceBean;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class OfferPriceListActivity extends BaseActivity<ActivityOfferPriceRecordLayoutBinding> {

    private String offerPriceTimes;

    private String art_id;

    private List<OfferPriceBean> mOfferPriceList = new ArrayList<>();

    private OfferPriceListAllAdapter mOfferPriceAdapter;

    private int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_offer_price_record_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        if (getIntent() != null) {
            art_id = getIntent().getStringExtra("art_id");
        }
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.offer_price_title;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        mOfferPriceAdapter = new OfferPriceListAllAdapter(this, mOfferPriceList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDataBinding.rvOfferPrice.setLayoutManager(layoutManager);
        mOfferPriceAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.rvOfferPrice);
        mDataBinding.rvOfferPrice.setAdapter(mOfferPriceAdapter);
        mOfferPriceAdapter.setOnLoadMoreListener(() -> {
            getOfferPriceList(art_id);
        }, mDataBinding.rvOfferPrice);

        mDataBinding.srlOfferPrice.setOnRefreshListener(() -> {
            page = 1;
            getOfferPriceList(art_id);
            mDataBinding.srlOfferPrice.setRefreshing(false);
        });

        getOfferPriceList(art_id);

    }

    private void getOfferPriceList(String art_id) {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryOfferPriceList(art_id, page, 30, new MinerCallback<BaseResponseVo<List<OfferPriceBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<OfferPriceBean>>> call, Response<BaseResponseVo<List<OfferPriceBean>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        offerPriceTimes = response.body().getHead().getTotal_count();
                        mDataBinding.tvOfferPriceCount.setText(getString(R.string.office_price_count, offerPriceTimes));
                        List<OfferPriceBean> data = response.body().getBody();
                        if (data != null && data.size() > 0) {
                            if (page == 1) {
                                mOfferPriceList.clear();
                                mOfferPriceList = data;
                            } else {
                                mOfferPriceList.addAll(data);
                            }
                            mOfferPriceAdapter.setNewData(mOfferPriceList);
                            page++;
                        } else {
                            if (page > 1) {
                                mOfferPriceAdapter.loadMoreEnd();
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<OfferPriceBean>>> call, Response<BaseResponseVo<List<OfferPriceBean>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}
