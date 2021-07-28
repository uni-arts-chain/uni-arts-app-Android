package com.yunhualian.ui.activity.wallet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityWithdrawLayoutBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 提现上传二维码页面
 */
public class WithdrawActivity extends BaseActivity<ActivityWithdrawLayoutBinding> implements TakePhoto.TakeResultListener, InvokeListener, View.OnClickListener {

    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    private File file;
    private Uri imageUri;
    private int MAX_SIZE = 5 * 1024;
    List<File> fileList = new ArrayList<>();
    private boolean bIsZFBCode;


    @Override
    public int getLayoutId() {
        return R.layout.activity_withdraw_layout;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.with_draw;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        mDataBinding.imgZfbAdd.setOnClickListener(this);
        mDataBinding.imgWxAdd.setOnClickListener(this);
        mDataBinding.btnWithdraw.setOnClickListener(this);
        initPhoto();
    }

    @Override
    public void takeSuccess(TResult result) {
        if (result != null) {
            File file = new File(result.getImages().get(0).getOriginalPath());
            fileList.add(file);
            updatePageInfo();
            updateButtonStatus();
            initPhoto();//初始化保存路径
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.e("takeFail", msg);
    }

    @Override
    public void takeCancel() {

    }

    private void updateButtonStatus() {

    }

    private void updatePageInfo() {
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        if (fileList != null && fileList.size() > 0) {
            if (fileList.size() == 1) {
                if (bIsZFBCode) {
                    mDataBinding.imgZfbCode.setVisibility(View.VISIBLE);
                    mDataBinding.imgZfbAdd.setVisibility(View.GONE);
                    mDataBinding.imgDeleteZfb.setVisibility(View.VISIBLE);
                    Glide.with(this).load(fileList.get(0).getAbsolutePath()).apply(options).into(mDataBinding.imgZfbCode);
                } else {
                    mDataBinding.imgWxCode.setVisibility(View.VISIBLE);
                    mDataBinding.imgWxAdd.setVisibility(View.GONE);
                    mDataBinding.imgDeleteWx.setVisibility(View.VISIBLE);
                    Glide.with(this).load(fileList.get(0).getAbsolutePath()).apply(options).into(mDataBinding.imgWxCode);
                }
            } else {

            }
        } else {

        }
    }

    private void removeAll() {

    }


    /**
     * 获取TakePhoto实例
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }


    /**
     * 初始化本地存储路径
     */
    public void initPhoto() {
        file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        imageUri = Uri.fromFile(file);
    }

    private CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        return builder.create();
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_zfb_add:
                bIsZFBCode = true;
                takePhoto.onEnableCompress(new CompressConfig.Builder().enablePixelCompress(false).enableQualityCompress(false).setMaxSize(MAX_SIZE).create(), false);
                takePhoto.onPickFromGallery();
                break;

            case R.id.img_wx_add:
                bIsZFBCode = false;
                takePhoto.onEnableCompress(new CompressConfig.Builder().enablePixelCompress(false).enableQualityCompress(false).setMaxSize(MAX_SIZE).create(), false);
                takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                break;

            case R.id.img_zfb_code:

                break;
            case R.id.btn_withdraw:

                break;
        }
    }
}