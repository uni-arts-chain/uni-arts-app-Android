package com.yunhualian.ui.activity.blindbox;


import android.view.LayoutInflater;
import android.view.View;

import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityBlindBoxRecordBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.BlindBoxVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;

import org.web3j.crypto.Hash;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BlindBoxRecordActivity extends BaseActivity<ActivityBlindBoxRecordBinding> {

    List<BlindBoxVo.CardGroupsBean> sellingArtVoList;
    private int id;

    private View mEmptyView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_blind_box_record;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        id = getIntent().getIntExtra("id", 0);
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.blind_box_open_record;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        getBoxRecord();
    }

    public void getBoxRecord() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        showLoading("加载中...");
        RequestManager.instance().queryBoxRecord(params, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                    }

                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(this).inflate(R.layout.layout_entrust_empty, null);

        return mEmptyView;
    }
}