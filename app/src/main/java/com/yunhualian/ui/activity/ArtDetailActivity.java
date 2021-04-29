package com.yunhualian.ui.activity;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jelly.mango.Mango;
import com.jelly.mango.MultiplexImage;
import com.lljjcoder.style.citylist.Toast.ToastUtils;
import com.yunhualian.R;
import com.yunhualian.adapter.ArtDetailOrderListAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityArtDetailBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.OrderAmountVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.user.CreateOrderActivity;
import com.yunhualian.ui.activity.user.UserHomePageActivity;
import com.yunhualian.utils.DialogManager;
import com.yunhualian.utils.FileHelper;
import com.yunhualian.utils.ZxingEncodingUtils;
import com.yunhualian.widget.LoadingPop;
import com.zhouwei.mzbanner.holder.MZViewHolder;


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
import jp.co.soramitsu.feature_account_impl.presentation.pincode.PinCodeAction;
import retrofit2.Call;
import retrofit2.Response;


public class ArtDetailActivity extends BaseActivity<ActivityArtDetailBinding> implements View.OnClickListener {

    public static final String ART_KEY = "art";
    public static final String GIF = "gif";
    //    PicDetailAdapter picDetailAdapter;
    int totalAmount;
    List<String> lists = new ArrayList<>();
    private int selectPostioton = 0;
    SellingArtVo sellingArtVo;
    String art_id;
    String path;//下载路径
    ArtDetailOrderListAdapter artDetailOrderListAdapter;
    private List<OrderAmountVo> artBeanList;
    private List<OrderAmountVo> defaultList;

    boolean showMoreFlag = false;

    @Override
    public int getLayoutId() {
        return R.layout.activity_art_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.title_detail;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        sellingArtVo = (SellingArtVo) getIntent().getExtras().getSerializable(ART_KEY);
        initBanner();
        initPageData();
        mDataBinding.copy.setOnClickListener(v -> {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData mClipData = ClipData.newPlainText("", "addr");
                    cm.setPrimaryClip(mClipData);
                }
        );
        createQrcode("");
        mDataBinding.buyNow.setOnClickListener(this);

        mDataBinding.zan.setOnClickListener(this);
        mDataBinding.cai.setOnClickListener(this);
        mDataBinding.collect.setOnClickListener(this);
        mDataBinding.goHomePage.setOnClickListener(this);
    }

