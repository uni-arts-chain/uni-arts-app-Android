package com.gammaray.ui.activity.blindbox;


import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.gammaray.R;
import com.gammaray.adapter.BlindBoxRecordAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityBlindBoxRecordBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.BlindBoxRecordVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;


import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BlindBoxRecordActivity extends BaseActivity<ActivityBlindBoxRecordBinding> {
    BlindBoxRecordAdapter blindBoxRecordAdapter;
    List<BlindBoxRecordVo> blindBoxRecordVoList;
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
        blindBoxRecordAdapter = new BlindBoxRecordAdapter(blindBoxRecordVoList);
        mDataBinding.list.setLayoutManager(new LinearLayoutManager(this));
        blindBoxRecordAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.list);
        mDataBinding.list.setAdapter(blindBoxRecordAdapter);
        getBoxRecord();

        mDataBinding.swipeRefresh.setOnRefreshListener(() -> {
            mDataBinding.swipeRefresh.setRefreshing(false);
            getBoxRecord();
        });
    }

    public void getBoxRecord() {
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        showLoading(getString(R.string.progress_loading));
        RequestManager.instance().queryBoxRecord(String.valueOf(id), params, new MinerCallback<BaseResponseVo<List<BlindBoxRecordVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<BlindBoxRecordVo>>> call, Response<BaseResponseVo<List<BlindBoxRecordVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getBody() != null) {
                            blindBoxRecordVoList = response.body().getBody();
                            blindBoxRecordAdapter.setNewData(blindBoxRecordVoList);
                        }
                    }

                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<BlindBoxRecordVo>>> call, Response<BaseResponseVo<List<BlindBoxRecordVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });

    }

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(this).inflate(R.layout.layout_entrust_empty, null);

        return mEmptyView;
    }
}