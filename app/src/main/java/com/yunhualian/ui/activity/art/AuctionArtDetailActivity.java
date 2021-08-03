package com.yunhualian.ui.activity.art;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
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
import com.yunhualian.R;
import com.yunhualian.adapter.ArtDetailImgAdapter;
import com.yunhualian.adapter.ArtDetailOrderListAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.databinding.ActivityAuctionArtDetailBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.EventBusMessageEvent;
import com.yunhualian.entity.OrderAmountVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.CustomerServiceActivity;
import com.yunhualian.ui.activity.PinCodeKtActivity;
import com.yunhualian.ui.activity.ShowNetBigImgActivity;
import com.yunhualian.ui.activity.user.CreateOrderActivity;
import com.yunhualian.ui.activity.user.MyHomePageActivity;
import com.yunhualian.ui.activity.user.SellArtActivity;
import com.yunhualian.ui.activity.user.SellArtUnCutActivity;
import com.yunhualian.ui.activity.user.UserHomePageActivity;
import com.yunhualian.utils.DateUtil;
import com.yunhualian.utils.DisplayUtils;
import com.yunhualian.utils.FileHelper;
import com.yunhualian.widget.BasePopupWindow;
import com.yunhualian.widget.UploadSuccessPopUpWindow;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
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
    public static final String ART_KEY = "art";
    public static final String GIF = "gif";
    public static final String ART_ID = "id";
    int totalAmount;
    List<String> lists = new ArrayList<>();
    private int selectPostioton = 0;
    SellingArtVo sellingArtVo;
    String art_id;
    String path;//下载路径
    String request_art_id;//部分页面传id
    boolean fistLoad = false;
    private int clickPosition = 0;
    private PopupWindow mZhengshuPopwinow;
    private List<String> artDetailUrls = new ArrayList<>();

    private final CountTimeHandler mHandler = new CountTimeHandler(this);
    ;
    private int mHours = 10;
    private int mMinutes = 20;
    private int mSeconds = 30;

    private static class CountTimeHandler extends Handler {
        private final WeakReference<AuctionArtDetailActivity> weakReference;

        CountTimeHandler(AuctionArtDetailActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AuctionArtDetailActivity activity = weakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        activity.computeTime();
                        activity.mDataBinding.tvCountHour.setText(activity.getTv(activity.mHours));
                        activity.mDataBinding.tvCountMinute.setText(activity.getTv(activity.mMinutes));
                        activity.mDataBinding.tvCountSecond.setText(activity.getTv(activity.mSeconds));
                        activity.mHandler.sendEmptyMessageDelayed(1, 1000);
                        break;
                }
            }
        }
    }

    private void computeTime() {
        mSeconds--;
        if (mSeconds < 0) {
            mMinutes--;
            mSeconds = 59;
            if (mMinutes < 0) {
                mMinutes = 59;
                mHours--;
                if (mHours < 0) {
                    // 倒计时结束
                }
            }
        }
    }

    private String getTv(long l) {
        if (l >= 10) {
            return l + "";
        } else {
            return "0" + l;//小于10,,前面补位一个"0"
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_auction_art_detail;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        fistLoad = true;
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.title_detail;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        sellingArtVo = (SellingArtVo) getIntent().getExtras().getSerializable(ART_KEY);
        initArtDetails();
        request_art_id = String.valueOf(getIntent().getIntExtra(ART_ID, 0));
        mDataBinding.buyNow.setOnClickListener(this);
        mDataBinding.zan.setOnClickListener(this);
        mDataBinding.cai.setOnClickListener(this);
        mDataBinding.collect.setOnClickListener(this);
        mDataBinding.goHomePage.setOnClickListener(this);
        mDataBinding.imgZhengshu.setOnClickListener(this);
        mDataBinding.imgPlay.setOnClickListener(this);
        mDataBinding.imgVideo.setOnClickListener(this);
        initZhengShuPopwindow();
        mHandler.sendEmptyMessage(1);
    }

    private void initArtDetails() {
        if (!artDetailUrls.isEmpty()) {
            ArtDetailImgAdapter artDetailImgAdapter = new ArtDetailImgAdapter(artDetailUrls, this);
            mDataBinding.artDetails.setLayoutManager(new LinearLayoutManager(this));
            mDataBinding.artDetails.setAdapter(artDetailImgAdapter);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessageEvent mEventBusMessageEvent) {
        if (null != mEventBusMessageEvent && !TextUtils.isEmpty(mEventBusMessageEvent.getmMessage())) {
            if (TextUtils.equals(ExtraConstant.EVENT_SELL_SUCCESS, mEventBusMessageEvent.getmMessage())) {
                showPopWindow();
            }
        }
    }

    private void showPopWindow() {
        uploadSuccessPopUpWindow = new UploadSuccessPopUpWindow(AuctionArtDetailActivity.this, new UploadSuccessPopUpWindow.OnBottomTextviewClickListener() {
            @Override
            public void onCancleClick() {
                uploadSuccessPopUpWindow.dismiss();
            }

            @Override
            public void onPerformClick() {
                uploadSuccessPopUpWindow.dismiss();
                startActivity(CustomerServiceActivity.class);
            }
        });
        uploadSuccessPopUpWindow.setConfirmText(getString(R.string.text_call_service));
        uploadSuccessPopUpWindow.setContent(getString(R.string.text_sell_tips));
        uploadSuccessPopUpWindow.showAtLocation(mDataBinding.parentLayout, Gravity.CENTER, 0, 0);
        EventBus.getDefault().removeAllStickyEvents();
    }

    private void initZhengShuPopwindow() {
        View contentView = LayoutInflater.from(this).inflate(
                R.layout.pop_zhengshu_layout, null);
        TextView mNftNameTv = contentView.findViewById(R.id.tv_nft_name);
        TextView mNftThemeTv = contentView.findViewById(R.id.tv_nft_theme);
        TextView mNftCountTv = contentView.findViewById(R.id.tv_nft_count);
        TextView mNftAddressTv = contentView.findViewById(R.id.tv_nft_address);
        RelativeLayout mNftCloseBtn = contentView.findViewById(R.id.layout_close);
        if (sellingArtVo != null) {
            if (!TextUtils.isEmpty(sellingArtVo.getName())) {
                mNftNameTv.setText(getString(R.string.nft_name, sellingArtVo.getName()));
            }
            if (!YunApplication.getArtThemeVoList().isEmpty()) {
                for (int i = 0; i < YunApplication.getArtThemeVoList().size(); i++) {
                    if (YunApplication.getArtThemeVoList().get(i).getId() == sellingArtVo.getCategory_id()) {
                        mNftThemeTv.setText(getString(R.string.nft_theme, YunApplication.getArtThemeVoList().get(i).getTitle()));
                    }
                }
            }
            mNftCountTv.setText(getString(R.string.nft_count, String.valueOf(sellingArtVo.getTotal_amount())));
            if (!TextUtils.isEmpty(sellingArtVo.getItem_hash())) {
                mNftAddressTv.setText(sellingArtVo.getItem_hash());
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

    public void initBanner() {
        fistLoad = false;
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
        mDataBinding.banner.setIndicatorVisible(false);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /*初始化页面信息*/
    public void initPageData() {

        if (sellingArtVo == null) return;
        if (!TextUtils.isEmpty(sellingArtVo.getResource_type())) {
            if (sellingArtVo.getResource_type().equals("4")) {
                if (!TextUtils.isEmpty(sellingArtVo.getImg_main_file2().getUrl()) && sellingArtVo.getImg_main_file2().getUrl().endsWith("mp4")) {
                    mDataBinding.imgPlay.setOnClickListener(this);
                    mDataBinding.layoutVideo.setVisibility(View.VISIBLE);
                    mDataBinding.layoutBanner.setVisibility(View.GONE);
                    Glide.with(this)
                            .load(sellingArtVo.getImg_main_file1().getUrl())
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

        if (sellingArtVo.getImg_detail_file1() != null) {
            if (!TextUtils.isEmpty(sellingArtVo.getImg_detail_file1().getUrl())) {
                artDetailUrls.add(sellingArtVo.getImg_detail_file1().getUrl());
            }
        }
        if (sellingArtVo.getImg_detail_file2() != null) {
            if (!TextUtils.isEmpty(sellingArtVo.getImg_detail_file2().getUrl())) {
                artDetailUrls.add(sellingArtVo.getImg_detail_file2().getUrl());
            }
        }
        if (sellingArtVo.getImg_detail_file3() != null) {
            if (!TextUtils.isEmpty(sellingArtVo.getImg_detail_file3().getUrl())) {
                artDetailUrls.add(sellingArtVo.getImg_detail_file3().getUrl());
            }
        }
        art_id = String.valueOf(sellingArtVo.getId());
        mDataBinding.pictureName.setText(sellingArtVo.getName());
        mDataBinding.centifyAddr.setText(getString(R.string.nft_address, sellingArtVo.getItem_hash()));
        String royalty = sellingArtVo.getRoyalty() == null ? "0" : sellingArtVo.getRoyalty().toPlainString();
        mDataBinding.rotailDate.setText(getString(R.string.royalty_date, sellingArtVo.getRoyalty_expired_at() == 0 ?
                sellingArtVo.getRoyalty() == null ? "" : "永久" :
                DateUtil.dateToStringWithZhYear(sellingArtVo.getRoyalty_expired_at() * 1000)));
        mDataBinding.rotailRate.setText(getString(R.string.royalty_rate, sellingArtVo.getRoyalty() == null ?
                "" :
                new BigDecimal(royalty).multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString().concat("%")));
        if (!TextUtils.isEmpty(sellingArtVo.getAuthor().getDisplay_name()))
            mDataBinding.creatorName.setText(sellingArtVo.getAuthor().getDisplay_name());
        else mDataBinding.creatorName.setText(getString(R.string.no_display_name));
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
        if (fistLoad)
            initBanner();
        mDataBinding.cai.setText(getString(R.string.text_cai_amount, String.valueOf(sellingArtVo.getDislike_count())));
        mDataBinding.zan.setText(getString(R.string.text_zan_amount, String.valueOf(sellingArtVo.getLiked_count())));
        mDataBinding.signAmount.setText(getString(R.string.sign_amounts, String.valueOf(sellingArtVo.getTrades_count())));
        mDataBinding.createAmount.setText(getString(R.string.create_amount, String.valueOf(sellingArtVo.getTotal_amount())));
        if (TextUtils.isEmpty(sellingArtVo.getDetails())) {
            mDataBinding.artAppreciationLayout.setVisibility(View.GONE);
        } else
            mDataBinding.artAppreciation.setText(sellingArtVo.getDetails());

        initBtnStatus();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void initBtnStatus() {

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
                if (sellingArtVo.getAuthor().getId() == YunApplication.getmUserVo().getId()) {
                    startActivity(MyHomePageActivity.class);
                } else {
                    Bundle bundle1 = new Bundle();
                    bundle1.putInt(UserHomePageActivity.UID, sellingArtVo.getAuthor().getId());
                    startActivity(UserHomePageActivity.class, bundle1);
                }
                break;

            case R.id.img_zhengshu:
                mZhengshuPopwinow.showAtLocation(mDataBinding.parentLayout, Gravity.CENTER, 0, 0);
                break;

            case R.id.img_play:
            case R.id.img_video:
                if (!TextUtils.isEmpty(sellingArtVo.getImg_main_file2().getUrl())) {
                    if (sellingArtVo.getImg_main_file2().getUrl().endsWith("mp4")) {
                        try {
                            PictureSelector.create(AuctionArtDetailActivity.this).externalPictureVideo(sellingArtVo.getImg_main_file2().getUrl());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
        }

    }

    /*根据不同状态跳不同页面*/
    private void buyAction() {
        if (sellingArtVo.isIs_owner()) {

        } else {

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

    /*取消点赞*/
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
        String modelName = sellingArtVo.getLive2d_file().concat(YunApplication.MODEL_PATH);
        Bundle bundle = new Bundle();
        bundle.putString(ShowLiveActivity.PATH, path);
        bundle.putString(ShowLiveActivity.MODEL_NAME, modelName);
        bundle.putBoolean("is_from_detail", true);
        startActivity(ShowLiveActivity.class, bundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestArtInfo();
    }

    public void requestArtInfo() {
        showLoading(getString(R.string.progress_loading));
        RequestManager.instance().artInfo(request_art_id, new MinerCallback<BaseResponseVo<SellingArtVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getBody() != null) {
                        dismissLoading();
                        sellingArtVo = response.body().getBody();
                        initPageData();
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<SellingArtVo>> call, Response<BaseResponseVo<SellingArtVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }
}