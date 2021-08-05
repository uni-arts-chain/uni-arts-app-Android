
package com.yunhualian.ui.activity.user;


import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
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
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.igexin.sdk.PushManager;
import com.jph.takephoto.app.TakePhoto;
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
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivitySettingsBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.AboutUsActivity;
import com.yunhualian.ui.activity.AdviceActivity;
import com.yunhualian.ui.activity.VerifiedActivity;
import com.yunhualian.utils.SharedPreUtils;
import com.yunhualian.utils.StringUtils;
import com.yunhualian.widget.BasePopupWindow;


import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

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
    UserVo userVo;
    RequestOptions requestOptions;

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
        popupWindow = new BasePopupWindow(this);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(contentView);
        //注意  要是点击外部空白处弹框消息  那么必须给弹框设置一个背景色  不然是不起作用的
//        popupWindow.setFocusable(true);
//        //点击外部消失
//        popupWindow.setOutsideTouchable(true);
//        //设置可以点击
//        popupWindow.setTouchable(true);
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
        popupWindow.showAtLocation(mDataBinding.content, Gravity.CENTER, 0, 0);
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
        mDataBinding.nickLayout.setOnClickListener(this);
        mDataBinding.decsLayout.setOnClickListener(this);
        mDataBinding.wechatLayout.setOnClickListener(this);
        mDataBinding.loginPswLayout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.head_layout:
                showPopwindow();
                break;
            case R.id.verifiedLayout:
//                goVerified();
                break;

            case R.id.nickLayout:
                startActivity(EditNickNameActivity.class);
                break;
            case R.id.decsLayout:
                Bundle bundle = new Bundle();
                if (!TextUtils.isEmpty(userVo.getDesc()))
                    bundle.putString(UserDescActivity.DEFULT_DESC, userVo.getDesc());
                startActivity(UserDescActivity.class, bundle);
                break;
            case R.id.wechatLayout:
                startActivity(AdviceActivity.class);
                break;
            case R.id.loginPswLayout:
                startActivity(AboutUsActivity.class);
                break;
            case R.id.phoneLayout:
                if (TextUtils.isEmpty(YunApplication.getmUserVo().getPhone_number()))
                    startActivity(BindPhoneActivity.class);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (YunApplication.getmUserVo() != null)
            initPageData();
    }

    private void initPageData() {
        userVo = YunApplication.getmUserVo();
        Glide.with(this)
                .load(userVo.getAvatar().getUrl())
                .apply(new RequestOptions().placeholder(R.mipmap.icon_default_head)).into(mDataBinding.mineTitleImg);
        if (TextUtils.isEmpty(userVo.getDisplay_name())) {
            mDataBinding.nickName.setText(R.string.set_display_name_tips);
        } else mDataBinding.nickName.setText(userVo.getDisplay_name());

        if (!TextUtils.isEmpty(userVo.getPhone_number()) && userVo.getPhone_number().length() > 10) {
            String phone = userVo.getPhone_number().length() == 13 ? userVo.getPhone_number().substring(2) : userVo.getPhone_number();
            mDataBinding.userPhone.setText(phone);
            mDataBinding.phoneIcon.setVisibility(View.INVISIBLE);
        } else {
            mDataBinding.phoneLayout.setOnClickListener(this);
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        if (result != null) {
            String url = result.getImage().getOriginalPath();
//            Glide.with(this).clear(headImag);
//            Glide.with(this)
//                    .load(url).into(headImag);
            upload(new File(url));
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

    public void upload(File file) {
        LogUtils.e("fileName = =" + StringUtils.getFileNameNoEx(file.getName()));
        RequestBody requestFrontFile = RequestBody.create(MediaType.parse("image/jpeg"), file);
        RequestBody mRequestBody = new MultipartBody.Builder()
                .addFormDataPart("avatar", StringUtils.getFileNameNoEx(file.getName()))
                .addFormDataPart("avatar", StringUtils.getFileNameNoEx(file.getName()), requestFrontFile)
                .setType(MultipartBody.FORM)
                .build();
        RequestManager.instance().uploadIdImages(mRequestBody, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess
                    (Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                if (response.body().isSuccessful()) {
                    if (response.body().getBody() != null) {
                        String headURL = response.body().getBody().getAvatar().getUrl();
                        if (!TextUtils.isEmpty(headURL)) {
                            YunApplication.getmUserVo().getAvatar().setUrl(headURL);
                            SharedPreUtils.setString(SettingsActivity.this, SharedPreUtils.KEY_HEAD_URL, headURL);
                        }
                        initPageData();
                    }
                    ToastUtils.showLong("上传成功");
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    public void login() {

    }


}