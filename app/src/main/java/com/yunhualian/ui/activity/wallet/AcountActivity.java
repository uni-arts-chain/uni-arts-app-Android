package com.yunhualian.ui.activity.wallet;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ThreadUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;
import com.upbest.arouter.Extras;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityAcountBinding;
import com.yunhualian.ui.activity.user.MyHomePageActivity;
import com.yunhualian.ui.activity.user.UploadArtActivity;
import com.yunhualian.utils.DialogManager;
import com.yunhualian.utils.SharedPreUtils;
import com.yunhualian.utils.ZxingEncodingUtils;
import com.yunhualian.widget.QrPopUpWindow;
import com.yunhualian.widget.UploadSuccessPopUpWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import jp.co.soramitsu.feature_wallet_impl.presentation.balance.list.BalanceListFragment;

public class AcountActivity extends BaseActivity<ActivityAcountBinding> {
    QrPopUpWindow qrPopUpWindow;
    UploadSuccessPopUpWindow uploadSuccessPopUpWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_acount;
    }

    @Override
    public void initPresenter() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusMessageEvent eventBusMessageEvent) {
        if (eventBusMessageEvent != null) {
            if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_ADDRESS_COPY)) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                if (cm != null) {
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", eventBusMessageEvent.getmValue().toString());
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    ToastUtils.showShort("复制成功");
                }
            } else if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_SHOW_QR)) {
                createQrcode(eventBusMessageEvent.getmValue().toString());
            } else if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_IMPORT_WALLET)) {
                showConfirmDialog();
            } else if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_IMPORT_COMPLETE)) {
                finish();
            }
        }
    }

    private void showConfirmDialog() {
        uploadSuccessPopUpWindow = new UploadSuccessPopUpWindow(AcountActivity.this, clickListener);
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
            startActivity(ImportWalletActivity.class);
            finish();
        }
    };

    @Override
    public void initView() {

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.count_bage;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        if (YunApplication.getmUserVo() != null && YunApplication.getmUserVo().getAvatar() != null)
            if (!TextUtils.isEmpty(YunApplication.getmUserVo().getAvatar().getUrl()))
                Extras.headUrl = YunApplication.getmUserVo().getAvatar().getUrl();
            else Extras.headUrl = null;
        EventBus.getDefault().register(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        BalanceListFragment otcFragment = new BalanceListFragment();
        Extras.isShow = true;
        mFragmentTransaction.add(R.id.container, otcFragment, "");
        mFragmentTransaction.show(otcFragment);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    private void showPopWindow(Bitmap bitmap) {
        qrPopUpWindow = new QrPopUpWindow(this);
        qrPopUpWindow.setImage(bitmap);
        qrPopUpWindow.showAtLocation(mDataBinding.parent, Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void createQrcode(final String mUrl) {
        if (TextUtils.isEmpty(mUrl)) return;
        showLoading(R.string.progress_loading);
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Bitmap>() {
            @Nullable
            @Override
            public Bitmap doInBackground() throws Throwable {

                return QRCodeEncoder.syncEncodeQRCode(mUrl, 500);
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
}