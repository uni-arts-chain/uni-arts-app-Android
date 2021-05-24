package com.yunhualian.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.databinding.ActivityLinkSearchBinding;

import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class LinkSearchActivity extends BaseActivity<ActivityLinkSearchBinding> {


    @Override
    public int getLayoutId() {
        return R.layout.activity_link_search;
    }

    @Override
    public void initPresenter() {
        initTitle();
    }

    @Override
    public void initView() {

    }

    public void initTitle() {
        TextView title = mDataBinding.toolbar.findViewById(R.id.search_edt);
        title.setText(R.string.link_search);
        mDataBinding.toolbar.findViewById(R.id.layout_back).setOnClickListener(view -> finish());

        mDataBinding.scan.setOnClickListener(view -> scan());
    }

    public void scan() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // android 6.0以上需要动态申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 访问手机存储申请权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // android 6.0以上需要动态申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(this, QrScanActivity.class);
        startActivityForResult(intent, AppConstant.REQ_QR_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstant.REQ_QR_CODE || requestCode == AppConstant.REQUEST_CODE_SCAN_GALLERY) {
                //扫描二维码或者扫描相册成功
                Bundle bundle = data.getExtras();
                ToastUtils.showShort("扫描成功");
                //将扫描出的信息显示出来
                //textView.setText(scanResult);
//                DialogManager.showConfirmDialog("温馨提示",scanResult);
            }
        } else {
            ToastUtils.showShort("取消扫描");
        }
    }
}