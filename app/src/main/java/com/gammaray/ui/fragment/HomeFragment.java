package com.gammaray.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gammaray.entity.NetworkInfos;
import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.interact.CreateWalletInteract;
import com.gammaray.eth.util.ETHWalletUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.igexin.sdk.PushManager;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;
import com.gammaray.R;
import com.gammaray.adapter.HomePageThemeAdapter;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.base.YunApplication;
import com.gammaray.databinding.FragmentHomeBinding;
import com.gammaray.entity.AnnouncementVo;
import com.gammaray.entity.ArtPriceVo;
import com.gammaray.entity.ArtTopicVo;
import com.gammaray.entity.ArtTypeVo;
import com.gammaray.entity.AuctionArtVo;
import com.gammaray.entity.BannersVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.NoRead;
import com.gammaray.entity.SellingArtVo;
import com.gammaray.entity.UserVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.CustomerServiceActivity;
import com.gammaray.ui.activity.NoticeInfoActivity;
import com.gammaray.ui.activity.SearchActivity;
import com.gammaray.ui.activity.art.ArtDetailActivity;
import com.gammaray.ui.activity.user.MessagesActivity;
import com.gammaray.ui.x5.ExplorerWebViewActivity;
import com.gammaray.utils.DateUtil;
import com.gammaray.utils.SharedPreUtils;
import com.upbest.arouter.Extras;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.bouncycastle.util.encoders.Hex;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Single;
import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType;
import jp.co.soramitsu.fearless_utils.encrypt.SignatureWrapper;
import jp.co.soramitsu.fearless_utils.encrypt.Signer;
import jp.co.soramitsu.fearless_utils.encrypt.model.Keypair;
import retrofit2.Call;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener, MyHomePageAdapter.TabPagerListener {

    private HomePageThemeAdapter themeAdapter;
    private List<BannersVo> bannersVoList;
    private List<AnnouncementVo> announcementVoList;
    private List<SellingArtVo> popularList = new ArrayList<>();
    private List<AuctionArtVo> auctionList = new ArrayList<>();
    private List<ArtTopicVo> themeList;
    LinearLayout searchLayout;
    LinearLayout messageIcon;
    LinearLayout kefuIcon;
    TextView noReadFlag;
    private boolean resume = false;
    private HomeHotFragment homeHotFragment;
    private HomeAuctionFragment homeAuctionFragment;
    private MyHomePageAdapter pageAdapter;
    private final CreateWalletInteract createWalletInteract = new CreateWalletInteract();
    private String pinCode;
    private String mnemonic;

    public static BaseFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void initPresenter() {

    }

    private void setViewFlipperItem(List<AnnouncementVo> announcementVos) {
        for (int i = 0; i < announcementVos.size(); i = i + 2) {
            AnnouncementVo announcementVo = announcementVos.get(i);
            AnnouncementVo announcementVo2 = null;
            if ((i + 1) < announcementVos.size()) {
                announcementVo2 = announcementVos.get(i + 1);
            }
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_flipper, null);
            TextView tv_notice = view.findViewById(R.id.tv_notice);
            TextView tv_notice2 = view.findViewById(R.id.tv_notice2);
            tv_notice.setText(announcementVo.getTitle());
            if (announcementVo2 != null)
                tv_notice2.setText(announcementVo2.getTitle());
            else tv_notice2.setVisibility(View.GONE);
            mBinding.noticeFlipper.addView(view);
            final int index = i;
            tv_notice.setOnClickListener(v -> {
                goNoticeActivity(index);
            });
            tv_notice2.setOnClickListener(v -> {
                goNoticeActivity(index + 1);
            });
        }
    }

    private void goNoticeActivity(int index) {
        if (announcementVoList.size() > 0 && announcementVoList.size() > index) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(NoticeInfoActivity.NOTIC, announcementVoList.get(index));
            startActivity(NoticeInfoActivity.class, bundle);
        }
    }

    @Override
    protected void initView() {
        initRefresh();
        initFragments();
        searchLayout = mBinding.titleLayout.findViewById(R.id.centerToolbarView);
        searchLayout.setOnClickListener(v -> startActivity(SearchActivity.class));
        noReadFlag = mBinding.titleLayout.findViewById(R.id.noRead);
        messageIcon = mBinding.titleLayout.findViewById(R.id.layout_menu);
        kefuIcon = mBinding.titleLayout.findViewById(R.id.layout_back);
        themeAdapter = new HomePageThemeAdapter(themeList, mActivity);
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        mBinding.theme.setLayoutManager(sortLayoutManager);
        mBinding.theme.setAdapter(themeAdapter);
        mBinding.applyLayout.setOnClickListener(this);
        mBinding.certifySearch.setOnClickListener(this);
        mBinding.wallet.setOnClickListener(this);
        mBinding.currentTime.setText(DateUtil.dateToStringWithZh(System.currentTimeMillis()));
        messageIcon.setOnClickListener(v -> startActivity(MessagesActivity.class));
        kefuIcon.setOnClickListener(v -> startActivity(CustomerServiceActivity.class));
        mBinding.mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    mBinding.srlShoopingMall.setEnabled(true);
                } else {
                    mBinding.srlShoopingMall.setEnabled(false);
                }
            }
        });
        loginByAddress(false);
    }

    private void initFragments() {
        homeHotFragment = new HomeHotFragment(popularList);
        homeAuctionFragment = new HomeAuctionFragment(auctionList);
        pageAdapter = new MyHomePageAdapter(getChildFragmentManager(), 2, Arrays.asList(getResources().getStringArray(R.array.home_tabs)), requireActivity());
        pageAdapter.setListener(this);
        mBinding.vpSellAuction.setAdapter(pageAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.vpSellAuction);
        mBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void initRequest() {
        getPrice();//获取价格区间
        getArtType();//获取类型数据
        getCategories();//获取主题
        getBanner();//获取banner
        getNews();//获取新闻
        getPopular();//获取流行
        getAuctions();//获取推荐拍卖
        getTheme();//获取主题
        hasUnReadMessage();//查询唯独消息
    }

    @Override
    public void onPause() {
        super.onPause();
        mBinding.banner.pause();
    }

    private void initRefresh() {
        mBinding.srlShoopingMall.setColorSchemeResources(R.color.colorAccent);
        mBinding.srlShoopingMall.setDistanceToTriggerSync(300);
        mBinding.srlShoopingMall.setOnRefreshListener(() -> {
            mBinding.srlShoopingMall.setRefreshing(false);
            loginByAddress(false);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (resume) {
            getBanner();//获取banner
            getNews();//获取新闻
            hasUnReadMessage();//查询唯独消息
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusMessageEvent eventBusMessageEvent) {
        if (eventBusMessageEvent != null) {
            if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_REFRESH_TOKEN)) {
                //refresh token
                loginByAddress(true);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ArtDetailActivity.ART_KEY, popularList.get(position));
        bundle.putInt(ArtDetailActivity.ART_ID, popularList.get(position).getId());
        startActivity(ArtDetailActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0) {
            return homeHotFragment;
        }
        return homeAuctionFragment;
    }

    public static class BannerViewHolder implements MZViewHolder<BannersVo> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, BannersVo data) {
            Glide.with(context).clear(mImageView);
            Glide.with(context).load(data.getImg_middle()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(withCrossFade()).into(mImageView);
            mImageView.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(data.getUrl())) {
                    Bundle bundle = new Bundle();
                    String title = TextUtils.isEmpty(data.getTitle()) ? context.getString(R.string.explore_page) : data.getTitle();
                    bundle.putString(ExplorerWebViewActivity.TITLE, title);
                    bundle.putString(ExplorerWebViewActivity.URL, data.getUrl());
                    Intent intent = new Intent(context, ExplorerWebViewActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }

            });
        }
    }

    /*
     * 获取banner
     * */
    public void getBanner() {
        RequestManager.instance().queryBanner(new MinerCallback<BaseResponseVo<List<BannersVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<BannersVo>>> call, Response<BaseResponseVo<List<BannersVo>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) return;
                    if (response.body().getBody() != null)
                        bannersVoList = response.body().getBody();
                    if (bannersVoList != null && bannersVoList.size() > 0) {
                        List<String> lists = new ArrayList<>();

                        for (BannersVo bannersVo : bannersVoList) {
                            lists.add(bannersVo.getImg_middle());
                        }
                        mBinding.banner.setPages(bannersVoList, BannerViewHolder::new);
                        mBinding.banner.start();
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<BannersVo>>> call, Response<BaseResponseVo<List<BannersVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    /*
     * 获取新闻
     * */
    public void getNews() {
        RequestManager.instance().queryNews(1, "New::Announcement", new MinerCallback<BaseResponseVo<List<AnnouncementVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AnnouncementVo>>> call, Response<BaseResponseVo<List<AnnouncementVo>>> response) {
                if (response.isSuccessful()) {
                    announcementVoList = response.body().getBody();

                    if (announcementVoList.size() > 0) {
                        setViewFlipperItem(announcementVoList);
                        mBinding.noticeFlipper.startFlipping();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<AnnouncementVo>>> call, Response<BaseResponseVo<List<AnnouncementVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }

    /*
     * 获取新闻
     * */
    public void getPopular() {
        RequestManager.instance().queryPopular(1, 10, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) return;
                    if (response.body().getBody() != null)
                        popularList = response.body().getBody();

                    if (popularList != null && popularList.size() > 0) {
                        homeHotFragment.updateData(popularList);
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

    private void getAuctions() {
        RequestManager.instance().queryHomePageAuctions(1, 10, new MinerCallback<BaseResponseVo<List<AuctionArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) return;
                    if (response.body().getBody() != null)
                        auctionList = response.body().getBody();

                    if (auctionList != null && auctionList.size() > 0) {
                        homeAuctionFragment.updateData(auctionList);
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    /*
     * 获取主题
     * */
    public void getTheme() {
        showLoading("");
        RequestManager.instance().queryTheme(new MinerCallback<BaseResponseVo<List<ArtTopicVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtTopicVo>>> call, Response<BaseResponseVo<List<ArtTopicVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    themeList = response.body().getBody();

                    if (themeList.size() > 0) {
                        themeAdapter.setNewData(themeList);
//                        new getDescTheme().execute(themeList);
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<ArtTopicVo>>> call, Response<BaseResponseVo<List<ArtTopicVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });

    }

    /*
     * 获取作品类型
     * */
    public void getArtType() {
        RequestManager.instance().queryArtType(new MinerCallback<BaseResponseVo<List<ArtTypeVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtTypeVo>>> call, Response<BaseResponseVo<List<ArtTypeVo>>> response) {
                if (response.isSuccessful()) {
                    List<ArtTypeVo> typeVoList = response.body().getBody();
                    if (typeVoList.size() > 0) {
                        YunApplication.setArtTypelist(typeVoList);
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<ArtTypeVo>>> call, Response<BaseResponseVo<List<ArtTypeVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }

    private void hasUnReadMessage() {
        RequestManager.instance().queryHasUnReadMessage(new MinerCallback<BaseResponseVo<NoRead>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<NoRead>> call, Response<BaseResponseVo<NoRead>> response) {
                if (response.isSuccessful())
                    if (response.body().getBody().getHas_unread() > BigDecimal.ZERO.intValue()) {
                        noReadFlag.setVisibility(View.VISIBLE);
                    } else noReadFlag.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Call<BaseResponseVo<NoRead>> call, Response<BaseResponseVo<NoRead>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }


    /*
     * 给图片id添加描述信息
     * */
//    public class getDesc extends AsyncTask<List<SellingArtVo>, Integer, List<ArtBean>> {
//        @Override
//        protected List<SellingArtVo> doInBackground(List<SellingArtVo>... lists) {
//            List<SellingArtVo> artBeanList = lists[0];
//            for (int i = 0; i < artBeanList.size(); i++) {
//
//                List<ArtTypeVo> list = YunApplication.getArtTypelist();
//                if (list.size() > 0) {
//                    SellingArtVo artBean = artBeanList.get(i);
//                    for (ArtTypeVo artTypeVo : list) {
////                        if (artTypeVo.getId() == artBean.getMaterial_id())
////                            artBean.setMaterial_des(artTypeVo.getTitle());
//                    }
//                }
//            }
//            return artBeanList;
//        }
//
//        @Override
//        protected void onPostExecute(List<SellingArtVo> artBeans) {
//            super.onPostExecute(artBeans);
//            popularAdapter.setNewData(artBeans);
//        }
//    }

    /*
     * 获取主题
     * */
    public class getDescTheme extends AsyncTask<List<ArtTopicVo>, Integer, List<ArtTopicVo>> {
        @Override
        protected List<ArtTopicVo> doInBackground(List<ArtTopicVo>... lists) {
            List<ArtTopicVo> artBeanList = lists[0];
            for (int i = 0; i < artBeanList.size(); i++) {

                List<ArtTypeVo> list = YunApplication.getArtTypelist();
                ArtTopicVo artBean = artBeanList.get(i);
                List<SellingArtVo> list1 = artBean.getArts();
                if (list1 != null)
                    if (list.size() > 0) {
                        for (int j = 0; j < list1.size(); j++) {
                            SellingArtVo artsBean = list1.get(j);
                            for (ArtTypeVo artTypeVo : list) {
//                                if (artTypeVo.getId() == artsBean.getMember_id())
//                                    artsBean.setMaterial_desc(artTypeVo.getTitle());
                            }
                        }
                    }
            }
            return artBeanList;
        }

        @Override
        protected void onPostExecute(List<ArtTopicVo> artBeans) {
            super.onPostExecute(artBeans);
            themeAdapter.setNewData(artBeans);
            dismissLoading();
        }

    }


    /*
     * 获取价格
     * */
    public void getPrice() {
        RequestManager.instance().queryPrize(new MinerCallback<BaseResponseVo<List<ArtPriceVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtPriceVo>>> call, Response<BaseResponseVo<List<ArtPriceVo>>> response) {
                if (response.isSuccessful()) {
                    List<ArtPriceVo> pricesVoList = response.body().getBody();
                    if (pricesVoList.size() > 0) {
                        YunApplication.setArtPriceVoList(pricesVoList);
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<ArtPriceVo>>> call, Response<BaseResponseVo<List<ArtPriceVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }

    private void getCategories() {
        RequestManager.instance().queryCategories(new MinerCallback<BaseResponseVo<List<ArtTypeVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtTypeVo>>> call, Response<BaseResponseVo<List<ArtTypeVo>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        if (response.body().getBody() != null) {
                            List<ArtTypeVo> typeVoList = response.body().getBody();
                            if (typeVoList.size() > 0) {
                                YunApplication.setArtThemeVoList(typeVoList);
                            }
                        }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<ArtTypeVo>>> call, Response<BaseResponseVo<List<ArtTypeVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void loginByAddress(boolean refreshToken) {
        String privateKey = SharedPreUtils.getString(mActivity, SharedPreUtils.KEY_PRIVATE);
        String publicKey = SharedPreUtils.getString(mActivity, SharedPreUtils.KEY_PUBLICKEY);
        String nonce = SharedPreUtils.getString(mActivity, SharedPreUtils.KEY_NONCE);
        String Address = SharedPreUtils.getString(mActivity, SharedPreUtils.KEY_ADDRESS);
        LogUtils.e(privateKey + "|" + publicKey + "|" + nonce);
        Keypair keypair = new Keypair(Hex.decode(privateKey), Hex.decode(publicKey), Hex.decode(nonce.substring(2)));
        Signer signer = new Signer();
        SignatureWrapper signatureWrapper = signer.sign(EncryptionType.SR25519, Address.getBytes(), keypair);
        String singStr2 = Hex.toHexString(signatureWrapper.getSignature());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("address", Address);
        hashMap.put("message", Address);
        hashMap.put("signature", singStr2);
        hashMap.put("cid", PushManager.getInstance().getClientid(mActivity));
        hashMap.put("os", "android");
        RequestManager.instance().addressLogin(hashMap, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        if (response.body().getBody() != null) {
                            UserVo userVo = response.body().getBody();
                            YunApplication.setmUserVo(userVo);
                            YunApplication.setToken(userVo.getToken());
                            resume = true;
                            if (!refreshToken)
                                initRequest();
                        }
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