package com.yunhualian.ui.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.upbest.arouter.Extras;
import com.yunhualian.R;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.databinding.FragmentDeriveKeystoreStringBinding;


/**
 * Created by Tiny 熊 @ Upchain.pro
 * WeiXin: xlbxiong
 */

public class ExportKeystoreStringFragment extends BaseFragment<FragmentDeriveKeystoreStringBinding> {


    public void initDatas() {
        Bundle arguments = getArguments();
        String walletKeystore = arguments.getString("walletKeystore");
        mBinding.tvKeystore.setText(walletKeystore);
    }


    public void copyText() {
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        // 将文本内容放到系统剪贴板里。
        if (cm != null) {
            // 创建普通字符型ClipData
            ClipData mClipData = ClipData.newPlainText("Label", mBinding.tvKeystore.getText().toString());
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData);
        }
        mBinding.btnCopy.setText(R.string.derive_private_key_already_copy_btn);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_derive_keystore_string;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        initDatas();
        mBinding.btnCopy.setOnClickListener(v -> copyText());
    }
}