    public void initBanner() {
        if (sellingArtVo == null) return;
        lists.clear();
        if (!TextUtils.isEmpty(sellingArtVo.getImg_main_file1().getUrl())) {
            lists.add(sellingArtVo.getImg_main_file1().getUrl());
        }
        if (!TextUtils.isEmpty(sellingArtVo.getImg_main_file2().getUrl())) {
            lists.add(sellingArtVo.getImg_main_file2().getUrl());
        }
        if (!TextUtils.isEmpty(sellingArtVo.getImg_main_file3().getUrl())) {
            lists.add(sellingArtVo.getImg_main_file3().getUrl());
        }
        if (lists.size() == 0) return;
        totalAmount = lists.size();
        mDataBinding.cursorTxt.setText("1/".concat("" + totalAmount));
        mDataBinding.banner.setPages(lists, BannerViewHolder::new);
        mDataBinding.largeAction.setOnClickListener(v -> {
            if (TextUtils.isEmpty(sellingArtVo.getLive2d_ipfs_zip_url()))
                showBigImg();
            else {
                String path = YunApplication.LIVE2D_CACHE_PATH
                        .concat(String.valueOf(sellingArtVo.getId()))
                        .concat("/" + sellingArtVo.getLive2d_file() + YunApplication.MODEL_PATH);
                File file = new File(path);
                boolean isHave = file.exists();
                if (isHave) {
                    openLive2dActivity(YunApplication.LIVE2D_CACHE_PATH
                            .concat(String.valueOf(sellingArtVo.getId())));
                } else
                    download2(sellingArtVo.getLive2d_ipfs_zip_url(), String.valueOf(sellingArtVo.getId()));
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

    public void initPageData() {

        if (sellingArtVo == null) return;
        art_id = String.valueOf(sellingArtVo.getId());
        mDataBinding.pictureName.setText(sellingArtVo.getName());
        mDataBinding.picturePrize.setText(getString(R.string.text_buy_amount, sellingArtVo.getPrice()));
        mDataBinding.centifyAddr.setText(getString(R.string.nft_address, sellingArtVo.getItem_hash()));
        artDetailOrderListAdapter = new ArtDetailOrderListAdapter(artBeanList);
        mDataBinding.orderAmount.setLayoutManager(new LinearLayoutManager(this));
        mDataBinding.orderAmount.setAdapter(artDetailOrderListAdapter);
        mDataBinding.creatorName.setText(sellingArtVo.getAuthor().getDisplay_name());
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.icon_default_head);
        Glide.with(this).load(sellingArtVo.getAuthor().getAvatar().getUrl()).apply(requestOptions).into(mDataBinding.headImg);
        mDataBinding.creatorProfile.setText(sellingArtVo.getAuthor().getDesc());

        if (sellingArtVo.isDisliked_by_me()) {
            Drawable top = getResources().getDrawable(R.mipmap.icon_cai_);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.cai.setCompoundDrawables(null, top, null, null);
        } else {
            Drawable top = getResources().getDrawable(R.mipmap.icon_cai);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.cai.setCompoundDrawables(null, top, null, null);
        }

        if (sellingArtVo.isLiked_by_me()) {
            Drawable top = getResources().getDrawable(R.mipmap.icon_zan_);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.zan.setCompoundDrawables(null, top, null, null);
        } else {
            Drawable top = getResources().getDrawable(R.mipmap.icon_zan);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.zan.setCompoundDrawables(null, top, null, null);

        }

        if (sellingArtVo.isFavorite_by_me()) {
            Drawable top = getResources().getDrawable(R.mipmap.icon_collect_);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.collect.setCompoundDrawables(null, top, null, null);
        } else {
            Drawable top = getResources().getDrawable(R.mipmap.icon_collect);
            top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
            mDataBinding.collect.setCompoundDrawables(null, top, null, null);

        }

        mDataBinding.cai.setText(getString(R.string.text_cai_amount, String.valueOf(sellingArtVo.getDislike_count())));
        mDataBinding.zan.setText(getString(R.string.text_zan_amount, String.valueOf(sellingArtVo.getLiked_count())));
        mDataBinding.signAmount.setText(getString(R.string.sign_amounts, String.valueOf(sellingArtVo.getTrades_count())));
        mDataBinding.showMoreList.setOnClickListener(v -> {
            if (!showMoreFlag) {
                if (artBeanList.size() > 0) {
                    artDetailOrderListAdapter.setNewData(artBeanList);
                    showMoreFlag = true;
                    rotation(180f);
                }
            } else {
                if (defaultList.size() > 0) {
                    artDetailOrderListAdapter.setNewData(defaultList);
                    showMoreFlag = false;
                    rotation(0f);
                }
            }

        });
        artDetailOrderListAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(CreateOrderActivity.ARTINFO, sellingArtVo);
            bundle.putSerializable(CreateOrderActivity.ORDERINFO, artBeanList.get(position));
            startActivity(CreateOrderActivity.class, bundle);
        });
        if (TextUtils.isEmpty(sellingArtVo.getDetails())) {
            mDataBinding.artAppreciationLayout.setVisibility(View.GONE);
        } else
            mDataBinding.artAppreciation.setText(sellingArtVo.getDetails());
        initBtnStatus();
    }

    public void rotation(float route) {
        ViewCompat.animate(mDataBinding.arrow).setDuration(200)
                .setInterpolator(new DecelerateInterpolator())
                .rotation(route)
                .start();
    }

    public void initBtnStatus() {

        if (sellingArtVo.isIs_owner()) {
            if (sellingArtVo.getCollection_mode() == 3) {
                if (sellingArtVo.getHas_amount() > 0) {
                    mDataBinding.buyNow.setText("出售");
                } else {
                    mDataBinding.buyNow.setEnabled(false);
                    mDataBinding.buyNow.setBackgroundColor(getColor(R.color._BEBEBE));
                }
            } else {
                mDataBinding.buyNow.setText("下架");
            }
        } else {
            if (sellingArtVo.getCollection_mode() == 3) {
                mDataBinding.buyNow.setEnabled(false);
                mDataBinding.buyNow.setBackgroundColor(getColor(R.color._BEBEBE));
            }
        }
    }

    private void createQrcode(final String mUrl) {
        if (TextUtils.isEmpty(mUrl)) return;
        showLoading(R.string.progress_loading);
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Bitmap>() {
            @Nullable
            @Override
            public Bitmap doInBackground() throws Throwable {

                return ZxingEncodingUtils.createQRCodeNative(mUrl, 300, 300, null);
            }

            @Override
            public void onSuccess(@Nullable Bitmap result) {
                dismissLoading();
                if (null != result)
                    DialogManager.showImgDialog(ArtDetailActivity.this, result, (dialog, which) -> {

                    });
            }

            @Override
            public void onCancel() {
                super.onCancel();
                dismissLoading();
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                dismissLoading();
            }
        });
    }

