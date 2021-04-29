package com.yunhualian.ui.fragment;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.upbest.arouter.EventBusMessageEvent;
import com.yunhualian.R;
import com.yunhualian.adapter.HomePagePopularAdapter;
import com.yunhualian.adapter.HomePageThemeAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.FragmentHomeBinding;
import com.yunhualian.entity.AnnouncementVo;
import com.yunhualian.entity.ArtAuctionVo;
import com.yunhualian.entity.ArtPriceVo;
import com.yunhualian.entity.ArtTopicVo;
import com.yunhualian.entity.ArtTypeVo;
import com.yunhualian.entity.BannersVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.ArtDetailActivity;
import com.yunhualian.ui.activity.CustomerServiceActivity;
import com.yunhualian.ui.activity.MessagesActivity;
import com.yunhualian.ui.activity.SearchActivity;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;
import retrofit2.Call;
import retrofit2.Response;


public class HomeFragment extends BaseFragment<FragmentHomeBinding> implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private static final int HANDLER_MSG_BANNER = 0x1001;
    private static final int HANDLER_MSG_NOTICE = 0x1002;
    private HomePagePopularAdapter popularAdapter;
    private HomePageThemeAdapter themeAdapter;
    private List<BannersVo> bannersVoList;
    private List<AnnouncementVo> announcementVoList;
    private List<ArtAuctionVo> artAuctionVoList;
    private List<SellingArtVo> popularList;
    private List<ArtTopicVo> themeList;
    private int refreshIndex = 0;
    EditText searchText;
    LinearLayout messageIcon;
    LinearLayout kefuIcon;

    public static BaseFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            getPopular();
            refreshIndex++;
        }
    };

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
                ToastUtils.showLong("click" + index);
            });
            tv_notice2.setOnClickListener(v -> {
                ToastUtils.showLong("click" + (index + 1));
            });
        }

    }

    @Override
    protected void initView() {
        initRefresh();
        searchText = mBinding.titleLayout.findViewById(R.id.search_edt);
        searchText.setOnClickListener(v -> startActivity(SearchActivity.class));
        messageIcon = mBinding.titleLayout.findViewById(R.id.layout_menu);
        kefuIcon = mBinding.titleLayout.findViewById(R.id.layout_back);
        popularAdapter = new HomePagePopularAdapter(popularList);
        themeAdapter = new HomePageThemeAdapter(themeList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        StaggeredGridLayoutManager hotManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        hotManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mBinding.hotRecycle.setLayoutManager(hotManager);
        mBinding.hotRecycle.setAdapter(popularAdapter);
        popularAdapter.setOnItemClickListener(this);
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        mBinding.theme.setLayoutManager(sortLayoutManager);
        mBinding.theme.setAdapter(themeAdapter);
        mBinding.applyLayout.setOnClickListener(this);
        mBinding.certifySearch.setOnClickListener(this);
        mBinding.wallet.setOnClickListener(this);

        messageIcon.setOnClickListener(v -> startActivity(MessagesActivity.class));
        kefuIcon.setOnClickListener(v -> startActivity(CustomerServiceActivity.class));

//        getAuctionMeet();//获取拍卖


        //      getArtMaterial();//获取材质
//        getArtTheme();
        getPrice();//获取价格区间
        getArtType();//获取类型数据
        getCategories();//获取主题

        getBanner();//获取banner
        getNews();//获取新闻
        getPopular();//获取流行
        getTheme();//获取主题
        getUserInfo();//获取用户信息

    }


    @Override
    public void onPause() {
        super.onPause();
        mBinding.banner.pause();
    }

    private void initRefresh() {
        mBinding.srlShoopingMall.setColorSchemeResources(R.color.colorAccent);
        mBinding.srlShoopingMall.setDistanceToTriggerSync(500);
        mBinding.srlShoopingMall.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.srlShoopingMall.setRefreshing(false);
                getArtType();//获取类型数据
                getCategories();//获取主题
                getBanner();//获取banner
                getNews();//获取新闻
                getPopular();//获取流行
                getTheme();//获取主题
                getPrice();//获取价格区间
                getUserInfo();//获取用户信息
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
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
//            ToastUtils.showLong("s" + eventBusMessageEvent.getmValue());
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
        startActivity(ArtDetailActivity.class, bundle);
    }

    @Override
    public void onClick(View v) {

    }

    public static class BannerViewHolder implements MZViewHolder<String> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, String data) {
            Glide.with(context).load(data).into(mImageView);
            mImageView.setOnClickListener(v -> {
                ToastUtils.showLong("click" + position);
            });
        }
    }

    public static class AuctionViewHolder implements MZViewHolder<ArtAuctionVo> {
        private ImageView mImageView;
        private TextView auctionTime;
        private TextView artAmount;
        private TextView button;
        private TextView artMeetName;
        private TextView title;
        private CountdownView countdownView;
        SimpleDateFormat simpleDateFormat;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_home_banner_image, null);
            mImageView = (ImageView) view.findViewById(R.id.artist_pic);
            auctionTime = (TextView) view.findViewById(R.id.artist_time);
            artAmount = (TextView) view.findViewById(R.id.artist_amount);
            artMeetName = (TextView) view.findViewById(R.id.artist_name);
            title = view.findViewById(R.id.title);
            button = (TextView) view.findViewById(R.id.come_in);
            countdownView = (CountdownView) view.findViewById(R.id.countDown);
            simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
            return view;
        }

        @Override
        public void onBind(Context context, int position, ArtAuctionVo data) {
            Glide.with(context).load(data.getImg_file().getUrl()).into(mImageView);
            long current = System.currentTimeMillis();
            long last = Long.parseLong(data.getStart_at()) * 1000 - current;
            if (last > 0) {
                countdownView.start(last);
                title.setText(R.string.auction_time_start);
            } else if ((Long.parseLong(data.getEnd_at()) * 1000 - current) > 0) {
                countdownView.start(Long.parseLong(data.getEnd_at()) * 1000 - current);
                title.setText(R.string.auction_time_end);
            } else {
                title.setText(R.string.auction_time_end);
                countdownView.start(0);
            }
            Date date = new Date(Long.parseLong(data.getStart_at()) * 1000);
            Date endDate = new Date(Long.parseLong(data.getEnd_at()) * 1000);
            auctionTime.setText(simpleDateFormat.format(date) + " - " + simpleDateFormat.format(endDate));
            artAmount.setText(String.valueOf(data.getArt_size()).concat("件"));
            artMeetName.setText(data.getDesc());
            button.setOnClickListener(v -> {
                ToastUtils.showLong("click" + position);
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
                    bannersVoList = response.body().getBody();
                    if (bannersVoList.size() > 0) {
                        List<String> lists = new ArrayList<>();

                        for (BannersVo bannersVo : bannersVoList) {
                            lists.add(bannersVo.getImg_middle());
                        }
                        mBinding.banner.setPages(lists, BannerViewHolder::new);
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
     * 获取拍卖会列表
     * */
    public void getAuctionMeet() {
        RequestManager.instance().queryAuction(new MinerCallback<BaseResponseVo<List<ArtAuctionVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtAuctionVo>>> call, Response<BaseResponseVo<List<ArtAuctionVo>>> response) {
                if (response.isSuccessful()) {
                    artAuctionVoList = response.body().getBody();
                    if (artAuctionVoList.size() > 0) {
                        mBinding.auctionList.setIndicatorRes(R.mipmap.icon_time_p, R.mipmap.icon_art_amount);
                        mBinding.auctionList.setPages(artAuctionVoList, AuctionViewHolder::new);
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<ArtAuctionVo>>> call, Response<BaseResponseVo<List<ArtAuctionVo>>> response) {

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
                    popularList = response.body().getBody();

                    if (popularList.size() > 0) {
                        popularAdapter.setNewData(popularList);
                        if (refreshIndex == 0)
                            handler.sendEmptyMessageDelayed(0, 300);
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

    /*
     * 获取主题
     * */
    public void getTheme() {
        showLoading("");
        RequestManager.instance().queryTheme(new MinerCallback<BaseResponseVo<List<ArtTopicVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ArtTopicVo>>> call, Response<BaseResponseVo<List<ArtTopicVo>>> response) {
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

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

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
                    List<ArtTypeVo> typeVoList = response.body().getBody();
                    if (typeVoList.size() > 0) {
                        YunApplication.setArtThemeVoList(typeVoList);
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

    private void getUserInfo() {
//        RequestManager.instance().queryUser(new MinerCallback<BaseResponseVo<UserVo>>() {
//            @Override
//            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
//                if (response.isSuccessful())
//                    YunApplication.setmUserVo(response.body().getBody());
//            }
//
//            @Override
//            public void onError(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<?> call, Throwable t) {
//
//            }
//        });
    }
}