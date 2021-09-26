package com.gammaray.ui.activity;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gammaray.R;
import com.gammaray.adapter.WalletLinksAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityWalletLinksLayoutBinding;
import com.gammaray.entity.WalletLinkBean;
import com.gammaray.ui.activity.wallet.AcountActivity;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class WalletLinksActivity extends BaseActivity<ActivityWalletLinksLayoutBinding> {

    private WalletLinkBean mUartLink = new WalletLinkBean("UART", "UniArt", R.mipmap.icon_uart);
    private WalletLinkBean mETHLink = new WalletLinkBean("ETH", "Ethereum", R.mipmap.icon_eth);
    private List<WalletLinkBean> mList = new ArrayList<>();
    private WalletLinksAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_links_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions toolBarOptions = new ToolBarOptions();
        toolBarOptions.titleString = "选择链类型";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);

        EventBus.getDefault().register(this);

        mList.add(mUartLink);
        mList.add(mETHLink);

        mAdapter = new WalletLinksAdapter(mList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mDataBinding.rvWalletLink.setLayoutManager(layoutManager);
        mDataBinding.rvWalletLink.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (position == 0) {
                startActivity(AcountActivity.class);
            } else if (position == 1) {
                startActivity(SelectedWalletsActivity.class);
            }
        });
    }

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
