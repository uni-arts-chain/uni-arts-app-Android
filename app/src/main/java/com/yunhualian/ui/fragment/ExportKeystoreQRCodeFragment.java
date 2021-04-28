package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.yunhualian.R;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentDeriveKeystoreQrcodeBinding;
import com.yunhualian.utils.ZxingEncodingUtils;

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

        mBinding.ivKeystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Single.fromCallable(
                        () -> {
//                            return QRCodeEncoder.syncEncodeQRCode(walletKeystore, BGAQRCodeUtil.dp2px(YunApplication.getInstance()
//                                    , 240), Color.parseColor("#000000"));
                            return ZxingEncodingUtils.createQRCodeNative(walletKeystore, 340, 340, null);
                        }
                ).subscribe(bitmap -> mBinding.ivKeystore.setImageBitmap(bitmap));// GlideImageLoader.loadBmpImage(mBinding.ivKeystore, bitmap, -1)

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
