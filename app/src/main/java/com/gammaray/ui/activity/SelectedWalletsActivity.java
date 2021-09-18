package com.gammaray.ui.activity;

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

import java.util.ArrayList;
import java.util.List;

/*
 * ETH钱包列表页面
 * */
public class SelectedWalletsActivity extends BaseActivity<ActivitySelectedWalletsLayoutBinding> {

    private List<ETHWallet> walletList = new ArrayList<>();
    private FetchWalletInteract fetchWalletInteract;
    private SelectedWalletAdapter adapter;

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

        fetchWalletInteract = new FetchWalletInteract();

        initRecyclerView();

        fetchWalletInteract.findDefault().subscribe(this::getCurrentWallet);
        mDataBinding.btnImportWallet.setOnClickListener(view -> {
            startActivity(ETHImportWalletActivity.class);
        });
    }

    private void initRecyclerView() {
        adapter = new SelectedWalletAdapter(this, walletList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mDataBinding.rvWallets.setLayoutManager(layoutManager);
        mDataBinding.rvWallets.setAdapter(adapter);
        adapter.setOnItemClickListener((adapter, view, position) -> {

        });
    }

    private void getCurrentWallet(ETHWallet ethWallet) {
        if (ethWallet != null) {
            walletList.clear();
            walletList.add(ethWallet);
            adapter.setNewData(walletList);
        }
    }
}
