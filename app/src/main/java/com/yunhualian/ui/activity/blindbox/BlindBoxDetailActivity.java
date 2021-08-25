package com.yunhualian.ui.activity.blindbox;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yunhualian.R;
import com.yunhualian.adapter.BlindBoxDetailCardAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.databinding.ActivityBlindBoxDetailBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.BlindBoxCheckVO;
import com.yunhualian.entity.BlindBoxOpenVo;
import com.yunhualian.entity.BlindBoxOrderCheck;
import com.yunhualian.entity.BlindBoxVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.user.CreateOrderForBlindBoxActivity;
import com.yunhualian.ui.activity.user.MyHomePageActivity;
import com.yunhualian.widget.BlindBoxOpenPop;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BlindBoxDetailActivity extends BaseActivity<ActivityBlindBoxDetailBinding> {

    List<BlindBoxVo.CardGroupsBean> sellingArtVoList;
    BlindBoxDetailCardAdapter adapter;
    BlindBoxOpenPop blindBoxOpenPop;
    BlindBoxVo blindBoxVo;
    List<BlindBoxCheckVO> hasBuyedBoxes;
    private BlindBoxCheckVO oneBlindBoxCheckVO;
    private BlindBoxCheckVO tenBlindBoxCheckVO;
    private boolean hasOneBlindBox = false;
    private boolean hasTenBlindBox = false;
    private static final int ONE = 1;
    private static final int TEN = 10;
    private static final String BOX = "box";

    @Override
    public int getLayoutId() {
        return R.layout.activity_blind_box_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initPage() {
        sellingArtVoList = new ArrayList<>();
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleString = blindBoxVo.getTitle() != null ? blindBoxVo.getTitle() : getString(R.string.title_detail);
        mToolBarOptions.rightTextString = R.string.blind_box_open_record;
        check();
        if (blindBoxVo.getBackground_color() == BigDecimal.ONE.intValue())
            initTextColor();
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        mDataBinding.mAppBarLayoutAv.mToolbar
                .findViewById(R.id.txt_right)
                .setOnClickListener(v -> openHistory());

        if (!TextUtils.isEmpty(blindBoxVo.getApp_background_img_path())) {
            setBackground(blindBoxVo.getApp_background_img_path());
        }

        if (!TextUtils.isEmpty(blindBoxVo.getApp_background_1x_img_path())) {
            set1xBackground(blindBoxVo.getApp_background_1x_img_path());
        }
        if (!TextUtils.isEmpty(blindBoxVo.getApp_background_10x_img_path())) {
            set10xBackground(blindBoxVo.getApp_background_10x_img_path());
        }
        adapter = new BlindBoxDetailCardAdapter(sellingArtVoList);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(ExtraConstant.DEFAULT_SPAN_COUNT, StaggeredGridLayoutManager.VERTICAL);
        mDataBinding.itemList.setLayoutManager(staggeredGridLayoutManager);
        mDataBinding.parentLayout.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            staggeredGridLayoutManager.invalidateSpanAssignments();
        });
        mDataBinding.itemList.setAdapter(adapter);
        blindBoxOpenPop = new BlindBoxOpenPop(this, (view, position) -> {
            blindBoxOpenPop.dismiss();
            startActivity(MyHomePageActivity.class);
        });
        blindBoxOpenPop.setBackgroundDrawable(null);
        blindBoxOpenPop.setFocusable(false);
        blindBoxOpenPop.setOutsideTouchable(false);
        blindBoxOpenPop.setAnimationStyle(Animation.RELATIVE_TO_PARENT);
        mDataBinding.openOnce.setOnClickListener(v -> {
            if (hasOneBlindBox) {
                //开启盲盒
                openBlindBox(oneBlindBoxCheckVO.getSn());
            } else performPay(ONE);
        });
        mDataBinding.openTen.setOnClickListener(v -> {
            if (hasTenBlindBox) {
                //开启盲盒
                openBlindBox(tenBlindBoxCheckVO.getSn());
            } else performPay(TEN);

        });
        initPageData();
    }

    @Override
    public void initView() {
        blindBoxVo = (BlindBoxVo) getIntent().getSerializableExtra(BOX);
        if (blindBoxVo != null)
            initPage();
        else {
            String id = getIntent().getStringExtra("id");
            if (!TextUtils.isEmpty(id))
                blindBoxDetail(id);
        }
    }

    private void setBackground(String url) {
        Glide.with(BlindBoxDetailActivity.this).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                Drawable drawable = new BitmapDrawable(bitmap);
                mDataBinding.parentLayout.setBackground(drawable);
            }
        });
    }

    private void set1xBackground(String url) {
        Glide.with(BlindBoxDetailActivity.this).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                Drawable drawable = new BitmapDrawable(bitmap);
                mDataBinding.openOnce.setBackground(drawable);
            }
        });
    }

    private void set10xBackground(String url) {
        Glide.with(BlindBoxDetailActivity.this).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                Drawable drawable = new BitmapDrawable(bitmap);
                mDataBinding.openTen.setBackground(drawable);
            }
        });
    }

    private void initTextColor() {
        mDataBinding.boxName.setTextColor(getColor(R.color._101010));
        mDataBinding.boxDesc.setTextColor(getColor(R.color._101010));

        mDataBinding.awardTitle.setTextColor(getColor(R.color._101010));

        mDataBinding.ruleProfile.setTextColor(getColor(R.color._101010));

        mDataBinding.ruleTitle.setTextColor(getColor(R.color._101010));
    }

    /*去支付*/
    private void goPayPage(int amount) {
        Bundle bundle = new Bundle();
        bundle.putString(CreateOrderForBlindBoxActivity.BUY_AMOUNT, String.valueOf(amount));
        bundle.putSerializable(CreateOrderForBlindBoxActivity.BLIND_BOX_INFO, blindBoxVo);
        startActivity(CreateOrderForBlindBoxActivity.class, bundle);
    }

    /*检查订单是否可购买*/
    private void performPay(int amount) {
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> params = new HashMap<>();
        params.put("box_id", String.valueOf(blindBoxVo.getId()));
        params.put("amount", String.valueOf(amount));
        RequestManager.instance().blindBoxOrderCheck(params, new MinerCallback<BaseResponseVo<BlindBoxOrderCheck>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<BlindBoxOrderCheck>> call, Response<BaseResponseVo<BlindBoxOrderCheck>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body().getBody().isAllow()) {
                        goPayPage(amount);
                    } else {
                        ToastUtils.showShort(getString(R.string.blind_box_transfering_tips));
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<BlindBoxOrderCheck>> call, Response<BaseResponseVo<BlindBoxOrderCheck>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private void blindBoxDetail(String id) {
        showLoading(getString(R.string.progress_loading));
        RequestManager.instance().blindBoxDetail(id, new MinerCallback<BaseResponseVo<BlindBoxVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<BlindBoxVo>> call, Response<BaseResponseVo<BlindBoxVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    blindBoxVo = response.body().getBody();
                    initPage();
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<BlindBoxVo>> call, Response<BaseResponseVo<BlindBoxVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private void initPageData() {
        if (blindBoxVo == null) return;
        sellingArtVoList = blindBoxVo.getCard_groups();
        Glide.with(this).load(blindBoxVo.getApp_img_path()).into(mDataBinding.mainImag);

        Glide.with(this).load(blindBoxVo.getApp_cover_img_path()).into(mDataBinding.bottomBackground);
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

    private void initBuyButton() {
        if (hasBuyedBoxes.size() > 0) {
            for (BlindBoxCheckVO blindBoxCheckVO : hasBuyedBoxes) {
                if (blindBoxCheckVO.getTotal() == BigDecimal.ONE.intValue()) {
                    hasOneBlindBox = true;
                    oneBlindBoxCheckVO = blindBoxCheckVO;
                    mDataBinding.openOnce.setText(getString(R.string.blindbox_open_x1));
                } else {
                    tenBlindBoxCheckVO = blindBoxCheckVO;
                    hasTenBlindBox = true;
                    mDataBinding.openTen.setText(getString(R.string.blindbox_open_x10));
                }
            }
        }
    }

    /*打开盲盒*/
    private void openBlindBox(String sn) {
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> params = new HashMap<>();
        params.put("sn", sn);
        RequestManager.instance().openBlindBoxs(params, new MinerCallback<BaseResponseVo<List<BlindBoxOpenVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<BlindBoxOpenVo>>> call, Response<BaseResponseVo<List<BlindBoxOpenVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getBody() != null) {
                            if (response.body().getBody().size() != 0) {
                                blindBoxOpenPop.setLists(response.body().getBody());
                                blindBoxOpenPop.showAtLocation(mDataBinding.getRoot(), Gravity.CENTER, BigDecimal.ZERO.intValue(), BigDecimal.ZERO.intValue());
                                check();
                            }
                        } else ToastUtils.showShort(getString(R.string.blind_box_failed_tips));
                    } else
                        ToastUtils.showShort(getString(R.string.blind_box_failed_tips));

                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<BlindBoxOpenVo>>> call, Response<BaseResponseVo<List<BlindBoxOpenVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    /*检查是否有已购买未打开盲盒*/
    private void check() {
        showLoading(getString(R.string.progress_loading));
        HashMap<String, String> params = new HashMap<>();
        params.put("box_id", String.valueOf(blindBoxVo.getId()));
        RequestManager.instance().checkBlindBoxs(params, new MinerCallback<BaseResponseVo<List<BlindBoxCheckVO>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<BlindBoxCheckVO>>> call, Response<BaseResponseVo<List<BlindBoxCheckVO>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        hasBuyedBoxes = response.body().getBody();
                    if (hasBuyedBoxes == null || hasBuyedBoxes.size() == 0) {
                        hasOneBlindBox = false;
                        hasTenBlindBox = false;
                        mDataBinding.openOnce.setText(R.string.blindbox_open_1);
                        mDataBinding.openTen.setText(R.string.blindbox_open_10);
                    } else {
                        hasBuyedBoxes = response.body().getBody();
                        initBuyButton();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<BlindBoxCheckVO>>> call, Response<BaseResponseVo<List<BlindBoxCheckVO>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

}