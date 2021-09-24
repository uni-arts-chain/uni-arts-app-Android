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

import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.R;
import com.gammaray.adapter.MyHomePageAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityWalletDetailLayoutBinding;
import com.gammaray.eth.domain.ETHWallet;
import com.gammaray.eth.interact.FetchWalletInteract;
import com.gammaray.ui.fragment.PersonalAssertFragment;
import com.gammaray.utils.SharedPreUtils;
import com.gammaray.widget.QrPopUpWindow;
import com.google.android.material.tabs.TabLayout;
import com.upbest.arouter.Extras;

import java.util.Arrays;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class WalletsDetailActivity extends BaseActivity<ActivityWalletDetailLayoutBinding> implements MyHomePageAdapter.TabPagerListener, View.OnClickListener {

    private MyHomePageAdapter mAdapter;
    private PersonalAssertFragment mTokenFragment;
    private PersonalAssertFragment mNftFragment;
    private QrPopUpWindow qrPopUpWindow;
    private String mWalletName;
    private String mHeadImgUrl;
    private String mWalletAddress;
    private FetchWalletInteract fetchWalletInteract;
    private ETHWallet mEthWallet;

//    private UploadSuccessPopUpWindow uploadSuccessPopUpWindow;

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
        fetchWalletInteract = new FetchWalletInteract();
        fetchWalletInteract.findDefault().subscribe(this::getCurrentWallet);

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
    }

    @Override
    public Fragment getFragment(int position) {
        if (position == 0) {
            return PersonalAssertFragment.newInstance("TOKEN");
        } else {
            return PersonalAssertFragment.newInstance("NFT");
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
