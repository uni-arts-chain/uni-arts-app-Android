package com.yunhualian.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.upbest.arouter.Extras;
import com.yunhualian.R;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentDeriveKeystoreQrcodeBinding;
import com.yunhualian.utils.ZxingEncodingUtils;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Single;


//import com.zxing.support.library.qrcode.QRCodeEncode;

/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */

public class ExportKeystoreQRCodeFragment extends BaseFragment<FragmentDeriveKeystoreQrcodeBinding> {

    String walletKeystore;

    public void attachView() {
        Bundle arguments = getArguments();
        walletKeystore = arguments.getString("walletKeystore");
    }

    public void initDatas() {
    }

    public void configViews() {

        mBinding.sellAction.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onClick(View view) {
                Single.fromCallable(
                        () -> {
                            return QRCodeEncoder.syncEncodeQRCode(walletKeystore, 340);
                        }
                ).subscribe(bitmap -> {
                    mBinding.ivKeystore.setVisibility(View.VISIBLE);
                    mBinding.ivKeystore.setImageBitmap(bitmap);
                    mBinding.coverLayout.setVisibility(View.INVISIBLE);
                });// GlideImageLoader.loadBmpImage(mBinding.ivKeystore, bitmap, -1)

            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_derive_keystore_qrcode;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        attachView();
        initDatas();
        configViews();
    }
}
