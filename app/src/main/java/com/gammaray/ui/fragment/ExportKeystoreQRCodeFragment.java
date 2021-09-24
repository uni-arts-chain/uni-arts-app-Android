package com.gammaray.ui.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.gammaray.R;
import com.gammaray.base.BaseFragment;
import com.gammaray.databinding.FragmentDeriveKeystoreQrcodeBinding;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import io.reactivex.Single;


//import com.zxing.support.library.qrcode.QRCodeEncode;

/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */

public class ExportKeystoreQRCodeFragment extends BaseFragment<FragmentDeriveKeystoreQrcodeBinding> {

    String walletKeystore;

    public static BaseFragment newInstance(String keyStore) {
        ExportKeystoreQRCodeFragment fragment = new ExportKeystoreQRCodeFragment();
        Bundle args = new Bundle();
        args.putString("walletKeystore", keyStore);
        fragment.setArguments(args);
        return fragment;
    }

    public void attachView() {
        Bundle arguments = getArguments();
        if(arguments != null){
            walletKeystore = arguments.getString("walletKeystore");
        }
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
