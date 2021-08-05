package com.yunhualian.ui.activity.wallet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.yunhualian.R;
import com.yunhualian.adapter.UploadCodeBean;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityWithdrawLayoutBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

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
    private RequestOptions options;

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
        options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        mDataBinding.imgZfbAdd.setOnClickListener(this);
        mDataBinding.imgWxAdd.setOnClickListener(this);
        mDataBinding.imgDeleteZfb.setOnClickListener(this);
        mDataBinding.imgDeleteWx.setOnClickListener(this);
        mDataBinding.btnWithdraw.setOnClickListener(this);
        initPhoto();
        initBtnStatus();
    }

    @Override
    public void takeSuccess(TResult result) {
        if (result != null) {
            File file = new File(result.getImages().get(0).getOriginalPath());
            fileList.add(file);
            updatePageInfo();
            updateButtonStatus();
//            initPhoto();//初始化保存路径
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

        if (fileList != null && fileList.size() > 0) {
            if (fileList.size() == 1) {
                if (bIsZFBCode) {
                    showZfbCode(0);
                    mDataBinding.llZfbCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
                } else {
                    showWxCode(0);
                    mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
                }
            } else {
                showZfbCode(0);
                showWxCode(1);
                mDataBinding.llZfbCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
            }
        }
        initBtnStatus();
    }

    private void showZfbCode(int index) {
        mDataBinding.imgZfbCode.setVisibility(View.VISIBLE);
        mDataBinding.imgDeleteZfb.setVisibility(View.VISIBLE);
        mDataBinding.rlZfbSelect.setVisibility(View.VISIBLE);
        Glide.with(this).load(fileList.get(index).getAbsolutePath()).apply(options).into(mDataBinding.imgZfbCode);
    }

    private void showWxCode(int index) {
        mDataBinding.imgWxCode.setVisibility(View.VISIBLE);
        mDataBinding.imgDeleteWx.setVisibility(View.VISIBLE);
        if (index == 1) {
            mDataBinding.rlWxSelect.setVisibility(View.GONE);
        } else {
            mDataBinding.rlWxSelect.setVisibility(View.VISIBLE);
        }
        Glide.with(this).load(fileList.get(index).getAbsolutePath()).apply(options).into(mDataBinding.imgWxCode);
    }

    private void removeAll() {

    }


    private void initBtnStatus() {
        if (fileList != null && fileList.size() > 0) {
            mDataBinding.btnWithdraw.setVisibility(View.VISIBLE);
            mDataBinding.tvHint.setVisibility(View.VISIBLE);
        } else {
            mDataBinding.btnWithdraw.setVisibility(View.GONE);
            mDataBinding.tvHint.setVisibility(View.GONE);
        }
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

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    private void uploadCode() {
        showLoading("正在上传...");
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);

        if (fileList == null || fileList.size() == 0) {
            return;
        }
        if (fileList.size() == 1) {
            File file = fileList.get(0);
            RequestBody requestFrontFile = RequestBody.create(MediaType.parse("image/*"), file);
            if (mDataBinding.imgZfbCode.getVisibility() == View.VISIBLE) {
                multipartBody.addFormDataPart("alipay_img", StringUtils.getFileNameNoEx(file.getName()), requestFrontFile);
            } else if (mDataBinding.imgWxCode.getVisibility() == View.VISIBLE) {
                multipartBody.addFormDataPart("weixin_img", StringUtils.getFileNameNoEx(file.getName()), requestFrontFile);
            }
        } else {
            for (int i = 0; i < fileList.size(); i++) {
                File file = fileList.get(i);
                RequestBody requestFrontFile = RequestBody.create(MediaType.parse("image/*"), file);
                if (i == 0) {
                    multipartBody.addFormDataPart("alipay_img", StringUtils.getFileNameNoEx(file.getName()), requestFrontFile);
                } else if (i == 1) {
                    multipartBody.addFormDataPart("weixin_img", StringUtils.getFileNameNoEx(file.getName()), requestFrontFile);
                }

            }
        }
        RequestBody mRequestBody = multipartBody.build();
        RequestManager.instance().uploadQrCode(mRequestBody, new MinerCallback<BaseResponseVo<UploadCodeBean>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UploadCodeBean>> call, Response<BaseResponseVo<UploadCodeBean>> response) {
                dismissLoading();
                if (response.body().isSuccessful()) {
                    if (response.body().getBody() != null) {

                    }
                    ToastUtils.showLong("上传成功");
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<UploadCodeBean>> call, Response<BaseResponseVo<UploadCodeBean>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
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
                takePhoto.onPickFromGallery();
                break;

            case R.id.img_delete_zfb:
                if (fileList.size() == 0) {
                    return;
                } else if (fileList.size() == 1) {
                    fileList.clear();
                } else {
                    fileList.remove(0);
                    //当删除支付宝收款码时，默认微信收款码为选中状态
                    mDataBinding.rlWxSelect.setVisibility(View.VISIBLE);
                    mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
                }
                mDataBinding.imgDeleteZfb.setVisibility(View.GONE);
                mDataBinding.imgZfbCode.setVisibility(View.GONE);
                mDataBinding.rlZfbSelect.setVisibility(View.GONE);
                mDataBinding.llZfbCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
                initBtnStatus();
                break;

            case R.id.img_delete_wx:
                if (fileList.size() == 0) {
                    return;
                } else if (fileList.size() == 1) {
                    fileList.clear();
                } else {
                    fileList.remove(1);
                }
                mDataBinding.imgDeleteWx.setVisibility(View.GONE);
                mDataBinding.imgWxCode.setVisibility(View.GONE);
                mDataBinding.rlWxSelect.setVisibility(View.GONE);
                mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
                initBtnStatus();
                break;

            case R.id.btn_withdraw:
                uploadCode();
                break;
        }
    }
}