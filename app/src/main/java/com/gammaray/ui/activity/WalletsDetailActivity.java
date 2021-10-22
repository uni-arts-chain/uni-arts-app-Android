package com.gammaray.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gammaray.R;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.base.YunApplication;
import com.gammaray.databinding.ActivityWalletDetailLayoutBinding;
import com.gammaray.entity.UserVo;
import com.gammaray.entity.WalletTokenBean;
import com.gammaray.eth.base.C;
import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.entity.NetworkInfo;
import com.gammaray.eth.entity.Token;
import com.gammaray.eth.entity.TokenInfo;
import com.gammaray.eth.interact.FetchWalletInteract;
import com.gammaray.eth.repository.EthereumNetworkRepository;
import com.gammaray.eth.ui.AddTokenActivity;
import com.gammaray.eth.viewmodel.AddTokenViewModel;
import com.gammaray.eth.viewmodel.AddTokenViewModelFactory;
import com.gammaray.eth.viewmodel.TokensViewModel;
import com.gammaray.eth.viewmodel.TokensViewModelFactory;
import com.gammaray.ui.fragment.ETHPersonalAssertFragment;
import com.gammaray.ui.fragment.PersonalAssertFragment;
import com.gammaray.widget.QrPopUpWindow;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class WalletsDetailActivity extends BaseActivity<ActivityWalletDetailLayoutBinding> implements MyHomePageAdapter.TabPagerListener, View.OnClickListener {

    private MyHomePageAdapter mAdapter;
    private PersonalAssertFragment mTokenAssertFragment = new PersonalAssertFragment();
    private QrPopUpWindow qrPopUpWindow;
    private String mWalletName;
    private String mHeadImgUrl;
    private String mWalletAddress;
    private FetchWalletInteract fetchWalletInteract;
    private ETHWallet mEthWallet;
    private TokensViewModelFactory tokensViewModelFactory;
    private TokensViewModel tokensViewModel;
    //    private UploadSuccessPopUpWindow uploadSuccessPopUpWindow;
    private EthereumNetworkRepository ethereumNetworkRepository;
    protected AddTokenViewModelFactory addTokenViewModelFactory;
    private AddTokenViewModel addTokenViewModel;
    private List<WalletTokenBean> mWallets = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_detail_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions toolBarOptions = new ToolBarOptions();
        toolBarOptions.titleString = "ETH";
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, toolBarOptions);
        if (getIntent() != null) {
            mWalletName = getIntent().getStringExtra("wallet_name");
            mWalletAddress = getIntent().getStringExtra("wallet_address");
        }
        tokensViewModelFactory = new TokensViewModelFactory();
        tokensViewModel = ViewModelProviders.of(this, tokensViewModelFactory)
                .get(TokensViewModel.class);

        addTokenViewModelFactory = new AddTokenViewModelFactory();
        addTokenViewModel = ViewModelProviders.of(this, addTokenViewModelFactory).get(AddTokenViewModel.class);
        ethereumNetworkRepository = YunApplication.repositoryFactory().ethereumNetworkRepository;
        NetworkInfo networkInfo = EthereumNetworkRepository.sSelf.getEthNetWork();
        AddTokenActivity.TokenItem item = new AddTokenActivity.TokenItem(new TokenInfo("", C.ETH_NAME, C.ETH_SYMBOL, 18, ethereumNetworkRepository.ethNetWork.rpcServerUrl, ""), true, 0);
        addTokenViewModel.save(item.tokenInfo.name, item.tokenInfo.address, item.tokenInfo.symbol, item.tokenInfo.decimals, networkInfo, item.tokenInfo.icon);

        mDataBinding.name.setText(mWalletName);
        mDataBinding.addr.setText(mWalletAddress);
        mDataBinding.copyAddress.setOnClickListener(this);
        mDataBinding.showQr.setOnClickListener(this);
        mDataBinding.imgSetting.setOnClickListener(this);
        mAdapter = new MyHomePageAdapter(getSupportFragmentManager(), 2, Arrays.asList(getResources().getStringArray(R.array.personal_tabs)), this);
        mAdapter.setListener(this);
        mDataBinding.viewpager.setAdapter(mAdapter);
        mDataBinding.tabLayout.setupWithViewPager(mDataBinding.viewpager);
        mDataBinding.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tokensViewModel.tokens().observe(this, this::onTokens);
        getEthRemains();
        UserVo userVo = YunApplication.getmUserVo();
        if (userVo != null){
            Glide.with(YunApplication.getInstance())
                    .load(userVo.getAvatar().getUrl())
                    .apply(new RequestOptions().placeholder(R.mipmap.icon_default_head)).into(mDataBinding.imageview);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchWalletInteract = new FetchWalletInteract();
        fetchWalletInteract.findDefault().subscribe(this::getCurrentWallet);
    }

    private void getEthRemains() {
        showLoading(R.string.progress_loading);
        tokensViewModel.prepare();
    }

    private void onTokens(Token[] tokens) {
        dismissLoading();
        if (tokens.length > 0) {
            WalletTokenBean tokenBean = new WalletTokenBean("ETH", "Ethereum", R.mipmap.icon_eth, tokens[0].balance);
            mWallets.add(tokenBean);
            mTokenAssertFragment.updateData(mWallets);
        }
    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0) {
            return mTokenAssertFragment;
        } else {
            return ETHPersonalAssertFragment.newInstance(mWalletAddress);
        }
    }

    private void showPopWindow(Bitmap bitmap) {
        qrPopUpWindow = new QrPopUpWindow(this);
        qrPopUpWindow.setImage(bitmap);
        qrPopUpWindow.showAtLocation(mDataBinding.parent, Gravity.CENTER, 0, 0);
    }


    private void createQrcode(final String address) {
        if (TextUtils.isEmpty(address)) return;
        showLoading(R.string.progress_loading);
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Bitmap>() {
            @Nullable
            @Override
            public Bitmap doInBackground() throws Throwable {

                return QRCodeEncoder.syncEncodeQRCode(address, 500);
            }

            @Override
            public void onSuccess(@Nullable Bitmap result) {
                dismissLoading();
                if (null != result)
                    showPopWindow(result);
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

    private void getCurrentWallet(ETHWallet ethWallet) {
        if (ethWallet != null) {
            mEthWallet = ethWallet;
            mDataBinding.name.setText(mEthWallet.getName());
            mDataBinding.addr.setText(mEthWallet.getAddress());
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.copyAddress) {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // 将文本内容放到系统剪贴板里。
            if (cm != null) {
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", mWalletAddress);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                ToastUtils.showShort("复制成功");
            }
        } else if (view.getId() == R.id.showQr) {
            createQrcode(mWalletAddress);
        } else if (view.getId() == R.id.img_setting) {
            Intent intent = new Intent(WalletsDetailActivity.this, WalletExportActivity.class);
            intent.putExtra("walletId", mEthWallet.getId());
            intent.putExtra("walletPwd", mEthWallet.getPassword());
            intent.putExtra("walletAddress", mEthWallet.getAddress());
            intent.putExtra("walletName", mEthWallet.getName());
            intent.putExtra("walletMnemonic", mEthWallet.getMnemonic());
            intent.putExtra("walletIsBackup", mEthWallet.getIsBackup());
            intent.putExtra("isCurrent", mEthWallet.isCurrent());
            intent.putExtra("fromList", true);
            startActivity(intent);
        }
    }
}
