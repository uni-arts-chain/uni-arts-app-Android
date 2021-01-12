
package com.yunhualian.ui.activity;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.app.TakePhotoImpl;
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
import com.yunhualian.databinding.ActivitySettingsBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends BaseActivity<ActivitySettingsBinding> implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {
    Button takePhotoBtn;
    Button selectPhoto;
    Button cancle;
    private PopupWindow popupWindow;
    private View contentView;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    File file;
    Uri imageUri;
    private boolean isUpload = false;
    private boolean isLoginOut = false;
    private boolean updateHeadImg = false;
    private String newHeadImg = "";

    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime = 0;
    private List<String> mLackedPermission = new ArrayList<>();
    ImageView headImag;

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

    public void initPopWindow() {
        contentView = LayoutInflater.from(SettingsActivity.this).inflate(
                R.layout.layout_pop_selector, null);
        takePhotoBtn = contentView.findViewById(R.id.take_photo);
        selectPhoto = contentView.findViewById(R.id.select_photo);
        cancle = contentView.findViewById(R.id.cancle);
        takePhotoBtn.setOnClickListener(popClick);
        selectPhoto.setOnClickListener(popClick);
        cancle.setOnClickListener(popClick);
        popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);// 取得焦点
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //点击外部消失
        popupWindow.setOutsideTouchable(true);
        //设置可以点击
        popupWindow.setTouchable(true);
        //进入退出的动画，指定刚才定义的style
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        initPhoto();//初始化拍照参数
    }

    private View.OnClickListener popClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
            switch (v.getId()) {
                case R.id.take_photo:
                    takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                    break;
                case R.id.select_photo:
                    takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                    break;
                case R.id.cancle:
                    popupWindow.dismiss();
                    break;
            }
        }
    };

    public void showPopwindow() {
        popupWindow.showAtLocation(contentView, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
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

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    private CropOptions getCropOptions() {

        int height = 800;
        int width = 800;

        CropOptions.Builder builder = new CropOptions.Builder();

        if (false) {
            builder.setAspectX(width).setAspectY(height);
        } else {
            builder.setOutputX(width).setOutputY(height);
        }
        builder.setWithOwnCrop(true);
        return builder.create();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.settings;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        initPopWindow();
        mDataBinding.headLayout.setOnClickListener(this);
        mDataBinding.verifiedLayout.setOnClickListener(this);
        headImag = mDataBinding.headLayout.findViewById(R.id.mine_title_img);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.head_layout:
                showPopwindow();
                break;
            case R.id.verifiedLayout:
                goVerified();
                break;

        }
    }

    @Override
    public void takeSuccess(TResult result) {
        if (result != null) {
            String url = result.getImage().getOriginalPath();
            Glide.with(this).load(url).into(headImag);
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    private void goVerified() {
        if (!PermissionUtils.isGranted(PermissionConstants.getPermissions(PermissionConstants.CAMERA))) {
            mLackedPermission.add(PermissionConstants.CAMERA);
        }
        if (null != mLackedPermission && !mLackedPermission.isEmpty()) {
            PermissionUtils.permission(mLackedPermission.toArray(new String[0])).callback(new PermissionUtils.FullCallback() {
                @Override
                public void onGranted(List<String> permissionsGranted) {
                    if (permissionsGranted.containsAll(Arrays.asList(PermissionConstants.getPermissions(PermissionConstants.CAMERA)))) {
                        startActivity(VerifiedActivity.class);
                    }
                }

                @Override
                public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                }
            }).request();
            return;
        }
        startActivity(VerifiedActivity.class);
    }
}