    private void showBigImg() {
        LogUtils.e("getFilesDir = " + getFilesDir());
        List<MultiplexImage> list = new ArrayList<>();
        for (String url : lists) {
            String str = url.substring(url.length() - 3);
            if (str.equalsIgnoreCase(GIF))
                list.add(new MultiplexImage(url, null, MultiplexImage.ImageType.GIF));
            else list.add(new MultiplexImage(url, null, MultiplexImage.ImageType.NORMAL));
        }
        File file = new File("assets/province.json");
        if (file.exists()) {
            ToastUtils.showLongToast(this, "success");
        }
        Mango.setImages(list);
        Mango.setPosition(selectPostioton);
        Mango.open(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buy_now:
                Bundle bundle = new Bundle();
                bundle.putSerializable(ART_KEY, sellingArtVo);
                startActivity(CreateOrderActivity.class, bundle);
                break;
            case R.id.zan:

                if (sellingArtVo.isLiked_by_me()) {
                    cancleLike();
                } else like();

                break;
            case R.id.cai:
                if (sellingArtVo.isDisliked_by_me()) {
                    cancleDisLike();
                } else disLike();
                break;
            case R.id.collect:
                if (sellingArtVo.isFavorite_by_me())
                    cancleCollect();
                else collect();
                break;
            case R.id.go_home_page:
                Bundle bundle1 = new Bundle();
                bundle1.putInt("uid", sellingArtVo.getAuthor().getId());
                startActivity(UserHomePageActivity.class, bundle1);
                break;

        }

    }

