package com.yunhualian.ui.activity.blindbox;


import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.yunhualian.R;
import com.yunhualian.adapter.BlindBoxDetailCardAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityBlindBoxDetailBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.BlindBoxCheckVO;
import com.yunhualian.entity.BlindBoxVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.widget.BlindBoxOpenPop;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BlindBoxDetailActivity extends BaseActivity<ActivityBlindBoxDetailBinding> {

    List<BlindBoxVo.CardGroupsBean> sellingArtVoList;
    BlindBoxDetailCardAdapter adapter;
    BlindBoxOpenPop blindBoxOpenPop;
    BlindBoxVo blindBoxVo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_blind_box_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.title_detail;
        mToolBarOptions.rightTextString = R.string.blind_box_open_record;
        blindBoxVo = (BlindBoxVo) getIntent().getSerializableExtra("box");
        check();
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        mDataBinding.mAppBarLayoutAv.mToolbar
                .findViewById(R.id.txt_right)
                .setOnClickListener(v -> openHistory());

        adapter = new BlindBoxDetailCardAdapter(sellingArtVoList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mDataBinding.itemList.setLayoutManager(staggeredGridLayoutManager);
        mDataBinding.itemList.setAdapter(adapter);
//        getPopular(new HashMap<>());
        blindBoxOpenPop = new BlindBoxOpenPop(this, null, (view, position) -> {

        });
        blindBoxOpenPop.setBackgroundDrawable(null);
        blindBoxOpenPop.setFocusable(false);
        blindBoxOpenPop.setOutsideTouchable(false);
        blindBoxOpenPop.setAnimationStyle(Animation.RELATIVE_TO_PARENT);
        mDataBinding.openOnce.setOnClickListener(v -> {

        });
        mDataBinding.openTen.setOnClickListener(v -> {
            blindBoxOpenPop.showAtLocation(mDataBinding.getRoot(), Gravity.CENTER, 0, 0);
        });
        initPageData();
    }

    private void initPageData() {
        if (blindBoxVo == null) return;
        sellingArtVoList = blindBoxVo.getCard_groups();
        Glide.with(this).load(blindBoxVo.getImg_path()).into(mDataBinding.mainImag);
        mDataBinding.boxName.setText(blindBoxVo.getTitle());
        mDataBinding.boxDesc.setText(Html.fromHtml(blindBoxVo.getDesc()));
        adapter.setNewData(sellingArtVoList);
        mDataBinding.ruleProfile.setText(Html.fromHtml(blindBoxVo.getRule()));
    }

    private void openHistory() {
        Bundle bundle = new Bundle();
        bundle.putInt("id", blindBoxVo.getId());
        startActivity(BlindBoxRecordActivity.class, bundle);
    }

    public void getPopular(HashMap<String, String> params) {
        showLoading("加载中...");
        RequestManager.instance().querySelling(params, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                if (response.isSuccessful()) {
//                    sellingArtVoList = response.body().getBody();
//                    if (artBeanList.size() > 0)
//                    adapter.setNewData(sellingArtVoList);
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

    private void check() {
        showLoading("加载中...");
        HashMap<String, String> params = new HashMap<>();
        params.put("box_id", String.valueOf(blindBoxVo.getId()));
        RequestManager.instance().checkBlindBoxs(params, new MinerCallback<BaseResponseVo<BlindBoxCheckVO>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<BlindBoxCheckVO>> call, Response<BaseResponseVo<BlindBoxCheckVO>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
//                    sellingArtVoList = response.body().getBody();
//                    if (artBeanList.size() > 0)
//                    adapter.setNewData(sellingArtVoList);
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<BlindBoxCheckVO>> call, Response<BaseResponseVo<BlindBoxCheckVO>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private void order() {
        showLoading("加载中...");
        HashMap<String, String> params = new HashMap<>();
        RequestManager.instance().blindBoxOrders(params, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                if (response.isSuccessful()) {
//                    sellingArtVoList = response.body().getBody();
//                    if (artBeanList.size() > 0)
//                    adapter.setNewData(sellingArtVoList);
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }
}