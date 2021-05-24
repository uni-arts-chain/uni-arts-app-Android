package com.yunhualian.ui.activity.blindbox;


import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.yunhualian.R;
import com.yunhualian.adapter.BlindBoxRecordAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityBlindBoxRecordBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.BlindBoxRecordVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;


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