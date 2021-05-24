package com.yunhualian.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.databinding.ActivityQrScanBinding;

import java.util.List;

import cn.bingoogolapple.qrcode.core.BarcodeType;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class QrScanActivity extends BaseActivity<ActivityQrScanBinding> implements EasyPermissions.PermissionCallbacks, View.OnClickListener, QRCodeView.Delegate {
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    private static final int QRCODE_RESULT = 124;
    private RelativeLayout rlFlashLight;
    private LinearLayout llBack;

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr_scan;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDataBinding.zxingview.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQRCodePermissions();

        mDataBinding.zxingview.startCamera();

        mDataBinding.zxingview.changeToScanQRCodeStyle();
        mDataBinding.zxingview.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
        mDataBinding.zxingview.startSpotAndShowRect();   // 显示扫描框，并开始识别
        mDataBinding.backAction.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        mDataBinding.zxingview.stopCamera();
        super.onDestroy();
    }

    @Override
    public void initView() {
        mDataBinding.zxingview.setDelegate(this);
    }

    @Override
    public void onClick(View v) {

    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        mDataBinding.zxingview.startCamera();
        mDataBinding.zxingview.startSpotAndShowRect();   // 显示扫描框，并开始识别
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();

        Intent intent = new Intent();
        intent.putExtra("scan_result", result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        String tipText = mDataBinding.zxingview.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mDataBinding.zxingview.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mDataBinding.zxingview.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtils.showShort("打开相机出错");
    }
}