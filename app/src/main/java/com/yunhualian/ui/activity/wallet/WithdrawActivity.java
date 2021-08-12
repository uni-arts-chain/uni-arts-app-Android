package com.yunhualian.ui.activity.wallet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
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
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityWithdrawLayoutBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.WithDrawsBean;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RemoteService;
import com.yunhualian.net.RequestManager;
import com.yunhualian.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private int MAX_SIZE = 3 * 1024;
    private boolean bIsZFBCode;
    private RequestOptions options;
    private Map<String, File> fileMap = new HashMap<>();
    private Map<String, String> mImgUrlMap = new HashMap<>();

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
        initCodeImg();
        initPhoto();
        initBtnStatus();
    }

    @Override
    public void takeSuccess(TResult result) {
        if (result != null) {
            File file = new File(result.getImages().get(0).getCompressPath());
            if (bIsZFBCode) {
                fileMap.put("alipay_code", file);
            } else {
                fileMap.put("wechat_code", file);
            }
            updatePageInfo();
//            initPhoto();//初始化保存路径
        }
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.e("takePhoto", "takeFail");
    }

    @Override
    public void takeCancel() {
        Log.e("takePhoto", "takeCancel");
    }

    private void updatePageInfo() {
        if (fileMap != null) {
            File alipayFile = fileMap.get("alipay_code");
            if (alipayFile != null) {
                showZfbCode(alipayFile.getAbsolutePath());
            } else {
                if (!TextUtils.isEmpty(mImgUrlMap.get("alipay_url"))) {
                    mDataBinding.llZfbCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
                    mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
                    return;
                }
                mDataBinding.imgDeleteZfb.setVisibility(View.GONE);
                mDataBinding.imgZfbCode.setVisibility(View.GONE);
                mDataBinding.rlZfbSelect.setVisibility(View.GONE);
                mDataBinding.llZfbCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
            }

            File wechatFile = fileMap.get("wechat_code");
            if (wechatFile != null) {
                showWxCode(wechatFile.getAbsolutePath());
            } else {
                if (!TextUtils.isEmpty(mImgUrlMap.get("wechat_url"))) {
                    if (alipayFile != null || !TextUtils.isEmpty(mImgUrlMap.get("alipay_url"))) {
                        mDataBinding.llZfbCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
                        mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
                    }
                    return;
                }
                mDataBinding.imgDeleteWx.setVisibility(View.GONE);
                mDataBinding.imgWxCode.setVisibility(View.GONE);
                mDataBinding.rlWxSelect.setVisibility(View.GONE);
                mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
            }

            if (alipayFile != null) {
                mDataBinding.llZfbCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
                mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
            } else if (wechatFile != null) {
                mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
            }
        }
        initBtnStatus();
    }

    private void showZfbCode(String path) {
        mDataBinding.imgZfbCode.setVisibility(View.VISIBLE);
        mDataBinding.imgDeleteZfb.setVisibility(View.VISIBLE);
        mDataBinding.rlZfbSelect.setVisibility(View.VISIBLE);
        Glide.with(this).load(path).apply(options).into(mDataBinding.imgZfbCode);
    }

    private void showWxCode(String path) {
        mDataBinding.imgWxCode.setVisibility(View.VISIBLE);
        mDataBinding.imgDeleteWx.setVisibility(View.VISIBLE);
        if (fileMap.get("alipay_code") != null) {
            mDataBinding.rlWxSelect.setVisibility(View.GONE);
        } else {
            mDataBinding.rlWxSelect.setVisibility(View.VISIBLE);
        }
        Glide.with(this).load(path).apply(options).into(mDataBinding.imgWxCode);
    }


    private void initBtnStatus() {
        if (mImgUrlMap != null && mImgUrlMap.size() > 0) {
            mDataBinding.btnWithdraw.setVisibility(View.VISIBLE);
            mDataBinding.tvHint.setVisibility(View.VISIBLE);
        } else {
            if (fileMap != null && fileMap.size() > 0) {
                mDataBinding.btnWithdraw.setVisibility(View.VISIBLE);
                mDataBinding.tvHint.setVisibility(View.VISIBLE);
            } else {
                mDataBinding.btnWithdraw.setVisibility(View.GONE);
                mDataBinding.tvHint.setVisibility(View.GONE);
            }
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

    private void initCodeImg() {
        mImgUrlMap.clear();
        if (YunApplication.getmUserVo() != null) {
            if (!TextUtils.isEmpty(YunApplication.getmUserVo().getAlipay_img().getUrl())) {
                mImgUrlMap.put("alipay_url", YunApplication.getmUserVo().getAlipay_img().getUrl());
                mDataBinding.imgZfbCode.setVisibility(View.VISIBLE);
                mDataBinding.imgDeleteZfb.setVisibility(View.VISIBLE);
                mDataBinding.rlZfbSelect.setVisibility(View.VISIBLE);
                mDataBinding.llZfbCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
                Glide.with(this).load(YunApplication.getmUserVo().getAlipay_img().getUrl()).apply(options).into(mDataBinding.imgZfbCode);
            } else {
                mDataBinding.imgDeleteZfb.setVisibility(View.GONE);
                mDataBinding.imgZfbCode.setVisibility(View.GONE);
                mDataBinding.rlZfbSelect.setVisibility(View.GONE);
                mDataBinding.llZfbCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
            }

            if (!TextUtils.isEmpty(YunApplication.getmUserVo().getWeixin_img().getUrl())) {
                mImgUrlMap.put("wechat_url", YunApplication.getmUserVo().getAlipay_img().getUrl());
                mDataBinding.imgWxCode.setVisibility(View.VISIBLE);
                mDataBinding.imgDeleteWx.setVisibility(View.VISIBLE);
                if (TextUtils.isEmpty(YunApplication.getmUserVo().getAlipay_img().getUrl())) {
                    mDataBinding.rlWxSelect.setVisibility(View.VISIBLE);
                    mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
                }
                Glide.with(this).load(YunApplication.getmUserVo().getWeixin_img().getUrl()).apply(options).into(mDataBinding.imgWxCode);
            } else {
                mDataBinding.imgDeleteWx.setVisibility(View.GONE);
                mDataBinding.imgWxCode.setVisibility(View.GONE);
                mDataBinding.rlWxSelect.setVisibility(View.GONE);
                mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
            }
        }
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    private void uploadCode(String type) {
        showLoading("正在上传...");
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);

        if (fileMap == null || fileMap.size() == 0) {
            return;
        }
        if (type.equals("alipay")) {
            File alipayFile = fileMap.get("alipay_code");
            if (alipayFile == null) {
                return;
            }
            RequestBody alipayFrontFile = RequestBody.create(MediaType.parse("image/*"), alipayFile);
            multipartBody.addFormDataPart("alipay_img", alipayFile.getName(), alipayFrontFile);
        } else {
            File wechatFile = fileMap.get("wechat_code");
            if (wechatFile == null) {
                return;
            }
            RequestBody wechatFrontFile = RequestBody.create(MediaType.parse("image/*"), wechatFile);
            multipartBody.addFormDataPart("weixin_img", wechatFile.getName(), wechatFrontFile);
        }

        RequestBody mRequestBody = multipartBody.build();
        RequestManager.instance().uploadQrCode(mRequestBody, new MinerCallback<BaseResponseVo<UploadCodeBean>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UploadCodeBean>> call, Response<BaseResponseVo<UploadCodeBean>> response) {
                dismissLoading();
                if (response.body().isSuccessful()) {
                    if (response.body().getBody() != null) {
                        UploadCodeBean data = response.body().getBody();
                        if (data != null) {
                            if (data.getPay_type().equals("1")) {
                                YunApplication.getmUserVo().getAlipay_img().setUrl(data.getImg().getUrl());
                            }
                            if (data.getPay_type().equals("2")) {
                                YunApplication.getmUserVo().getWeixin_img().setUrl(data.getImg().getUrl());
                            }
                        }
                    }
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

    private void updateCode(String type) {
        showLoading("正在上传...");
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);

        if (fileMap == null || fileMap.size() == 0) {
            return;
        }
        if (type.equals("alipay")) {
            File alipayFile = fileMap.get("alipay_code");
            if (alipayFile == null) {
                return;
            }
            RequestBody alipayFrontFile = RequestBody.create(MediaType.parse("image/*"), alipayFile);
            multipartBody.addFormDataPart("alipay_img", alipayFile.getName(), alipayFrontFile);
        } else {
            File wechatFile = fileMap.get("wechat_code");
            if (wechatFile == null) {
                return;
            }
            RequestBody wechatFrontFile = RequestBody.create(MediaType.parse("image/*"), wechatFile);
            multipartBody.addFormDataPart("weixin_img", wechatFile.getName(), wechatFrontFile);
        }
        RequestBody mRequestBody = multipartBody.build();
        RequestManager.instance().updateQrCode(mRequestBody, new MinerCallback<BaseResponseVo<UploadCodeBean>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UploadCodeBean>> call, Response<BaseResponseVo<UploadCodeBean>> response) {
                dismissLoading();
                if (response.body().isSuccessful()) {
                    if (response.body().getBody() != null) {
                        UploadCodeBean data = response.body().getBody();
                        if (data != null) {
                            if (data.getPay_type().equals("1")) {
                                YunApplication.getmUserVo().getAlipay_img().setUrl(data.getImg().getUrl());
                            }
                            if (data.getPay_type().equals("2")) {
                                YunApplication.getmUserVo().getWeixin_img().setUrl(data.getImg().getUrl());
                            }
                        }
                    }
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

    private void withDraws(String type){
        showLoading(getString(R.string.progress_loading));
        HashMap<String,String> params = new HashMap<>();
        params.put("amount","10");
        params.put("pay_type",type);
        RequestManager.instance().withdraws(params, new MinerCallback<BaseResponseVo<WithDrawsBean>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<WithDrawsBean>> call, Response<BaseResponseVo<WithDrawsBean>> response) {
                dismissLoading();
                if(response.isSuccessful()){
                    ToastUtils.showShort("提现申请已提交");
                    finish();
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<WithDrawsBean>> call, Response<BaseResponseVo<WithDrawsBean>> response) {
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
                if (!TextUtils.isEmpty(mImgUrlMap.get("alipay_url"))) {
                    mImgUrlMap.remove("alipay_url");
                    mDataBinding.imgDeleteZfb.setVisibility(View.GONE);
                    mDataBinding.imgZfbCode.setVisibility(View.GONE);
                    mDataBinding.rlZfbSelect.setVisibility(View.GONE);
                    mDataBinding.llZfbCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
                    if (!TextUtils.isEmpty(mImgUrlMap.get("wechat_url"))) {
                        mDataBinding.imgWxCode.setVisibility(View.VISIBLE);
                        mDataBinding.imgDeleteWx.setVisibility(View.VISIBLE);
                        mDataBinding.rlWxSelect.setVisibility(View.VISIBLE);
                        mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.bg_yellow_selected));
                    }
                    initBtnStatus();
                } else {
                    if (fileMap.get("alipay_code") != null) {
                        fileMap.remove("alipay_code");
                    }
                    updatePageInfo();
                }
                break;

            case R.id.img_delete_wx:
                if (!TextUtils.isEmpty(mImgUrlMap.get("wechat_url"))) {
                    mImgUrlMap.remove("wechat_url");
                    mDataBinding.imgDeleteWx.setVisibility(View.GONE);
                    mDataBinding.imgWxCode.setVisibility(View.GONE);
                    mDataBinding.rlWxSelect.setVisibility(View.GONE);
                    mDataBinding.llWxCodeLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.shape_add_bg));
                    initBtnStatus();
                } else {
                    if (fileMap.get("wechat_code") != null) {
                        fileMap.remove("wechat_code");
                    }
                    updatePageInfo();
                }
                break;

            case R.id.btn_withdraw:
                if(mImgUrlMap.size() > 0){ //当前用户已上传支付图片
                    if(!TextUtils.isEmpty(mImgUrlMap.get("alipay_url")) && !TextUtils.isEmpty(mImgUrlMap.get("wechat_url")) ){
                        //当前用户已上传支付宝二维码以及微信二维码，则直接提现
                        withDraws("alipay");
                    }else if(!TextUtils.isEmpty("alipay_url")){
                        //当前用户已上传支付宝二维码,但是无微信二维码，则上传微信二维码后提现
                        updateCode("wechat");
                    }else if(!TextUtils.isEmpty("wechat_url")){
                        //当前用户已上传微信二维码,但是无支付宝二维码，则上传支付宝二维码后提现
                        updateCode("alipay");
                    }
                }else{
                    //当前用户没有上传任何支付图片或想重新上传图片
                    if (YunApplication.getmUserVo() != null) {
                        if (!TextUtils.isEmpty(YunApplication.getmUserVo().getAlipay_img().getUrl())) {
                            updateCode("alipay");
                        } else {
                            uploadCode("alipay");
                        }
                        if (!TextUtils.isEmpty(YunApplication.getmUserVo().getWeixin_img().getUrl())) {
                            updateCode("wechat");
                        } else {
                            uploadCode("wechat");
                        }
                    }
                }
                break;
        }
    }
}