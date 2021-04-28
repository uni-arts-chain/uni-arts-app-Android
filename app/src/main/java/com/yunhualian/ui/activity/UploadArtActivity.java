package com.yunhualian.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.suke.widget.SwitchButton;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.base.YunApplication;
import com.yunhualian.databinding.ActivityUploadArtBinding;
import com.yunhualian.entity.ArtTypeVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.LivenessVerifyVo;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.user.MyHomePageActivity;
import com.yunhualian.utils.StringUtils;
import com.yunhualian.widget.BasePopupWindow;
import com.yunhualian.widget.UploadNormalPopUpWindow;
import com.yunhualian.widget.UploadSuccessPopUpWindow;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class UploadArtActivity extends BaseActivity<ActivityUploadArtBinding> implements View.OnClickListener, TakePhoto.TakeResultListener, InvokeListener {
    public static final String FROM = "from";
    List<String> list;
    List<ArtTypeVo> themeList;
    List<String> themeNameList = new ArrayList<>();
    List<String> cutAmoutnList = new ArrayList<>();
    UploadNormalPopUpWindow uploadNormalPopUpWindow;
    TimePickerView pvTime;
    Button takePhotoBtn;
    Button selectPhoto;
    Button cancle;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;
    File file;
    Uri imageUri;
    List<File> fileList;
    private String themeId;
    private String cutAmount;
    private PopupWindow popupWindow;
    private View contentView;
    private List<String> mLackedPermission = new ArrayList<>();
    ImageView headImag;
    boolean themeClick = false;
    boolean isCut = false;
    //作品评析输入框初始值
    private int num = 0;
    //作品评析输入框最大值
    public int mMaxNum = 200;

    //作品标题输入框初始值
    private int title_num = 0;
    //作品表提输入框最大值
    public int title_mMaxNum = 20;
    String from;
    UploadSuccessPopUpWindow uploadSuccessPopUpWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_art;
    }

    @Override
    public void initPresenter() {

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


    private String getTimes(Date date) {//年月日时分秒格式
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        return format.format(date);
    }

    @Override
    public void initView() {
        fileList = new ArrayList<>();
        from = getIntent().getStringExtra(UploadArtActivity.FROM);
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mDataBinding.uploadAction.setBackgroundColor(getColor(R.color._C5C5C5));
        mDataBinding.uploadAction.setClickable(false);
        initArtType();
        initArtTitle();
        initArtDesc();
        mToolBarOptions.titleId = R.string.upload_img;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        cutAmoutnList = Arrays.asList(getResources().getStringArray(R.array.cut_amount));
        mDataBinding.themeSelect.setOnClickListener(this);
        mDataBinding.amountSelect.setOnClickListener(this);
        mDataBinding.closed1.setOnClickListener(this);
        mDataBinding.closed2.setOnClickListener(this);
        mDataBinding.closed3.setOnClickListener(this);
        mDataBinding.performLayout1.setOnClickListener(this);
        mDataBinding.performLayout2.setOnClickListener(this);
        mDataBinding.performLayout3.setOnClickListener(this);
        mDataBinding.uploadAction.setOnClickListener(this);
        mDataBinding.switchButton.setOnCheckedChangeListener((view, isChecked) -> {
            if (isChecked) {
                isCut = true;
                mDataBinding.selectCutAmount.setText("10");
                mDataBinding.cutAmoutnLayout.setVisibility(View.VISIBLE);
                mDataBinding.artPriceLayout.setVisibility(View.GONE);
                mDataBinding.amountSelect.setVisibility(View.VISIBLE);
            } else {
                isCut = false;
                mDataBinding.cutAmoutnLayout.setVisibility(View.GONE);
                mDataBinding.artPriceLayout.setVisibility(View.VISIBLE);
                mDataBinding.amountSelect.setVisibility(View.GONE);
            }
        });
//        mDataBinding.selectCreateTime.setOnClickListener(this);
//        mDataBinding.artDetail.setOnClickListener(this);
        initPopWindow();
//        initPicker();
        uploadNormalPopUpWindow = new UploadNormalPopUpWindow(this, list, (view, position) -> {
            String str = "0";
            if (themeClick) {
                str = themeList.get(position).getTitle();
                mDataBinding.selectThemeTv.setText(str);
                themeId = String.valueOf(themeList.get(position).getId());
                buttonStateListener();
            } else {
                str = cutAmoutnList.get(position);
                mDataBinding.selectCutAmount.setText(str);
                cutAmount = str.substring(0, str.length() - 1);
            }
            uploadNormalPopUpWindow.dismiss();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.selectType:

                break;
            case R.id.selectTheme:

                break;
            case R.id.selectCreateTime:
                pvTime.show(mDataBinding.selectCreateTime);
                break;
            case R.id.art_detail:
                showPopwindow();
                break;
            case R.id.themeSelect:
                themeClick = true;
                uploadNormalPopUpWindow.setLists(themeNameList);
                uploadNormalPopUpWindow.showAtLocation(mDataBinding.content, Gravity.BOTTOM, 0, 0);

                break;
            case R.id.amountSelect:
                themeClick = false;
                uploadNormalPopUpWindow.setLists(cutAmoutnList);
                uploadNormalPopUpWindow.showAtLocation(mDataBinding.content, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.closed1:
                if (fileList.size() == 0) return;
                if (fileList.size() == 1) {
                    fileList.clear();
                } else {
                    fileList.remove(0);
                    removeAll();
                }
                updateUI();
                break;
            case R.id.closed2:
                if (fileList.size() > 1) {
                    fileList.remove(1);
                    removeAll();
                }
                updateUI();
                break;
            case R.id.closed3:
                if (fileList.size() > 2) {
                    fileList.remove(2);
                    removeAll();
                }
                updateUI();
                break;
            case R.id.performLayout1:
                showPopwindow();
                break;
            case R.id.performLayout2:
                showPopwindow();
                break;
            case R.id.performLayout3:
                showPopwindow();
                break;
            case R.id.uploadAction:
                performUpload();
                break;
        }
    }

    /*根据输入的内容改变按钮的状态*/
    private void buttonStateListener() {
        if (!TextUtils.isEmpty(mDataBinding.artTitle.getText().toString())
                && !TextUtils.isEmpty(mDataBinding.advice.getText().toString())
                && !TextUtils.isEmpty(themeId) && fileList.size() > 0) {
            mDataBinding.uploadAction.setBackgroundColor(getColor(R.color._101010));
            mDataBinding.uploadAction.setClickable(true);
        } else {
            mDataBinding.uploadAction.setBackgroundColor(getColor(R.color._C5C5C5));
            mDataBinding.uploadAction.setClickable(false);
        }
    }

    @Override
    public void takeSuccess(TResult result) {
        if (result != null) {
            File file = new File(result.getImages().get(0).getOriginalPath());
            fileList.add(file);
            buttonStateListener();//改变按钮状态
            updateUI();
            initPhoto();//初始化保存路径
        }
    }

    /*作品标题 watch*/
    private void initArtTitle() {
        mDataBinding.artTitle.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num + s.length();
                //TextView显示剩余字数
                mDataBinding.textNameNum.setText("" + number + "/" + title_mMaxNum);
                selectionStart = mDataBinding.artTitle.getSelectionStart();
                selectionEnd = mDataBinding.artTitle.getSelectionEnd();
                //判断大于最大值
                if (wordNum.length() > title_mMaxNum) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mDataBinding.artTitle.setText(s);
                    mDataBinding.artTitle.setSelection(tempSelection);//设置光标在最后
                    //吐司最多输入300字
                    ToastUtils.showLong("最多输入20字");
                }
                buttonStateListener();
            }
        });
    }

    /*作品评析textWatch*/
    private void initArtDesc() {
        mDataBinding.advice.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num + s.length();
                //TextView显示剩余字数
                mDataBinding.textNum.setText("" + number + "/" + mMaxNum);
                selectionStart = mDataBinding.advice.getSelectionStart();
                selectionEnd = mDataBinding.advice.getSelectionEnd();
                //判断大于最大值
                if (wordNum.length() > mMaxNum) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    mDataBinding.advice.setText(s);
                    mDataBinding.advice.setSelection(tempSelection);//设置光标在最后
                    //吐司最多输入300字
                    ToastUtils.showLong("最多输入200字");
                }
                buttonStateListener();
            }
        });
    }

    /*根据当前已拍摄的图片更新ui*/
    public void updateUI() {
        buttonStateListener();
        for (File file : fileList) {
            LogUtils.e("url = " + file.getAbsolutePath());
        }
        switch (fileList.size()) {
            case 0:
                removeAll();
                break;
            case 1:
                if (fileList.size() > 0)
                    performOneFile();
                break;
            case 2:
                if (fileList.size() > 1) {
                    performOneFile();
                    performTwoFile();
                }
                break;
            case 3:
                if (fileList.size() > 2) {
                    performOneFile();
                    performTwoFile();
                    performThreeFile();
                }
                break;

        }

    }

    /*取消所有图片上传状态*/
    private void removeAll() {
        mDataBinding.performLayout1.setVisibility(View.VISIBLE);
        mDataBinding.imageParent1.setVisibility(View.GONE);
        mDataBinding.img1.setVisibility(View.GONE);
        mDataBinding.fileLayout2.setVisibility(View.INVISIBLE);
        mDataBinding.fileLayout3.setVisibility(View.INVISIBLE);
        mDataBinding.imageParent1.setVisibility(View.GONE);
        mDataBinding.img1.setVisibility(View.GONE);
        mDataBinding.imageParent2.setVisibility(View.GONE);
        mDataBinding.img2.setVisibility(View.GONE);
    }

    private void performOneFile() {

        LogUtils.e("performOneFile = " + fileList.get(0).getAbsolutePath());
        mDataBinding.performLayout1.setVisibility(View.GONE);
        mDataBinding.imageParent1.setVisibility(View.VISIBLE);
        mDataBinding.img1.setVisibility(View.VISIBLE);
        Glide.with(this).load(fileList.get(0).getAbsolutePath()).into(mDataBinding.img1);
        mDataBinding.fileLayout2.setVisibility(View.VISIBLE);
        mDataBinding.performLayout2.setVisibility(View.VISIBLE);
        mDataBinding.imageParent2.setVisibility(View.GONE);
        mDataBinding.img2.setVisibility(View.GONE);
        mDataBinding.imageParent3.setVisibility(View.GONE);
        mDataBinding.img3.setVisibility(View.GONE);
    }

    private void performTwoFile() {
        LogUtils.e("performTwoFile = " + fileList.get(1).getAbsolutePath());
        mDataBinding.performLayout2.setVisibility(View.GONE);
        mDataBinding.imageParent2.setVisibility(View.VISIBLE);
        mDataBinding.img2.setVisibility(View.VISIBLE);
        Glide.with(this).load(fileList.get(1).getAbsolutePath()).into(mDataBinding.img2);
        mDataBinding.fileLayout3.setVisibility(View.VISIBLE);
        mDataBinding.performLayout3.setVisibility(View.VISIBLE);
        mDataBinding.imageParent3.setVisibility(View.GONE);
        mDataBinding.img3.setVisibility(View.GONE);
    }

    private void performThreeFile() {
        LogUtils.e("performThreeFile = " + fileList.get(2).getAbsolutePath());
        mDataBinding.performLayout3.setVisibility(View.GONE);
        mDataBinding.imageParent3.setVisibility(View.VISIBLE);
        mDataBinding.img3.setVisibility(View.VISIBLE);
        Glide.with(this).load(fileList.get(2).getAbsolutePath()).into(mDataBinding.img3);
    }


    public void initArtType() {
        themeList = YunApplication.getArtThemeVoList();
        for (ArtTypeVo artTypeVo : themeList) {
            themeNameList.add(artTypeVo.getTitle());
        }
    }

    public void showPopwindow() {
        popupWindow.showAtLocation(mDataBinding.content, Gravity.CENTER, 0, 0);
    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

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
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    public void initPopWindow() {
        contentView = LayoutInflater.from(UploadArtActivity.this).inflate(
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

    public void upload() {
        showLoading("正在上传...");
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);

        for (File file : fileList) {
            RequestBody requestFrontFile = RequestBody.create(MediaType.parse("image/*"), file);
            multipartBody.addFormDataPart("img_main_file" + (fileList.indexOf(file) + 1), StringUtils.getFileNameNoEx(file.getName()), requestFrontFile);
        }
        multipartBody.addFormDataPart("name", mDataBinding.artTitle.getText().toString());
        multipartBody.addFormDataPart("category_id", themeId);
        multipartBody.addFormDataPart("details", mDataBinding.advice.getText().toString());
        multipartBody.addFormDataPart("is_refungible", String.valueOf(isCut));
        if (isCut) {
            multipartBody.addFormDataPart("refungible_decimal", mDataBinding.selectCutAmount.getText().toString());
        }
        multipartBody.addFormDataPart("resource_type", String.valueOf(1));
        RequestBody mRequestBody = multipartBody
                .build();
        RequestManager.instance().uploadArt(mRequestBody, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess
                    (Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                if (response.body().isSuccessful()) {
                    dismissLoading();
                    uploadSuccessPopUpWindow = new UploadSuccessPopUpWindow(UploadArtActivity.this, clickListener);
                    uploadSuccessPopUpWindow.setContent(getString(R.string.upload_art_success));
                    uploadSuccessPopUpWindow.showAtLocation(mDataBinding.content, Gravity.CENTER, 0, 0);
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

    private UploadSuccessPopUpWindow.OnBottomTextviewClickListener clickListener = new UploadSuccessPopUpWindow.OnBottomTextviewClickListener() {
        @Override
        public void onCancleClick() {
            if (uploadSuccessPopUpWindow.isShowing()) uploadSuccessPopUpWindow.dismiss();
            finish();
        }

        @Override
        public void onPerformClick() {
            if (uploadSuccessPopUpWindow.isShowing()) uploadSuccessPopUpWindow.dismiss();
            if (TextUtils.isEmpty(from)) {
                startActivity(MyHomePageActivity.class);
            }
            finish();
        }
    };

    private void performUpload() {
        if (fileList.size() == 0) {
            ToastUtils.showShort("未选择图片");
            return;
        }
        if (TextUtils.isEmpty(mDataBinding.artTitle.getText())) {
            ToastUtils.showShort("作品标题不能为空");
            return;
        }
        if (TextUtils.isEmpty(themeId)) {
            ToastUtils.showShort("作品所属主题不能为空");
            return;
        }
        upload();
    }

    private void initPicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(2013, 0, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2029, 11, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                TextView btn = (TextView) v;
                btn.setText(getTimes(date));
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, false, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(16)//字号
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .isCyclic(true)
                .build();
    }
}