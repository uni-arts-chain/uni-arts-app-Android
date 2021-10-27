package com.gammaray.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gammaray.R;
import com.gammaray.adapter.CollectedDAppsAdapter;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.adapter.NetWorkTypeAdapter;
import com.gammaray.adapter.RecentlyDAppsAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.base.YunApplication;
import com.gammaray.constant.AppConstant;
import com.gammaray.databinding.FragmentFindLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.ChainBean;
import com.gammaray.entity.DAppFavouriteBean;
import com.gammaray.entity.DAppRecentlyBean;
import com.gammaray.entity.NetworkInfos;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.CollectedDAppsActivity;
import com.gammaray.ui.activity.DAppSearchActivity;
import com.gammaray.ui.activity.DAppWebsActivity;
import com.gammaray.ui.activity.QrScanActivity;
import com.gammaray.ui.activity.RecentlyDAppsActivity;
import com.gammaray.utils.SharedPreUtils;
import com.gammaray.widget.BasePopupWindow;
import com.google.android.material.tabs.TabLayout;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FindFragment extends BaseFragment<FragmentFindLayoutBinding> implements View.OnClickListener, MyHomePageAdapter.TabPagerListener {


    public static BaseFragment newInstance() {
        return new FindFragment();
    }

    private List<DAppFavouriteBean> mCollectApps = new ArrayList<>();

    private List<DAppRecentlyBean> mRecentApps = new ArrayList<>();

    private CollectedDAppsAdapter mCollectAdapter;

    private RecentlyDAppsAdapter mRecentAdapter;

    private boolean bIsShowCollect = true;

    private final List<String> mChainNames = new ArrayList<>();

    private List<ChainBean> mChainList = new ArrayList<>();

    private MyHomePageAdapter mChainPageAdapter;

    //默认只展示9条数据
    private int mPage = 1;

    private int mPerPage = 9;

    private PopupWindow mNetworkInfoPopWindow;

    private NetWorkTypeAdapter mNetWorkTypeAdapter;

    private List<NetworkInfos> mNetWorks = new ArrayList<>();

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_find_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

        initSelfDApps();

        mBinding.tvGoSearch.setOnClickListener(view -> {
            startActivity(DAppSearchActivity.class);
        });

        mBinding.rlSaoma.setOnClickListener(view -> {
            scan();
        });

        getChainList(); //获取链列表
        getCollectedDApps(); //获取收藏DApps
        getRecentlyDApps();//获取最近DApps
        initNetworks();
    }

    private void initSelfDApps() {

        mBinding.tvCollectLine.setVisibility(View.VISIBLE);
        mBinding.tvRecentLine.setVisibility(View.GONE);
        mBinding.rvCollects.setVisibility(View.VISIBLE);
        mBinding.rvRecent.setVisibility(View.GONE);
        mBinding.rlCollect.setOnClickListener(this);
        mBinding.rlRecent.setOnClickListener(this);
        mBinding.tvAllApp.setOnClickListener(this);
        mBinding.rlTitleLayout.setOnClickListener(this);
        mCollectAdapter = new CollectedDAppsAdapter(requireContext(), mCollectApps);
        LinearLayoutManager collectLayoutManager = new LinearLayoutManager(requireContext());
        collectLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBinding.rvCollects.setLayoutManager(collectLayoutManager);
        mCollectAdapter.setEmptyView(R.layout.dapps_empty_layout, mBinding.rvCollects);
        mBinding.rvCollects.setAdapter(mCollectAdapter);
        mCollectAdapter.setOnItemClickListener((adapter, view, position) -> {
            if(mCollectApps.get(position).getFavoritable() != null) {
                Intent intent = new Intent(requireContext(), DAppWebsActivity.class);
                intent.putExtra("dapp_title", mCollectApps.get(position).getFavoritable().getTitle());
                intent.putExtra("dapp_id", mCollectApps.get(position).getFavoritable().getId());
                intent.putExtra("dapp_collect", true);
                if(TextUtils.isEmpty(mCollectApps.get(position).getFavoritable().getWebsite_url())){
                    ToastUtils.showShort("网址解析错误");
                    return;
                }
                try {
                    intent.putExtra("dapp_icon_url", URLDecoder.decode(mCollectApps.get(position).getFavoritable().getLogo().getUrl(), "UTF-8"));
                    intent.putExtra("dapp_url", URLDecoder.decode(mCollectApps.get(position).getFavoritable().getWebsite_url(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }else{
                ToastUtils.showShort("非法数据");
            }
        });

        LinearLayoutManager recentLayoutManager = new LinearLayoutManager(requireContext());
        recentLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRecentAdapter = new RecentlyDAppsAdapter(requireContext(), mRecentApps);
        mBinding.rvRecent.setLayoutManager(recentLayoutManager);
        mRecentAdapter.setEmptyView(R.layout.dapps_empty_layout, mBinding.rvRecent);
        mBinding.rvRecent.setAdapter(mRecentAdapter);
        mRecentAdapter.setOnItemClickListener((adapter, view, position) -> {
            if(mRecentApps.get(position).getDapp() != null){
            Intent intent = new Intent(requireContext(), DAppWebsActivity.class);
            intent.putExtra("dapp_title", mRecentApps.get(position).getDapp().getTitle());
            intent.putExtra("dapp_id", mRecentApps.get(position).getDapp().getId());
            intent.putExtra("dapp_collect", mRecentApps.get(position).getDapp().isFavorite_by_me());
                if(TextUtils.isEmpty(mRecentApps.get(position).getDapp().getWebsite_url())){
                    ToastUtils.showShort("网址解析错误");
                    return;
                }
            try {
                intent.putExtra("dapp_icon_url", URLDecoder.decode(mRecentApps.get(position).getDapp().getLogo().getUrl(), "UTF-8"));
                intent.putExtra("dapp_url", URLDecoder.decode(mRecentApps.get(position).getDapp().getWebsite_url(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        }else{
                ToastUtils.showShort("非法数据");
            }
        });



        mBinding.srlLayout.setOnRefreshListener(() -> {
            mBinding.srlLayout.setRefreshing(false);
            getChainList(); //获取链列表
            getCollectedDApps(); //获取收藏DApps
            getRecentlyDApps();//获取最近DApps
        });
    }

    private void initNetworks() {
        if (YunApplication.getNetWorkInfos() != null && YunApplication.getNetWorkInfos().size() > 0) {
            mNetWorks = YunApplication.getNetWorkInfos();
            if (!SharedPreUtils.getString(requireContext(), SharedPreUtils.KEY_RPC_URL).equals("")) {
                String networkName = SharedPreUtils.getString(requireContext(), SharedPreUtils.KEY_RPC_NAME);
                mBinding.tvNetworks.setText(networkName);
            } else {
                initMainNet(mNetWorks);
            }
            View view = LayoutInflater.from(requireContext()).inflate(R.layout.pop_networks_info_layout, null);

            mNetworkInfoPopWindow = new BasePopupWindow(requireActivity());
            mNetworkInfoPopWindow.setContentView(view);
            mNetworkInfoPopWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            mNetworkInfoPopWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            mNetworkInfoPopWindow.setOutsideTouchable(false);
            mNetworkInfoPopWindow.setTouchable(true);
            mNetworkInfoPopWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

            RecyclerView rvNetWorks = view.findViewById(R.id.rv_networks);
            mNetWorkTypeAdapter = new NetWorkTypeAdapter(mBinding.tvNetworks, mNetworkInfoPopWindow, mNetWorks);
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
            rvNetWorks.setLayoutManager(layoutManager);
            rvNetWorks.setAdapter(mNetWorkTypeAdapter);

            RelativeLayout mCloseLayout = view.findViewById(R.id.tv_network_close);
            mCloseLayout.setOnClickListener(view1 -> {
                if (mNetworkInfoPopWindow != null) {
                    mNetworkInfoPopWindow.dismiss();
                }
            });
        } else {
            queryNetworks();
        }
    }

    private void showNetWorkPopWindow() {
        if (mNetworkInfoPopWindow != null) {
            mNetworkInfoPopWindow.showAtLocation(mBinding.srlLayout, Gravity.BOTTOM, 0, 0);
        }
    }

    private void queryNetworks() {
        RequestManager.instance().queryNetworks(new MinerCallback<BaseResponseVo<List<NetworkInfos>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<NetworkInfos>>> call, Response<BaseResponseVo<List<NetworkInfos>>> response) {
                dismissLoading();
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        YunApplication.setNetWorkInfo(response.body().getBody());
                        initNetworks();
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<NetworkInfos>>> call, Response<BaseResponseVo<List<NetworkInfos>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    private void initMainNet(List<NetworkInfos> networkInfos) {
        if (networkInfos != null && networkInfos.size() > 0) {
            for (int i = 0; i < networkInfos.size(); i++) {
                if (networkInfos.get(i).getTitle().equals("主网络")) {
                    List<NetworkInfos.ChainNetWork> mainChainWorks = networkInfos.get(i).getChain_networks();
                    if (mainChainWorks != null && mainChainWorks.size() > 0) {
                        if (!TextUtils.isEmpty(mainChainWorks.get(0).getName())) {
                            mBinding.tvNetworks.setText(mainChainWorks.get(0).getName());
                            return;
                        }
                    }
                }
            }
        }
    }

    private void scan() {

        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // android 6.0以上需要动态申请权限
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 访问手机存储申请权限
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // android 6.0以上需要动态申请权限
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(requireActivity(), QrScanActivity.class);
        startActivityForResult(intent, AppConstant.REQ_QR_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == AppConstant.REQ_QR_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                //扫描二维码或者扫描相册成功
                String result = data.getStringExtra("scan_result");
                ToastUtils.showShort("扫描成功" + result);
            } else {
                ToastUtils.showShort("取消扫描");
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_collect) {
            bIsShowCollect = true;
            mBinding.tvCollectLine.setVisibility(View.VISIBLE);
            mBinding.tvRecentLine.setVisibility(View.GONE);
            mBinding.rvCollects.setVisibility(View.VISIBLE);
            mBinding.rvRecent.setVisibility(View.GONE);
        } else if (view.getId() == R.id.rl_recent) {
            bIsShowCollect = false;
            mBinding.tvCollectLine.setVisibility(View.GONE);
            mBinding.tvRecentLine.setVisibility(View.VISIBLE);
            mBinding.rvCollects.setVisibility(View.GONE);
            mBinding.rvRecent.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.tv_all_app) {
            Intent intent;
            if (bIsShowCollect) {
                intent = new Intent(requireActivity(), CollectedDAppsActivity.class);
                intent.putExtra("title", "收藏");
            } else {
                intent = new Intent(requireActivity(), RecentlyDAppsActivity.class);
                intent.putExtra("title", "最近");
            }
            startActivity(intent);
        } else if (view.getId() == R.id.rl_title_layout) {
            showNetWorkPopWindow();
        }
    }

    @Override
    public Fragment getFragment(int position) {
        return DAppListFragment.newInstance(String.valueOf(mChainList.get(position).getId()));
    }

    //获取链列表，获取ChainId
    private void getChainList() {
        showLoading(R.string.progress_loading);
        RequestManager.instance().queryChainList(new MinerCallback<BaseResponseVo<List<ChainBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ChainBean>>> call, Response<BaseResponseVo<List<ChainBean>>> response) {
                dismissLoading();
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            mChainList = response.body().getBody();
                            mChainNames.clear();
                            for (int i = 0; i < mChainList.size(); i++) {
                                mChainNames.add(mChainList.get(i).getTitle());
                            }
                            initTabs();
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<ChainBean>>> call, Response<BaseResponseVo<List<ChainBean>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private void initTabs() {
        mChainPageAdapter = new MyHomePageAdapter(getChildFragmentManager(), mChainList.size(), mChainNames, requireActivity());
        mChainPageAdapter.setListener(this);
        mBinding.vpChains.setAdapter(mChainPageAdapter);
        mBinding.tabLayout.setupWithViewPager(mBinding.vpChains);
        mBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    private void getCollectedDApps() {
        RequestManager.instance().queryCollectedDApps(mPage, mPerPage, new MinerCallback<BaseResponseVo<List<DAppFavouriteBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppFavouriteBean>>> call, Response<BaseResponseVo<List<DAppFavouriteBean>>> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        mCollectApps.clear();
                        mCollectApps = response.body().getBody();
                        mCollectAdapter.setNewData(mCollectApps);
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<DAppFavouriteBean>>> call, Response<BaseResponseVo<List<DAppFavouriteBean>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    private void getRecentlyDApps() {
        RequestManager.instance().queryRecentlyDApps(mPage, mPerPage, new MinerCallback<BaseResponseVo<List<DAppRecentlyBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<DAppRecentlyBean>>> call, Response<BaseResponseVo<List<DAppRecentlyBean>>> response) {
                if (response != null && response.isSuccessful()) {
                    if (response.body() != null) {
                        mRecentApps.clear();
                        mRecentApps = response.body().getBody();
                        mRecentAdapter.setNewData(mRecentApps);
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<DAppRecentlyBean>>> call, Response<BaseResponseVo<List<DAppRecentlyBean>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }
}