    public static class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            Glide.with(context).load(data).into(mImageView);
        }
    }

    public void like() {
        HashMap<String, String> params = new HashMap<>();
        params.put("art_id", art_id);
        RequestManager.instance().like(art_id, params, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null) {
                        sellingArtVo = response.body().getBody();
                        initPageData();
                    }
//                    sellingArtVo.setLiked_by_me(true);
//                    Drawable top = getResources().getDrawable(R.mipmap.icon_zan_);
//                    top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
//                    mDataBinding.zan.setCompoundDrawables(null, top, null, null);
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

    public void cancleLike() {
        HashMap<String, String> params = new HashMap<>();
        params.put("art_id", art_id);
        RequestManager.instance().canclelike(art_id, params, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null) {
                        sellingArtVo = response.body().getBody();
                        initPageData();
                    }
//                    sellingArtVo.setLiked_by_me(false);
//                    Drawable top = getResources().getDrawable(R.mipmap.icon_zan);
//                    top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
//                    mDataBinding.zan.setCompoundDrawables(null, top, null, null);
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
                    if (response.body().getBody() != null) {
                        sellingArtVo = response.body().getBody();
                        initPageData();
                    }
//                    sellingArtVo.setDisliked_by_me(true);
//                    Drawable top = getResources().getDrawable(R.mipmap.icon_cai_);
//                    top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
//                    mDataBinding.cai.setCompoundDrawables(null, top, null, null);
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

    public void cancleDisLike() {
        HashMap<String, String> params = new HashMap<>();
        params.put("art_id", art_id);
        RequestManager.instance().cancleDislike(art_id, params, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null) {
                        sellingArtVo = response.body().getBody();
                        initPageData();
                    }
//                    sellingArtVo.setDisliked_by_me(false);
//                    Drawable top = getResources().getDrawable(R.mipmap.icon_cai);
//                    top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
//                    mDataBinding.cai.setCompoundDrawables(null, top, null, null);
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
                    if (response.body().getBody() != null) {
                        sellingArtVo = response.body().getBody();
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

    public void cancleCollect() {
        HashMap<String, String> params = new HashMap<>();
        params.put("art_id", art_id);
        RequestManager.instance().discollect(art_id, params, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null) {
                        sellingArtVo = response.body().getBody();
                        initPageData();
                    }
//                    sellingArtVo.setFavorite_by_me(false);
//                    Drawable top = getResources().getDrawable(R.mipmap.icon_collect);
//                    top.setBounds(0, 0, top.getMinimumWidth(), top.getMinimumHeight());// 一定要设置setBounds();
//                    mDataBinding.collect.setCompoundDrawables(null, top, null, null);

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
                showLoading("正在下载...");
                LogUtils.e("Prepare downloading");
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
                int progressRate = new BigDecimal(String.valueOf(progress))
                        .divide(new BigDecimal(String.valueOf(size)), 0, RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("100"))
                        .stripTrailingZeros().intValue();
            }

            @Override
            public void onRemoved() {
            }

            @Override
            public void onDownloadSuccess() {
//                bt_download_button.setText("Delete");
                dismissLoading();
                String dir = YunApplication.LIVE2D_CACHE_PATH.concat(resouceId);
                if (FileHelper.unzip(path, dir)) {
                    ToastUtils.showLongToast(ArtDetailActivity.this, "success");
                    openLive2dActivity(dir);
                } else {
                    ToastUtils.showLongToast(ArtDetailActivity.this, "资源解压失败");
                }
            }

            @Override
            public void onDownloadFailed(DownloadException e) {
                e.printStackTrace();
                LogUtils.e("Download fail:" + e.getMessage());
                ToastUtils.showLongToast(ArtDetailActivity.this, "资源加载失败");
            }
        });
        downloadManager.download(downloadInfo);
    }

    private void openLive2dActivity(String dir) {
        String path = dir.concat("/");
        String modelName = sellingArtVo.getLive2d_file().concat(YunApplication.MODEL_PATH);
        Bundle bundle = new Bundle();
        bundle.putString(ShowLiveActivity.PATH, path);
        bundle.putString(ShowLiveActivity.MODEL_NAME, modelName);
        startActivity(ShowLiveActivity.class, bundle);
    }

    public void invisiableViews() {
        mDataBinding.showMoreList.setVisibility(View.GONE);
        mDataBinding.orderTimesTv.setVisibility(View.GONE);
        mDataBinding.orderTitle.setVisibility(View.GONE);
        mDataBinding.arrow.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderAmount(String.valueOf(sellingArtVo.getId()));
    }

    public void getOrderAmount(String uid) {
        RequestManager.instance().queryOrderAmounts(uid, new MinerCallback<BaseResponseVo<List<OrderAmountVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<OrderAmountVo>>> call, Response<BaseResponseVo<List<OrderAmountVo>>> response) {
                if (response.isSuccessful()) {
                    defaultList = new ArrayList<>();
                    artBeanList = response.body().getBody();
                    if (artBeanList.size() > 0) {
                        if (artBeanList.size() > 4) {
                            for (int i = 0; i < 4; i++) {
                                defaultList.add(artBeanList.get(i));
                            }
                            mDataBinding.showMoreList.setVisibility(View.VISIBLE);
                        } else {
                            mDataBinding.showMoreList.setVisibility(View.GONE);
                        }
                        if (defaultList.size() > 0) {
                            artDetailOrderListAdapter.setNewData(defaultList);
                        } else {
                            artDetailOrderListAdapter.setNewData(artBeanList);
                        }
                    } else invisiableViews();
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<OrderAmountVo>>> call, Response<BaseResponseVo<List<OrderAmountVo>>> response) {
                invisiableViews();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                invisiableViews();
            }
        });

    }
}