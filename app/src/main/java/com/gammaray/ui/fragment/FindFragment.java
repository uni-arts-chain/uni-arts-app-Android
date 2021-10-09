package com.gammaray.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.R;
import com.gammaray.adapter.CollectedDAppsAdapter;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.adapter.RecentlyDAppsAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.constant.AppConstant;
import com.gammaray.databinding.FragmentFindLayoutBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.ChainBean;
import com.gammaray.entity.DAppFavouriteBean;
import com.gammaray.entity.DAppItemBean;
import com.gammaray.entity.DAppRecentlyBean;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.CollectedDAppsActivity;
import com.gammaray.ui.activity.DAppSearchActivity;
import com.gammaray.ui.activity.DAppsListActivity;
import com.gammaray.ui.activity.QrScanActivity;
import com.gammaray.ui.activity.RecentlyDAppsActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
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

    }

    private void initSelfDApps() {

        mBinding.tvCollectLine.setVisibility(View.VISIBLE);
        mBinding.tvRecentLine.setVisibility(View.GONE);
        mBinding.rvCollects.setVisibility(View.VISIBLE);
        mBinding.rvRecent.setVisibility(View.GONE);
        mBinding.rlCollect.setOnClickListener(this);
        mBinding.rlRecent.setOnClickListener(this);
        mBinding.tvAllApp.setOnClickListener(this);

        mCollectAdapter = new CollectedDAppsAdapter(requireContext(), mCollectApps);
        LinearLayoutManager collectLayoutManager = new LinearLayoutManager(requireContext());
        collectLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBinding.rvCollects.setLayoutManager(collectLayoutManager);
        mCollectAdapter.setEmptyView(R.layout.dapps_empty_layout, mBinding.rvCollects);
        mBinding.rvCollects.setAdapter(mCollectAdapter);

        LinearLayoutManager recentLayoutManager = new LinearLayoutManager(requireContext());
        recentLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRecentAdapter = new RecentlyDAppsAdapter(requireContext(), mRecentApps);
        mBinding.rvRecent.setLayoutManager(recentLayoutManager);
        mRecentAdapter.setEmptyView(R.layout.dapps_empty_layout, mBinding.rvRecent);
        mBinding.rvRecent.setAdapter(mRecentAdapter);

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
        }
    }

    @Override
    public Fragment getFragment(int position) {
        return DAppListFragment.newInstance(String.valueOf(mChainList.get(position).getId()));
    }

    //获取链列表，获取ChainId
    private void getChainList() {
        RequestManager.instance().queryChainList(new MinerCallback<BaseResponseVo<List<ChainBean>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<ChainBean>>> call, Response<BaseResponseVo<List<ChainBean>>> response) {
                if (response != null) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            mChainList = response.body().getBody();
                            for (int i = 0; i < mChainList.size(); i++) {
                                mChainNames.clear();
                                mChainNames.add(mChainList.get(i).getTitle());
                                initTabs();
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<ChainBean>>> call, Response<BaseResponseVo<List<ChainBean>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

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

    private void sendRecentlyDApps(String id) {
        HashMap<String, String> params = new HashMap<>();
        params.put("dapp_id", id);
        RequestManager.instance().sendRecentlyDApps(params, new MinerCallback<BaseResponseVo<DAppRecentlyBean>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<DAppRecentlyBean>> call, Response<BaseResponseVo<DAppRecentlyBean>> response) {

            }

            @Override
            public void onError(Call<BaseResponseVo<DAppRecentlyBean>> call, Response<BaseResponseVo<DAppRecentlyBean>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    private void favoriteDApp(String id) {
        RequestManager.instance().favoriteDApp(id, new MinerCallback<BaseResponseVo<DAppItemBean>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<DAppItemBean>> call, Response<BaseResponseVo<DAppItemBean>> response) {
                if (response != null && response.isSuccessful()) {

                }
            }

            @Override
            public void onError(Call<BaseResponseVo<DAppItemBean>> call, Response<BaseResponseVo<DAppItemBean>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }
}
