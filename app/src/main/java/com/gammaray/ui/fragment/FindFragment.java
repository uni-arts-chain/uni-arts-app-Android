package com.gammaray.ui.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.R;
import com.gammaray.adapter.DAppsListAdapter;
import com.gammaray.adapter.FindDAppsAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.constant.AppConstant;
import com.gammaray.databinding.FragmentFindLayoutBinding;
import com.gammaray.entity.DAppBean;
import com.gammaray.entity.WalletLinkBean;
import com.gammaray.ui.activity.DAppSearchActivity;
import com.gammaray.ui.activity.DAppsListActivity;
import com.gammaray.ui.activity.QrScanActivity;
import com.gammaray.utils.DisplayUtils;
import com.gammaray.widget.pager.PagerGridLayoutManager;
import com.gammaray.widget.pager.PagerGridSnapHelper;

import java.util.ArrayList;
import java.util.List;

public class FindFragment extends BaseFragment<FragmentFindLayoutBinding> implements View.OnClickListener {


    public static BaseFragment newInstance() {
        return new FindFragment();
    }

    private List<DAppBean> mCollectApps = new ArrayList<>();

    private List<DAppBean> mRecentApps = new ArrayList<>();

    private FindDAppsAdapter mCollectAdapter;

    private FindDAppsAdapter mRecentAdapter;

    private DAppsListAdapter mEthRecommendAdapter;

    private DAppsListAdapter mEthTransferAdapter;

    private List<WalletLinkBean> mEthRecommends = new ArrayList<>();

    private List<WalletLinkBean> mEthTransfers = new ArrayList<>();

    private DAppsListAdapter mUartRecommendAdapter;

    private DAppsListAdapter mUartTransferAdapter;

    private List<WalletLinkBean> mUartRecommends = new ArrayList<>();

    private List<WalletLinkBean> mUartTransfers = new ArrayList<>();

    private int mScreenWidth;

    private boolean bIsShowCollect = true;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_find_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

        mScreenWidth = ScreenUtils.getScreenWidth() / 12 * 11;

        initSelfDApps();

        initCoinDApps();

        mBinding.tvGoSearch.setOnClickListener(view -> {
            startActivity(DAppSearchActivity.class);
        });

