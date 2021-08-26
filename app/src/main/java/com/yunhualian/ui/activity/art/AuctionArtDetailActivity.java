package com.yunhualian.ui.activity.art;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.tools.ToastManage;
import com.yunhualian.R;
import com.yunhualian.adapter.ArtDetailImgAdapter;
import com.yunhualian.adapter.OfferPriceListAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.databinding.ActivityAuctionArtDetailBinding;
import com.yunhualian.entity.AccountVo;
import com.yunhualian.entity.AuctionArtVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.EventBusMessageEvent;
import com.yunhualian.entity.OfferPriceBean;
import com.yunhualian.entity.PayResyltVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.AuctionRuleActivity;
import com.yunhualian.ui.activity.OfferPriceListActivity;
import com.yunhualian.ui.activity.ShowNetBigImgActivity;
import com.yunhualian.ui.activity.user.CreateAuctionOrderActivity;
import com.yunhualian.ui.activity.user.CreateOrderActivity;
import com.yunhualian.ui.activity.user.MyHomePageActivity;
import com.yunhualian.ui.activity.user.UserHomePageActivity;
import com.yunhualian.ui.x5.WebViewActivity;
import com.yunhualian.ui.x5.X5WebViewActivity;
import com.yunhualian.ui.x5.X5WebViewForAliPayActivity;
import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.DisplayUtils;
import com.yunhualian.utils.FileHelper;
import com.yunhualian.utils.SharedPreUtils;
import com.yunhualian.utils.ToastManager;
import com.yunhualian.widget.BasePopupWindow;
import com.yunhualian.widget.ConfirmOrCancelPopwindow;
import com.yunhualian.widget.UploadSuccessPopUpWindow;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.woblog.android.downloader.DownloadService;
import cn.woblog.android.downloader.callback.DownloadListener;
import cn.woblog.android.downloader.callback.DownloadManager;
import cn.woblog.android.downloader.domain.DownloadInfo;
import cn.woblog.android.downloader.exception.DownloadException;
import retrofit2.Call;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class AuctionArtDetailActivity extends BaseActivity<ActivityAuctionArtDetailBinding> implements View.OnClickListener {

    UploadSuccessPopUpWindow uploadSuccessPopUpWindow;
    ConfirmOrCancelPopwindow confirmOrCancelPopwindow;

    public static final String ART_KEY = "art";
    public static final String GIF = "gif";
    public static final String ART_ID = "id";
    int totalAmount;
    List<String> lists = new ArrayList<>();
    private int selectPostioton = 0;
    AuctionArtVo sellingArtVo;
    String art_id;
    String path;//下载路径
    String request_art_id;//部分页面传id
    boolean fistLoad = false;
    private int clickPosition = 0;
    private PopupWindow mZhengshuPopwinow;
    private PopupWindow mDepositPopwindow;
    private PopupWindow mOfferPricePopwindow;
    private List<String> artDetailUrls = new ArrayList<>();
    private CountDownTimer mCountDownTimer;
    private String payType;
    private String offerPriceTimes;
    private OfferPriceListAdapter mOfferPriceAdapter;
    private List<OfferPriceBean> mOfferPriceList;

    private PopupWindow mPasswordPopwindow;
    private String passwd;

    @Override
    public int getLayoutId() {
        return R.layout.activity_auction_art_detail;
    }

    @Override
    public void initPresenter() {

    }

    private String getTv(long l) {
        if (l >= 10) {
            return l + "";
        } else {
            return "0" + l;//小于10,,前面补位一个"0"
        }
    }

    private void initCountTimer(long millSeconds) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = new CountDownTimer(millSeconds, 1000) {
            @Override
            public void onTick(long seconds) {

                long hours = seconds / (3600 * 1000);            //转换小时
                seconds = seconds % (3600 * 1000);
                long minutes = seconds / (60 * 1000);            //转换分钟
                seconds = seconds % (60 * 1000);
                seconds = seconds / 1000;                       //转换秒钟

                mDataBinding.tvCountHour.setText(getTv(hours));
                mDataBinding.tvCountMinute.setText(getTv(minutes));
                mDataBinding.tvCountSecond.setText(getTv(seconds));
            }

            @Override
            public void onFinish() {
                mCountDownTimer.cancel();
                requestArtInfo();
            }
        };
        mCountDownTimer.start();
    }

    @Override
    public void initView() {
//        EventBus.getDefault().register(this);
        fistLoad = true;
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.title_detail;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        sellingArtVo = (AuctionArtVo) getIntent().getExtras().getSerializable(ART_KEY);
        request_art_id = String.valueOf(getIntent().getIntExtra(ART_ID, 0));

        requestArtInfo(); //获取拍卖艺术品相关信息
        getOfferPriceList(false, request_art_id); //获取出价记录列表

        mDataBinding.buyNow.setOnClickListener(this);
        mDataBinding.zan.setOnClickListener(this);
        mDataBinding.cai.setOnClickListener(this);
        mDataBinding.collect.setOnClickListener(this);
        mDataBinding.goHomePage.setOnClickListener(this);
        mDataBinding.imgZhengshu.setOnClickListener(this);
        mDataBinding.imgPlay.setOnClickListener(this);
        mDataBinding.imgVideo.setOnClickListener(this);
        mDataBinding.rlOfferPrice.setOnClickListener(this);
        initZhengShuPopwindow();
        initPasswordPopwindow();
        mOfferPriceList = new ArrayList<>();
        mOfferPriceAdapter = new OfferPriceListAdapter(this, mOfferPriceList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDataBinding.rvOfferPrice.setLayoutManager(layoutManager);
        mOfferPriceAdapter.setEmptyView(R.layout.layout_offer_price_list_empty, mDataBinding.rvOfferPrice);
        mDataBinding.rvOfferPrice.setAdapter(mOfferPriceAdapter);
    }

    private void initArtDetails() {
        if (!artDetailUrls.isEmpty()) {
            ArtDetailImgAdapter artDetailImgAdapter = new ArtDetailImgAdapter(artDetailUrls, this);
            mDataBinding.artDetails.setLayoutManager(new LinearLayoutManager(this));
            mDataBinding.artDetails.setAdapter(artDetailImgAdapter);
        }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void Event(EventBusMessageEvent mEventBusMessageEvent) {
//        if (null != mEventBusMessageEvent && !TextUtils.isEmpty(mEventBusMessageEvent.getmMessage())) {
//            if (TextUtils.equals(ExtraConstant.EVENT_SELL_SUCCESS, mEventBusMessageEvent.getmMessage())) {
//                showPopWindow();
//            }
//        }
//    }
//
//    private void showPopWindow() {
//        uploadSuccessPopUpWindow = new UploadSuccessPopUpWindow(AuctionArtDetailActivity.this, new UploadSuccessPopUpWindow.OnBottomTextviewClickListener() {
//            @Override
//            public void onCancleClick() {
//                uploadSuccessPopUpWindow.dismiss();
//            }
//
//            @Override
//            public void onPerformClick() {
//                uploadSuccessPopUpWindow.dismiss();
////                startActivity(CustomerServiceActivity.class);
//            }
//        });
//        uploadSuccessPopUpWindow.setOneKey(true);
//        uploadSuccessPopUpWindow.setConfirmText(getString(R.string.confirm));
//        uploadSuccessPopUpWindow.setContent(getString(R.string.text_sell_tips));
//        uploadSuccessPopUpWindow.showAtLocation(mDataBinding.parentLayout, Gravity.CENTER, 0, 0);
//        EventBus.getDefault().removeAllStickyEvents();
//    }

    private void initZhengShuPopwindow() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_zhengshu_layout, null);
        TextView mNftNameTv = contentView.findViewById(R.id.tv_nft_name);
        TextView mNftThemeTv = contentView.findViewById(R.id.tv_nft_theme);
        TextView mNftCountTv = contentView.findViewById(R.id.tv_nft_count);
        TextView mNftAddressTv = contentView.findViewById(R.id.tv_nft_address);
        RelativeLayout mNftCloseBtn = contentView.findViewById(R.id.layout_close);
        if (sellingArtVo != null) {
            if (sellingArtVo.getArt() != null) {
                if (!TextUtils.isEmpty(sellingArtVo.getArt().getName())) {
                    mNftNameTv.setText(getString(R.string.nft_name, sellingArtVo.getArt().getName()));
                }
                if (YunApplication.getArtThemeVoList() != null && !YunApplication.getArtThemeVoList().isEmpty()) {
                    for (int i = 0; i < YunApplication.getArtThemeVoList().size(); i++) {
                        if (YunApplication.getArtThemeVoList().get(i).getId() == sellingArtVo.getArt().getCategory_id()) {
                            mNftThemeTv.setText(getString(R.string.nft_theme, YunApplication.getArtThemeVoList().get(i).getTitle()));
                        }
                    }
                }
                mNftCountTv.setText(getString(R.string.nft_count, String.valueOf(sellingArtVo.getArt().getTotal_amount())));
                if (!TextUtils.isEmpty(sellingArtVo.getArt().getItem_hash())) {
                    mNftAddressTv.setText(sellingArtVo.getArt().getItem_hash());
                }
            }
        }
        mZhengshuPopwinow = new BasePopupWindow(this);
        mZhengshuPopwinow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mZhengshuPopwinow.setContentView(contentView);
        mZhengshuPopwinow.setOutsideTouchable(false);
        mZhengshuPopwinow.setTouchable(true);
        mZhengshuPopwinow.setAnimationStyle(R.style.mypopwindow_anim_style);
        mNftCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mZhengshuPopwinow.dismiss();
            }
        });
    }

    private void initPayDepositWindow(String deposit, String remains) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_select_payway_layout, null);
        TextView remainsTv = contentView.findViewById(R.id.tv_remains_value);
        TextView needpayTv = contentView.findViewById(R.id.tv_need_pay);
        TextView payBtn = contentView.findViewById(R.id.confirm);

        CheckBox remainsCheck = contentView.findViewById(R.id.check_remain);
        CheckBox wxCheck = contentView.findViewById(R.id.check_weichatPay);
        CheckBox aliCheck = contentView.findViewById(R.id.check_aliPay);

        RelativeLayout remainLayout = contentView.findViewById(R.id.remainLayout);
        RelativeLayout zfbLayout = contentView.findViewById(R.id.aPayLayout);
        RelativeLayout wxLayout = contentView.findViewById(R.id.weiPayLayout);

        remainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remainsCheck.setChecked(true);
            }
        });
        zfbLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aliCheck.setChecked(true);
            }
        });
        wxLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wxCheck.setChecked(true);
            }
        });

        ImageView closeImg = contentView.findViewById(R.id.img_close);
        mDepositPopwindow = new BasePopupWindow(this);
        mDepositPopwindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mDepositPopwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mDepositPopwindow.setContentView(contentView);
        mDepositPopwindow.setOutsideTouchable(false);
        mDepositPopwindow.setTouchable(true);
        mDepositPopwindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        remainsTv.setText(getString(R.string.account_remain_v, remains));
        needpayTv.setText("¥" + deposit);

        if (Double.parseDouble(remains) >= Double.parseDouble(deposit)) {
            payType = "account";
            remainsCheck.setChecked(true);
        } else {
            payType = "wepay";
            wxCheck.setChecked(true);
        }

        remainsCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (Double.parseDouble(remains) < Double.parseDouble(deposit)) {
                    remainsCheck.setChecked(false);
                    com.blankj.utilcode.util.ToastUtils.showShort("账户余额不足");
                    return;
                }
                if (isChecked) {
                    payType = "account";
                    wxCheck.setChecked(false);
                    aliCheck.setChecked(false);
                } else {
                    if (!wxCheck.isChecked() && !aliCheck.isChecked()) {
                        remainsCheck.setChecked(true);
                    }
                }
            }
        });

        wxCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    payType = "wepay";
                    remainsCheck.setChecked(false);
                    aliCheck.setChecked(false);
                } else {
                    if (!remainsCheck.isChecked() && !aliCheck.isChecked()) {
                        wxCheck.setChecked(true);
                    }
                }
            }
        });

        aliCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    payType = "alipay";
                    remainsCheck.setChecked(false);
                    wxCheck.setChecked(false);
                } else {
                    if (!remainsCheck.isChecked() && !wxCheck.isChecked()) {
                        aliCheck.setChecked(true);
                    }
                }
            }
        });
        closeImg.setOnClickListener(view -> {
            mDepositPopwindow.dismiss();
        });
        payBtn.setOnClickListener(view -> {
            mDepositPopwindow.dismiss();
            mPasswordPopwindow.showAtLocation(mDataBinding.parentLayout, Gravity.CENTER, 0, 0);
        });

    }

    private void initPasswordPopwindow() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_passwd_layout, null);
        ImageView closeBtn = contentView.findViewById(R.id.img_close);
        TextView confirmBtn = contentView.findViewById(R.id.confirm);
        TextView passwdHintTv = contentView.findViewById(R.id.tv_passwd_len_hint);
        EditText passwdEd = contentView.findViewById(R.id.ed_password);
        mPasswordPopwindow = new BasePopupWindow(this);
        mPasswordPopwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPasswordPopwindow.setContentView(contentView);
        mPasswordPopwindow.setOutsideTouchable(false);
        mPasswordPopwindow.setTouchable(true);
        mPasswordPopwindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        passwdEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (TextUtils.isEmpty(editable.toString())) {
                    passwdHintTv.setText("0/6");
                } else {
                    passwdHintTv.setText(editable.toString().length() + "/6");
                    passwd = editable.toString();
                }
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (passwd.equals(SharedPreUtils.getString(AuctionArtDetailActivity.this, SharedPreUtils.KEY_PIN))) {
                    mPasswordPopwindow.dismiss();
                    goToPayDeposit();
                } else {
                    com.blankj.utilcode.util.ToastUtils.showShort("密码错误");
                }
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwdEd.setText("");
                mPasswordPopwindow.dismiss();
            }
        });

    }

    //获取出价列表
    private void getOfferPriceList(boolean isShowOfferPriceWindow, String art_id) {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryOfferPriceList(art_id, 1, 30, new MinerCallback<BaseResponseVo<List<OfferPriceBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<OfferPriceBean>>> call, Response<BaseResponseVo<List<OfferPriceBean>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        offerPriceTimes = response.body().getHead().getTotal_count();
                        mOfferPriceList = response.body().getBody();
                        mDataBinding.tvOfferPriceCount.setText(offerPriceTimes + "次");
                        //展示前三条出价记录
                        if (mOfferPriceList.size() >= 3) {
                            mOfferPriceAdapter.setNewData(mOfferPriceList.subList(0, 3));
                        } else {
                            mOfferPriceAdapter.setNewData(mOfferPriceList);
                        }

                        if (isShowOfferPriceWindow) {
                            double offeredPrice = 0; //您已出价
                            double addPrice; //还需加价
                            double topPrice;
                            if (sellingArtVo.getCurrent_price() != null) {
                                topPrice = Double.parseDouble(sellingArtVo.getCurrent_price()); //表示当前商品最高价格
                            } else {
                                topPrice = Double.parseDouble(sellingArtVo.getStart_price()); //如果没有最高价（无人出价），则显示起拍价
                            }

                            if (mOfferPriceList != null && mOfferPriceList.size() > 0) {
                                offeredPrice = Double.parseDouble(sellingArtVo.getCurrent_user_highest_price());
                                addPrice = topPrice - offeredPrice + Double.parseDouble(sellingArtVo.getPrice_increment());
                            } else {
                                addPrice = 0; //如果没有出价记录，加价为0
                            }
                            initOfferPriceWindow(topPrice, offeredPrice, addPrice);
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

    private double getSumOfferPrice(List<OfferPriceBean> offerPriceList) {
        double sumOfferPrice = 0.0;
        for (int i = 0; i < offerPriceList.size(); i++) {
            try {
                sumOfferPrice += Double.parseDouble(offerPriceList.get(i).getPrice());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sumOfferPrice;
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
                                String remains = accounts.get(i).getBalance();
                                initPayDepositWindow(sellingArtVo.getDeposit_amount(), remains);
                                mDepositPopwindow.showAtLocation(mDataBinding.parentLayout, Gravity.BOTTOM, 0, 0);
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

    public void initBanner() {
        fistLoad = false;
        if (sellingArtVo == null) return;
        lists.clear();
        if (!TextUtils.isEmpty(sellingArtVo.getArt().getImg_main_file1().getUrl())) {
            lists.add(sellingArtVo.getArt().getImg_main_file1().getUrl());
        }
        if (!TextUtils.isEmpty(sellingArtVo.getArt().getImg_main_file2().getUrl())) {
            lists.add(sellingArtVo.getArt().getImg_main_file2().getUrl());
        }
        if (!TextUtils.isEmpty(sellingArtVo.getArt().getImg_main_file3().getUrl())) {
            lists.add(sellingArtVo.getArt().getImg_main_file3().getUrl());
        }
        if (lists.size() == 0) return;
        totalAmount = lists.size();
        mDataBinding.cursorTxt.setText("1/".concat("" + totalAmount));
        mDataBinding.banner.setIndicatorVisible(false);
        mDataBinding.banner.setPages(lists, BannerViewHolder::new);
        mDataBinding.largeAction.setOnClickListener(v -> {
            if (TextUtils.isEmpty(sellingArtVo.getArt().getLive2d_ipfs_zip_url()))
                showBigImg();
            else {
                String path = YunApplication.LIVE2D_CACHE_PATH
                        .concat(String.valueOf(sellingArtVo.getId()))
                        .concat("/" + sellingArtVo.getArt().getLive2d_file() + YunApplication.MODEL_PATH);
                File file = new File(path);
                boolean isHave = file.exists();
                if (isHave) {
                    openLive2dActivity(YunApplication.LIVE2D_CACHE_PATH
                            .concat(String.valueOf(sellingArtVo.getId())));
                } else
                    download2(sellingArtVo.getArt().getLive2d_ipfs_zip_url(), String.valueOf(sellingArtVo.getId()));
            }
        });

        mDataBinding.banner.addPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                selectPostioton = position;
                mDataBinding.cursorTxt.setText(position + 1 + "/" + totalAmount);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    /*初始化页面信息*/
    public void initPageData() {

        if (sellingArtVo == null || sellingArtVo.getArt() == null) return;
        if (!TextUtils.isEmpty(sellingArtVo.getArt().getResource_type())) {
            if (sellingArtVo.getArt().getResource_type().equals("4")) {
                if (!TextUtils.isEmpty(sellingArtVo.getArt().getImg_main_file2().getUrl()) && sellingArtVo.getArt().getImg_main_file2().getUrl().endsWith("mp4")) {
                    mDataBinding.imgPlay.setOnClickListener(this);
                    mDataBinding.layoutVideo.setVisibility(View.VISIBLE);
                    mDataBinding.layoutBanner.setVisibility(View.GONE);
                    Glide.with(this)
                            .load(sellingArtVo.getArt().getImg_main_file1().getUrl())
                            .skipMemoryCache(true).transition(withCrossFade())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mDataBinding.imgVideo);
                } else {
                    mDataBinding.layoutVideo.setVisibility(View.GONE);
                    mDataBinding.layoutBanner.setVisibility(View.VISIBLE);
                }
            } else {
                mDataBinding.layoutVideo.setVisibility(View.GONE);
                mDataBinding.layoutBanner.setVisibility(View.VISIBLE);
            }
        } else {
            mDataBinding.layoutVideo.setVisibility(View.GONE);
            mDataBinding.layoutBanner.setVisibility(View.VISIBLE);
        }

        if (sellingArtVo.getArt().getImg_detail_file1() != null) {
            if (!TextUtils.isEmpty(sellingArtVo.getArt().getImg_detail_file1().getUrl())) {
                artDetailUrls.add(sellingArtVo.getArt().getImg_detail_file1().getUrl());
            }
        }
        if (sellingArtVo.getArt().getImg_detail_file2() != null) {
            if (!TextUtils.isEmpty(sellingArtVo.getArt().getImg_detail_file2().getUrl())) {
                artDetailUrls.add(sellingArtVo.getArt().getImg_detail_file2().getUrl());
            }
        }
        if (sellingArtVo.getArt().getImg_detail_file3() != null) {
            if (!TextUtils.isEmpty(sellingArtVo.getArt().getImg_detail_file3().getUrl())) {
                artDetailUrls.add(sellingArtVo.getArt().getImg_detail_file3().getUrl());
            }
        }
        initArtDetails();
        art_id = String.valueOf(sellingArtVo.getArt().getId()); //艺术品ID
        mDataBinding.pictureName.setText(sellingArtVo.getArt().getName());
        if (sellingArtVo.getCurrent_price() != null) {
            mDataBinding.tvCurPriceV.setText("¥ " + sellingArtVo.getCurrent_price());//当前价
        } else {
            mDataBinding.tvCurPriceV.setText("¥ " + sellingArtVo.getStart_price());//当前价
        }
        mDataBinding.tvStartAuctionPrice.setText(getString(R.string.auction_start_price_, sellingArtVo.getStart_price()));//起拍价
        mDataBinding.tvAuctionPieces.setText(getString(R.string.auction_pieces_, String.valueOf(sellingArtVo.getAmount())));//拍卖份数
        mDataBinding.tvAuctionProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(AuctionRuleActivity.class);
            }
        });
        mDataBinding.centifyAddr.setText(getString(R.string.nft_address, sellingArtVo.getArt().getItem_hash()));
        String royalty = sellingArtVo.getArt().getRoyalty() == null ? "0" : sellingArtVo.getArt().getRoyalty().toPlainString();
        mDataBinding.rotailDate.setText(getString(R.string.royalty_date, sellingArtVo.getArt().getRoyalty_expired_at() == 0 ?
                sellingArtVo.getArt().getRoyalty() == null ? "" : "永久" :
                DateUtil.dateToStringWithZhYear(sellingArtVo.getArt().getRoyalty_expired_at() * 1000)));
        mDataBinding.rotailRate.setText(getString(R.string.royalty_rate, sellingArtVo.getArt().getRoyalty() == null ?
                "" :
                new BigDecimal(royalty).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString().concat("%")));
        if (!TextUtils.isEmpty(sellingArtVo.getArt().getAuthor().getDisplay_name()))
            mDataBinding.creatorName.setText(sellingArtVo.getArt().getAuthor().getDisplay_name());
        else mDataBinding.creatorName.setText(getString(R.string.no_display_name));

        mDataBinding.tvAuctionStartPrice.setText(getString(R.string.auction_start_price_, sellingArtVo.getStart_price()));
        mDataBinding.tvAuctionPriceUnit.setText(getString(R.string.auction_add_unit_, sellingArtVo.getPrice_increment()));
        mDataBinding.tvAuctionStartTime.setText(getString(R.string.auction_start_time_, DateUtil.dateToStringWithAllDot(sellingArtVo.getStart_time() * 1000)));
        mDataBinding.tvAuctionEndTime.setText(getString(R.string.auction_end_time_, DateUtil.dateToStringWithAllDot(sellingArtVo.getEnd_time() * 1000)));
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.icon_default_head);
        Glide.with(this).load(sellingArtVo.getArt().getAuthor().getAvatar().getUrl()).apply(requestOptions).into(mDataBinding.headImg);
        mDataBinding.creatorProfile.setText(sellingArtVo.getArt().getAuthor().getDesc());

        if (sellingArtVo.getArt().isDisliked_by_me()) {
            Drawable top = getResources().getDrawable(R.mipmap.icon_cai_);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.cai.setCompoundDrawables(null, top, null, null);
        } else {
            Drawable top = getResources().getDrawable(R.mipmap.icon_cai);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.cai.setCompoundDrawables(null, top, null, null);
        }

        if (sellingArtVo.getArt().isLiked_by_me()) {
            Drawable top = getResources().getDrawable(R.mipmap.icon_zan_);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.zan.setCompoundDrawables(null, top, null, null);
        } else {
            Drawable top = getResources().getDrawable(R.mipmap.icon_zan);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.zan.setCompoundDrawables(null, top, null, null);

        }

        if (sellingArtVo.getArt().isFavorite_by_me()) {
            Drawable top = getResources().getDrawable(R.mipmap.icon_collect_);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.collect.setCompoundDrawables(null, top, null, null);
        } else {
            Drawable top = getResources().getDrawable(R.mipmap.icon_collect);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.collect.setCompoundDrawables(null, top, null, null);

        }
        if (fistLoad)
            initBanner();
        mDataBinding.cai.setText(getString(R.string.text_cai_amount, String.valueOf(sellingArtVo.getArt().getDislike_count())));
        mDataBinding.zan.setText(getString(R.string.text_zan_amount, String.valueOf(sellingArtVo.getArt().getLiked_count())));
        mDataBinding.signAmount.setText(getString(R.string.sign_amounts, String.valueOf(sellingArtVo.getArt().getTrades_count())));
        mDataBinding.createAmount.setText(getString(R.string.create_amount, String.valueOf(sellingArtVo.getArt().getTotal_amount())));
        if (TextUtils.isEmpty(sellingArtVo.getArt().getDetails())) {
            mDataBinding.artAppreciationLayout.setVisibility(View.GONE);
        } else
            mDataBinding.artAppreciation.setText(sellingArtVo.getArt().getDetails());
        initBtnStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void initBtnStatus() {
        if (sellingArtVo.isIs_owner()) {
            //持有者为开始可以取消拍卖
            mDataBinding.buyNow.setText("取消拍卖");
            if (sellingArtVo.isCan_cancel() && sellingArtVo.getServer_timestamp() < sellingArtVo.getEnd_time()) {
                mDataBinding.buyNow.setClickable(true);
                mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_red));
            } else {
                mDataBinding.buyNow.setClickable(false);
                mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_gray));
            }
            if (sellingArtVo.getServer_timestamp() < sellingArtVo.getStart_time()) {
                //未开始
                mDataBinding.tvAuctionStatus.setText("距开始");
                long countDownTime = sellingArtVo.getStart_time() - sellingArtVo.getServer_timestamp();
                initCountTimer(countDownTime * 1000);
                mDataBinding.rlAuctionCountTime.setBackground(ContextCompat.getDrawable(this, R.mipmap.bg_auction_yet));
            } else if (sellingArtVo.getServer_timestamp() > sellingArtVo.getEnd_time()) {
                //已结束
                mDataBinding.tvAuctionStatus.setText("距结束");
                mDataBinding.tvCountHour.setText("00");
                mDataBinding.tvCountMinute.setText("00");
                mDataBinding.tvCountSecond.setText("00");
                mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_gray));
                mDataBinding.rlAuctionCountTime.setBackground(ContextCompat.getDrawable(this, R.mipmap.bg_auction_end));
            } else {
                //进行中
                mDataBinding.tvAuctionStatus.setText("距结束");
                long countDownTime = sellingArtVo.getEnd_time() - sellingArtVo.getServer_timestamp();
                initCountTimer(countDownTime * 1000);
                mDataBinding.rlAuctionCountTime.setBackground(ContextCompat.getDrawable(this, R.mipmap.bg_auction_ing));
            }
        } else {
            if (sellingArtVo.getServer_timestamp() < sellingArtVo.getStart_time()) {
                //未开始
                mDataBinding.buyNow.setText("未开始");
                mDataBinding.tvAuctionStatus.setText("距开始");
                long countDownTime = sellingArtVo.getStart_time() - sellingArtVo.getServer_timestamp();
                initCountTimer(countDownTime * 1000);
                mDataBinding.rlAuctionCountTime.setBackground(ContextCompat.getDrawable(this, R.mipmap.bg_auction_yet));
                mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_gray));
            } else if (sellingArtVo.getServer_timestamp() > sellingArtVo.getEnd_time()) {
                //已结束
                mDataBinding.tvAuctionStatus.setText("距结束");
                mDataBinding.tvCountHour.setText("00");
                mDataBinding.tvCountMinute.setText("00");
                mDataBinding.tvCountSecond.setText("00");
                mDataBinding.rlAuctionCountTime.setBackground(ContextCompat.getDrawable(this, R.mipmap.bg_auction_end));

                if (sellingArtVo.isIs_settlement()) {
                    mDataBinding.buyNow.setText("正在结算中");
                    mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_gray));
                } else {
                    if (sellingArtVo.getBuyer() != null) {
                        if (YunApplication.getmUserVo().getId() == sellingArtVo.getBuyer().getId()) {
                            if (sellingArtVo.isIs_paying()) {
                                mDataBinding.buyNow.setText("正在支付中");
                                mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_gray));
                            } else {
                                if (sellingArtVo.isBuyer_paid()) {
                                    mDataBinding.buyNow.setText("中标已支付");
                                    mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_gray));
                                } else {
                                    if (sellingArtVo.getServer_timestamp() >= (sellingArtVo.getEnd_time() + sellingArtVo.getPay_timeout())) {
                                        mDataBinding.buyNow.setText("超时未支付");
                                        mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_gray));
                                    } else {
                                        mDataBinding.buyNow.setText("中标去支付");
                                        mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_red));
                                    }
                                }
                            }
                        } else {
                            mDataBinding.buyNow.setText("已结束");
                            mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_gray));
                        }
                    } else {
                        mDataBinding.buyNow.setText("已结束");
                        mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_gray));
                    }
                }
            } else {
                //拍卖中
                mDataBinding.tvAuctionStatus.setText("距结束");
                mDataBinding.rlAuctionCountTime.setBackground(ContextCompat.getDrawable(this, R.mipmap.bg_auction_ing));
                long countDownTime = sellingArtVo.getEnd_time() - sellingArtVo.getServer_timestamp();
                initCountTimer(countDownTime * 1000);
                mDataBinding.buyNow.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_btn_red));
                if (sellingArtVo.isDeposit_paid()) {
                    mDataBinding.buyNow.setText("出价");
                } else {
                    mDataBinding.buyNow.setText(getString(R.string.pay_deposit, sellingArtVo.getDeposit_amount()));
                }
            }
        }

    }

    private void showBigImg() {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("url", (ArrayList<String>) lists);
        bundle.putInt("index", selectPostioton + 1);
        startActivity(ShowNetBigImgActivity.class, bundle);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buy_now:
                buyAction();
                break;
            case R.id.zan:

                if (sellingArtVo.getArt().isLiked_by_me()) {
                    cancelLike();
                } else like();

                break;
            case R.id.cai:
                if (sellingArtVo.getArt().isDisliked_by_me()) {
                    cancelDislike();
                } else disLike();
                break;
            case R.id.collect:
                if (sellingArtVo.getArt().isFavorite_by_me())
                    cancelCollect();
                else collect();
                break;
            case R.id.go_home_page:
                if (sellingArtVo.getArt().getAuthor().getId() == YunApplication.getmUserVo().getId()) {
                    startActivity(MyHomePageActivity.class);
                } else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(UserHomePageActivity.UID, sellingArtVo.getArt().getAuthor().getId());
                    startActivity(UserHomePageActivity.class, bundle1);
                }
                break;

            case R.id.img_zhengshu:
                mZhengshuPopwinow.showAtLocation(mDataBinding.parentLayout, Gravity.CENTER, 0, 0);
                break;

            case R.id.img_play:
            case R.id.img_video:
                if (!TextUtils.isEmpty(sellingArtVo.getArt().getImg_main_file2().getUrl())) {
                    if (sellingArtVo.getArt().getImg_main_file2().getUrl().endsWith("mp4")) {
                        try {
                            PictureSelector.create(AuctionArtDetailActivity.this).externalPictureVideo(sellingArtVo.getArt().getImg_main_file2().getUrl());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case R.id.rl_offer_price:
                Intent intent = new Intent(AuctionArtDetailActivity.this, OfferPriceListActivity.class);
                intent.putExtra("art_id", String.valueOf(sellingArtVo.getId()));
                startActivity(intent);
                break;
        }

    }

    /*根据不同状态跳不同页面*/
    private void buyAction() {
        String btnStatus = mDataBinding.buyNow.getText().toString();
        switch (btnStatus) {
            case "取消拍卖":
                showCancelDepositDialog();
                break;

            case "出价":
                getOfferPriceList(true, String.valueOf(sellingArtVo.getId()));
                break;

            case "中标去支付":
                Intent intent = new Intent(AuctionArtDetailActivity.this, CreateAuctionOrderActivity.class);
                intent.putExtra("id", String.valueOf(sellingArtVo.getId()));
                startActivity(intent);
                break;

            case "未开始":
            case "已结束":
            case "超时未支付":
            case "中标已支付":
            case "正在结算中":
            case "正在支付中":
                break;

            default:
                queryAccountInfo();
                break;
        }

    }

    public static class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;
        private RelativeLayout relativeLayout;
        private View view;
        private CardView cardView;

        @Override
        public View createView(Context context) {
            view = LayoutInflater.from(context).inflate(R.layout.banner_item_art_detail, null);
            mImageView = view.findViewById(R.id.banner_image);
            cardView = view.findViewById(R.id.cardView);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            Glide.with(context).clear(mImageView);
            RoundedCorners roundedCorners = new RoundedCorners(10);
            RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);

            Glide.with(context).asBitmap().load(data).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                    int height = DisplayUtils.px2dp(context, bitmap.getHeight());
                    int width = DisplayUtils.px2dp(context, bitmap.getWidth());
                    int imageViewWidth = DisplayUtils.px2dp(context, ScreenUtils.getScreenWidth());
                    int imageViewHeigt = 240;
                    BigDecimal width_ = new BigDecimal(String.valueOf(imageViewHeigt))
                            .divide(new BigDecimal(String.valueOf(height)), 2, RoundingMode.HALF_DOWN)
                            .multiply(new BigDecimal(String.valueOf(width)));
                    BigDecimal height_ = new BigDecimal(String.valueOf(imageViewWidth))
                            .divide(new BigDecimal(String.valueOf(width)), 2, RoundingMode.HALF_DOWN)
                            .multiply(new BigDecimal(String.valueOf(height)));
                    if (width > height) {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                DisplayUtils.dp2px(context, height_.floatValue()));
                        params.addRule(RelativeLayout.CENTER_IN_PARENT);
                        mImageView.setLayoutParams(params);
                    } else {
                        mImageView.setLayoutParams(new RelativeLayout.LayoutParams(DisplayUtils.dp2px(context, width_.floatValue()),
                                ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                }
            });
            Glide.with(context).load(data).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(mImageView);
        }


    }

    /*zan*/
    public void like() {
        HashMap<String, String> params = new HashMap<>();
        params.put("art_id", art_id);
        RequestManager.instance().like(art_id, params, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        sellingArtVo.setArt(response.body().getBody());
                        initPageData();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    /*取消点赞*/
    public void cancelLike() {
        HashMap<String, String> params = new HashMap<>();
        params.put("art_id", art_id);
        RequestManager.instance().canclelike(art_id, params, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        sellingArtVo.setArt(response.body().getBody());
                        initPageData();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void disLike() {

        HashMap<String, String> params = new HashMap<>();
        params.put("art_id", art_id);
        RequestManager.instance().dislike(art_id, params, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        sellingArtVo.setArt(response.body().getBody());
                        initPageData();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void cancelDislike() {
        HashMap<String, String> params = new HashMap<>();
        params.put("art_id", art_id);
        RequestManager.instance().cancleDislike(art_id, params, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        sellingArtVo.setArt(response.body().getBody());
                        initPageData();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void collect() {
        HashMap<String, String> params = new HashMap<>();
        params.put("art_id", art_id);
        RequestManager.instance().collect(art_id, params, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        sellingArtVo.setArt(response.body().getBody());
                        initPageData();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void cancelCollect() {
        HashMap<String, String> params = new HashMap<>();
        params.put("art_id", art_id);
        RequestManager.instance().discollect(art_id, params, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        sellingArtVo.setArt(response.body().getBody());
                        initPageData();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    private void download2(String downLoadUrl, String resouceId) {
        DownloadManager downloadManager = DownloadService.getDownloadManager(this);
        String dir = YunApplication.LIVE2D_CACHE_PATH.concat(resouceId.concat(".zip"));
        File file = new File(dir);
        file.getParentFile().mkdirs();
        path = file.getAbsolutePath();
        DownloadInfo downloadInfo = new DownloadInfo.Builder().setUrl(downLoadUrl).setPath(file.getAbsolutePath()).build();

        downloadInfo.setDownloadListener(new DownloadListener() {

            @Override
            public void onStart() {
                showLoading(getString(R.string.text_downloading));
            }

            @Override
            public void onWaited() {
            }

            @Override
            public void onPaused() {
            }

            @Override
            public void onDownloading(long progress, long size) {
                LogUtils.e(progress);
            }

            @Override
            public void onRemoved() {
            }

            @Override
            public void onDownloadSuccess() {
                dismissLoading();
                String dir = YunApplication.LIVE2D_CACHE_PATH.concat(resouceId);
                if (FileHelper.unzip(path, dir)) {
                    ToastUtils.showLongToast(AuctionArtDetailActivity.this, getString(R.string.text_download_success));
                    openLive2dActivity(dir);
                } else {
                    ToastUtils.showLongToast(AuctionArtDetailActivity.this, "资源解压失败");
                }
            }

            @Override
            public void onDownloadFailed(DownloadException e) {
                e.printStackTrace();
                ToastUtils.showLongToast(AuctionArtDetailActivity.this, "资源加载失败");
            }
        });
        downloadManager.download(downloadInfo);
    }

    private void openLive2dActivity(String dir) {
        String path = dir.concat("/");
        String modelName = sellingArtVo.getArt().getLive2d_file().concat(YunApplication.MODEL_PATH);
        Bundle bundle = new Bundle();
        bundle.putString(ShowLiveActivity.PATH, path);
        bundle.putString(ShowLiveActivity.MODEL_NAME, modelName);
        bundle.putBoolean("is_from_detail", true);
        startActivity(ShowLiveActivity.class, bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void requestArtInfo() {
        showLoading(getString(R.string.progress_loading));
        RequestManager.instance().auctionArtInfo(request_art_id, new MinerCallback<BaseResponseVo<AuctionArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<AuctionArtVo>> call, Response<BaseResponseVo<AuctionArtVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        sellingArtVo = response.body().getBody();
                        initPageData();
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<AuctionArtVo>> call, Response<BaseResponseVo<AuctionArtVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private void showCancelDepositDialog() {
        confirmOrCancelPopwindow = new ConfirmOrCancelPopwindow(AuctionArtDetailActivity.this, new ConfirmOrCancelPopwindow.OnBottomTextviewClickListener() {
            @Override
            public void onCancleClick() {
                confirmOrCancelPopwindow.dismiss();
            }

            @Override
            public void onPerformClick() {
                confirmOrCancelPopwindow.dismiss();
                goToCancelAuction();
            }
        });
        confirmOrCancelPopwindow.setConfirmText("确定");
        confirmOrCancelPopwindow.setCancleText("取消");
        confirmOrCancelPopwindow.setContent(getString(R.string.cancel_auction_hint));
        confirmOrCancelPopwindow.showAtLocation(mDataBinding.parentLayout, Gravity.CENTER, 0, 0);
    }

    private void initOfferPriceWindow(double currentHighestPrice, double hasOfferedPrice, double addPrice) {
        View contentView = LayoutInflater.from(this).inflate(R.layout.pop_offer_price_layout, null);

        TextView currentPriceTv = contentView.findViewById(R.id.tv_current_price);
        TextView hasOfferPriceHintTv = contentView.findViewById(R.id.tv_offered_price_hint);
        TextView priceUnitTv = contentView.findViewById(R.id.tv_offering_price_hint);
        TextView confirmBtn = contentView.findViewById(R.id.confirm);
        ImageView closeBtn = contentView.findViewById(R.id.img_close);


        currentPriceTv.setText(getString(R.string.price_holder, String.valueOf(currentHighestPrice)));
        hasOfferPriceHintTv.setText(getString(R.string.has_offered_price, String.valueOf(hasOfferedPrice)));
        priceUnitTv.setText(getString(R.string.need_offered_price, String.valueOf(addPrice)));
        double needOfferPrice;
        if (addPrice == 0) {
            needOfferPrice = currentHighestPrice; //立即出价金额= 已出价 + 还需加价
        } else {
            needOfferPrice = hasOfferedPrice + addPrice; //立即出价金额= 已出价 + 还需加价
        }
        confirmBtn.setText(getString(R.string.offer_price_now, String.valueOf(needOfferPrice)));

        mOfferPricePopwindow = new BasePopupWindow(this);
        mOfferPricePopwindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mOfferPricePopwindow.setContentView(contentView);
        mOfferPricePopwindow.setOutsideTouchable(false);
        mOfferPricePopwindow.setTouchable(true);
        mOfferPricePopwindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        if (hasOfferedPrice != 0 && currentHighestPrice == hasOfferedPrice) {
            confirmBtn.setEnabled(false);
            confirmBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_line));
        } else {
            confirmBtn.setEnabled(true);
            confirmBtn.setBackgroundColor(ContextCompat.getColor(this, R.color._101010));
        }
        closeBtn.setOnClickListener(view -> {
            mOfferPricePopwindow.dismiss();
        });

        confirmBtn.setOnClickListener(view -> {
            offerPrice(needOfferPrice);
            mOfferPricePopwindow.dismiss();
        });

        mOfferPricePopwindow.showAtLocation(mDataBinding.parentLayout, Gravity.CENTER, 0, 0);
    }

    //取消拍卖
    private void goToCancelAuction() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().cancelAuction(String.valueOf(sellingArtVo.getId()), new MinerCallback<BaseResponseVo<AuctionArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<AuctionArtVo>> call, Response<BaseResponseVo<AuctionArtVo>> response) {
                if (response.isSuccessful()) {
                    finish();
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<AuctionArtVo>> call, Response<BaseResponseVo<AuctionArtVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    //支付保证金
    private void goToPayDeposit() {
        HashMap<String, String> params = new HashMap<>();
        params.put("auction_id", String.valueOf(sellingArtVo.getId()));
        params.put("pay_type", payType);
        params.put("order_from", "android");
        RequestManager.instance().payForDeposit(params, new MinerCallback<BaseResponseVo<PayResyltVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<PayResyltVo>> call, Response<BaseResponseVo<PayResyltVo>> response) {
                if (response.body() != null) {
                    if (payType.equals("account")) {
                        requestArtInfo();
                    } else {
                        if (response.body().getBody() != null) {
                            String url = response.body().getBody().getUrl();
                            String title = "支付";
                            Bundle bundle = new Bundle();
                            bundle.putString(WebViewActivity.TITLE, title);
                            bundle.putString(WebViewActivity.URL, url);
                            bundle.putString(WebViewActivity.TYPE, payType);
                            if (!TextUtils.isEmpty(url)) {
                                if (payType.equals(X5WebViewActivity.WECHAT)) {
                                    startActivity(X5WebViewActivity.class, bundle);
                                } else startActivity(X5WebViewForAliPayActivity.class, bundle);
                                finish();
                            } else
                                com.blankj.utilcode.util.ToastUtils.showShort("出错了");
                        }
                    }

                }
            }

            @Override
            public void onError(Call<BaseResponseVo<PayResyltVo>> call, Response<BaseResponseVo<PayResyltVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    //出价艺术品拍卖
    private void offerPrice(double price) {
        showLoading(R.string.progress_loading);
        HashMap<String, String> params = new HashMap<>();
        params.put("price", String.valueOf(price));
        RequestManager.instance().offerPrice(String.valueOf(sellingArtVo.getId()), params, new MinerCallback<BaseResponseVo<OfferPriceBean>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<OfferPriceBean>> call, Response<BaseResponseVo<OfferPriceBean>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    ToastManager.showShort("出价成功");
                    requestArtInfo();
                    getOfferPriceList(false, String.valueOf(sellingArtVo.getId()));
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<OfferPriceBean>> call, Response<BaseResponseVo<OfferPriceBean>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}