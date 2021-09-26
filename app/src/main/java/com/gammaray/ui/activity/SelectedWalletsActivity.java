package com.gammaray.ui.activity;

import android.content.Entity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gammaray.R;
import com.gammaray.adapter.SelectedWalletAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivitySelectedWalletsLayoutBinding;
import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.interact.FetchWalletInteract;
import com.gammaray.ui.activity.wallet.AcountActivity;
import com.gammaray.ui.activity.wallet.ImportWalletActivity;
import com.gammaray.widget.UploadSuccessPopUpWindow;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import jnr.ffi.annotations.In;

/*
 * ETH钱包列表页面
 * */
public class SelectedWalletsActivity extends BaseActivity<ActivitySelectedWalletsLayoutBinding> {

    private List<ETHWallet> walletList = new ArrayList<>();
    private FetchWalletInteract fetchWalletInteract;
    private SelectedWalletAdapter adapter;
    UploadSuccessPopUpWindow uploadSuccessPopUpWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_selected_wallets_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions toolBarOptions = new ToolBarOptions();
        toolBarOptions.titleString = "ETH";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);

        EventBus.getDefault().register(this);

        fetchWalletInteract = new FetchWalletInteract();
        fetchWalletInteract.findDefault().subscribe(this::getCurrentWallet);

        initRecyclerView();
        mDataBinding.btnImportWallet.setOnClickListener(view -> {
            showConfirmDialog();
        });
    }

    private void initRecyclerView() {
        adapter = new SelectedWalletAdapter(this, walletList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDataBinding.rvWallets.setLayoutManager(layoutManager);
        mDataBinding.rvWallets.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(SelectedWalletsActivity.this, WalletsDetailActivity.class);
            intent.putExtra("wallet_name", walletList.get(position).getName());
            intent.putExtra("wallet_address", walletList.get(position).getAddress());
            startActivity(intent);
        });
    }

    private void getCurrentWallet(ETHWallet ethWallet) {
        if (ethWallet != null) {
            walletList.clear();
            walletList.add(ethWallet);
            adapter.setNewData(walletList);
        }
    }

    private void showConfirmDialog() {
        uploadSuccessPopUpWindow = new UploadSuccessPopUpWindow(SelectedWalletsActivity.this, clickListener);
        uploadSuccessPopUpWindow.setContent(getString(R.string.import_wallet_tips));
        uploadSuccessPopUpWindow.setConfirmText(getString(R.string.confirm_import));
        uploadSuccessPopUpWindow.showAtLocation(mDataBinding.parent, Gravity.CENTER, 0, 0);

    }

    private UploadSuccessPopUpWindow.OnBottomTextviewClickListener clickListener = new UploadSuccessPopUpWindow.OnBottomTextviewClickListener() {
        @Override
        public void onCancleClick() {
            if (uploadSuccessPopUpWindow.isShowing()) uploadSuccessPopUpWindow.dismiss();
        }

        @Override
        public void onPerformClick() {
            startActivity(ETHImportWalletActivity.class);
            finish();
        }
    };


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusMessageEvent eventBusMessageEvent) {
        if (eventBusMessageEvent != null) {
            if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_IMPORT_ETH_SUCCESS)) {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