        mBinding.rlSaoma.setOnClickListener(view -> {
            scan();
        });
    }

    private void initSelfDApps() {

        mBinding.tvCollectLine.setVisibility(View.VISIBLE);
        mBinding.tvRecentLine.setVisibility(View.GONE);
        mBinding.rvCollects.setVisibility(View.VISIBLE);
        mBinding.rvRecent.setVisibility(View.GONE);
        mBinding.rlCollect.setOnClickListener(this);
        mBinding.rlRecent.setOnClickListener(this);
        mBinding.tvAllApp.setOnClickListener(this);
        mBinding.tvEthRecommendAll.setOnClickListener(this);
        mBinding.tvEthTransferAll.setOnClickListener(this);
        mBinding.tvUartRecommendAll.setOnClickListener(this);
        mBinding.tvUartTransferAll.setOnClickListener(this);

        for (int i = 0; i < 10; i++) {
            mCollectApps.add(new DAppBean(R.mipmap.icon_eth, "ETH"));
        }
        for (int i = 0; i < 10; i++) {
            mRecentApps.add(new DAppBean(R.mipmap.icon_uart, "UART"));
        }

        for (int i = 0; i < 10; i++) {
            mEthRecommends.add(new WalletLinkBean("ETH", "ETHETHETHETH", R.mipmap.icon_eth));
        }

        for (int i = 0; i < 10; i++) {
            mEthTransfers.add(new WalletLinkBean("ETH", "ETHETHETHETH", R.mipmap.icon_eth));
        }

        for (int i = 0; i < 10; i++) {
            mUartRecommends.add(new WalletLinkBean("UART", "UniartUniartUniart", R.mipmap.icon_uart));
        }

        for (int i = 0; i < 10; i++) {
            mUartTransfers.add(new WalletLinkBean("UART", "UniartUniartUniart", R.mipmap.icon_uart));
        }

        mCollectAdapter = new FindDAppsAdapter(mCollectApps);
        LinearLayoutManager collectLayoutManager = new LinearLayoutManager(requireContext());
        collectLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mBinding.rvCollects.setLayoutManager(collectLayoutManager);
        mCollectAdapter.setEmptyView(R.layout.dapps_empty_layout, mBinding.rvCollects);
        mBinding.rvCollects.setAdapter(mCollectAdapter);

        LinearLayoutManager recentLayoutManager = new LinearLayoutManager(requireContext());
        recentLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRecentAdapter = new FindDAppsAdapter(mRecentApps);
        mBinding.rvRecent.setLayoutManager(recentLayoutManager);
        mRecentAdapter.setEmptyView(R.layout.dapps_empty_layout, mBinding.rvRecent);
        mBinding.rvRecent.setAdapter(mRecentAdapter);

    }

    private void initCoinDApps() {
        mBinding.tvEthLine.setVisibility(View.VISIBLE);
        mBinding.tvUartLine.setVisibility(View.GONE);
        mBinding.rlEthAppList.setVisibility(View.VISIBLE);
        mBinding.rlUartAppList.setVisibility(View.GONE);
        mBinding.rlEthCoin.setOnClickListener(this);
        mBinding.rlUartCoin.setOnClickListener(this);
        initEthDAppList();
        initUartDAppList();
    }

    private void initEthDAppList() {

        int ethRecommendHeight = 0;
        int ethRecommendRows = 1;
        if (mEthRecommends != null && mEthRecommends.size() > 0) {
            if (mEthRecommends.size() == 1) {
                ethRecommendHeight = 80;
                ethRecommendRows = 1;
            } else if (mEthRecommends.size() == 2) {
                ethRecommendHeight = 160;
                ethRecommendRows = 2;
            } else {
                ethRecommendHeight = 240;
                ethRecommendRows = 3;
            }
        } else {
            mBinding.rlEthRecommendTitle.setVisibility(View.GONE);
            mBinding.rvEthRecommend.setVisibility(View.GONE);
        }

        //ETH推荐
        PagerGridLayoutManager ethRecommendLayoutManager = new PagerGridLayoutManager(ethRecommendRows, 1, PagerGridLayoutManager.HORIZONTAL);
        mBinding.rvEthRecommend.setLayoutManager(ethRecommendLayoutManager);

        RelativeLayout.LayoutParams recommendParams = new RelativeLayout.LayoutParams(mScreenWidth, DisplayUtils.dp2px(requireContext(), ethRecommendHeight));
        recommendParams.addRule(RelativeLayout.BELOW, R.id.rl_eth_recommend_title);
        mBinding.rvEthRecommend.setLayoutParams(recommendParams);

        PagerGridSnapHelper recommendPageSnapHepler = new PagerGridSnapHelper();
        recommendPageSnapHepler.attachToRecyclerView(mBinding.rvEthRecommend);

        mEthRecommendAdapter = new DAppsListAdapter(mEthRecommends);
        mBinding.rvEthRecommend.setAdapter(mEthRecommendAdapter);

        int ethTransferHeight = 0;
        int ethTransferRows = 1;
        if (mEthTransfers != null && mEthTransfers.size() > 0) {
            if (mEthTransfers.size() == 1) {
                ethTransferHeight = 80;
                ethTransferRows = 1;
            } else if (mEthTransfers.size() == 2) {
                ethTransferHeight = 160;
                ethTransferRows = 2;
            } else {
                ethTransferHeight = 240;
                ethTransferRows = 3;
            }
        } else {
            mBinding.rlEthTransferTitle.setVisibility(View.GONE);
            mBinding.rvEthTransfer.setVisibility(View.GONE);
        }

        //ETH交易
        PagerGridLayoutManager ethTransferLayoutManager = new PagerGridLayoutManager(ethTransferRows, 1, PagerGridLayoutManager.HORIZONTAL);
        mBinding.rvEthTransfer.setLayoutManager(ethTransferLayoutManager);

        RelativeLayout.LayoutParams transferParams = new RelativeLayout.LayoutParams(mScreenWidth, DisplayUtils.dp2px(requireContext(), ethTransferHeight));
        transferParams.addRule(RelativeLayout.BELOW, R.id.rl_eth_transfer_title);
        mBinding.rvEthTransfer.setLayoutParams(transferParams);

        PagerGridSnapHelper transferPageSnapHelper = new PagerGridSnapHelper();
        transferPageSnapHelper.attachToRecyclerView(mBinding.rvEthTransfer);

        mEthTransferAdapter = new DAppsListAdapter(mEthTransfers);
        mBinding.rvEthTransfer.setAdapter(mEthTransferAdapter);

    }

    private void initUartDAppList() {
        int uartRecommendHeight = 0;
        int uartRecommendRows = 1;
        if (mUartRecommends != null && mUartRecommends.size() > 0) {
            if (mUartRecommends.size() == 1) {
                uartRecommendHeight = 80;
                uartRecommendRows = 1;
            } else if (mUartRecommends.size() == 2) {
                uartRecommendHeight = 160;
                uartRecommendRows = 2;
            } else {
                uartRecommendHeight = 240;
                uartRecommendRows = 3;
            }
        } else {
            mBinding.rlUartRecommendTitle.setVisibility(View.GONE);
            mBinding.rvUartRecommend.setVisibility(View.GONE);
        }

        //Uart推荐
        PagerGridLayoutManager uartRecommendLayoutManager = new PagerGridLayoutManager(uartRecommendRows, 1, PagerGridLayoutManager.HORIZONTAL);
        mBinding.rvUartRecommend.setLayoutManager(uartRecommendLayoutManager);

        RelativeLayout.LayoutParams recommendParams = new RelativeLayout.LayoutParams(mScreenWidth, DisplayUtils.dp2px(requireContext(), uartRecommendHeight));
        recommendParams.addRule(RelativeLayout.BELOW, R.id.rl_uart_recommend_title);
        mBinding.rvUartRecommend.setLayoutParams(recommendParams);

        PagerGridSnapHelper recommendPageSnapHepler = new PagerGridSnapHelper();
        recommendPageSnapHepler.attachToRecyclerView(mBinding.rvUartRecommend);

        mUartRecommendAdapter = new DAppsListAdapter(mUartRecommends);
        mBinding.rvUartRecommend.setAdapter(mUartRecommendAdapter);

        int uartTransferHeight = 0;
        int uartTransferRows = 1;
        if (mUartTransfers != null && mUartTransfers.size() > 0) {
            if (mUartTransfers.size() == 1) {
                uartTransferHeight = 80;
                uartTransferRows = 1;
            } else if (mUartTransfers.size() == 2) {
                uartTransferHeight = 160;
                uartTransferRows = 2;
            } else {
                uartTransferHeight = 240;
                uartTransferRows = 3;
            }
        } else {
            mBinding.rlUartTransferTitle.setVisibility(View.GONE);
            mBinding.rvUartTransfer.setVisibility(View.GONE);
        }

        //Uart交易
        PagerGridLayoutManager uartTransferLayoutManager = new PagerGridLayoutManager(uartTransferRows, 1, PagerGridLayoutManager.HORIZONTAL);
        mBinding.rvUartTransfer.setLayoutManager(uartTransferLayoutManager);

        RelativeLayout.LayoutParams transferParams = new RelativeLayout.LayoutParams(mScreenWidth, DisplayUtils.dp2px(requireContext(), uartTransferHeight));
        transferParams.addRule(RelativeLayout.BELOW, R.id.rl_uart_transfer_title);
        mBinding.rvUartTransfer.setLayoutParams(transferParams);

        PagerGridSnapHelper transferPageSnapHelper = new PagerGridSnapHelper();
        transferPageSnapHelper.attachToRecyclerView(mBinding.rvUartTransfer);

        mUartTransferAdapter = new DAppsListAdapter(mUartTransfers);
        mBinding.rvUartTransfer.setAdapter(mUartTransferAdapter);

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
            Intent intent = new Intent(requireActivity(),DAppsListActivity.class);
            if (bIsShowCollect) {
                intent.putExtra("title","收藏");
                intent.putExtra("type","0");
            } else {
                intent.putExtra("title","最近");
                intent.putExtra("type","1");
            }
            startActivity(intent);
        } else if (view.getId() == R.id.rl_eth_coin) {
            mBinding.tvEthLine.setVisibility(View.VISIBLE);
            mBinding.tvUartLine.setVisibility(View.GONE);
            mBinding.rlEthAppList.setVisibility(View.VISIBLE);
            mBinding.rlUartAppList.setVisibility(View.GONE);
        } else if (view.getId() == R.id.rl_uart_coin) {
            mBinding.tvEthLine.setVisibility(View.GONE);
            mBinding.tvUartLine.setVisibility(View.VISIBLE);
            mBinding.rlEthAppList.setVisibility(View.GONE);
            mBinding.rlUartAppList.setVisibility(View.VISIBLE);
        } else if (view.getId() == R.id.tv_eth_recommend_all) {
            Intent intent = new Intent(requireActivity(),DAppsListActivity.class);
            intent.putExtra("title","ETH-推荐");
            intent.putExtra("type","2");
            startActivity(intent);
        } else if (view.getId() == R.id.tv_eth_transfer_all) {
            Intent intent = new Intent(requireActivity(),DAppsListActivity.class);
            intent.putExtra("title","ETH-交易");
            intent.putExtra("type","3");
            startActivity(intent);
        } else if (view.getId() == R.id.tv_uart_recommend_all) {
            Intent intent = new Intent(requireActivity(),DAppsListActivity.class);
            intent.putExtra("title","UART-推荐");
            intent.putExtra("type","4");
            startActivity(intent);
        } else if (view.getId() == R.id.tv_uart_transfer_all) {
            Intent intent = new Intent(requireActivity(),DAppsListActivity.class);
            intent.putExtra("title","UART-交易");
            intent.putExtra("type","5");
            startActivity(intent);
        }
    }
}
