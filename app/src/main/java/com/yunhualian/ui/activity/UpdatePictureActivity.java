package com.yunhualian.ui.activity;


import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.yunhualian.R;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.ui.activity.user.UserCenterActivity;

import java.io.File;

public class UpdatePictureActivity extends BaseActivity implements TakePhoto.TakeResultListener, InvokeListener {
    Button takeAction;
    CityPickerView pickerView;
    TakePhoto takePhoto;
    File file;
    Uri imageUri;
    private InvokeParam invokeParam;

    @Override
    public int getLayoutId() {
        return R.layout.activity_update_picture;
    }

    @Override
    public void initPresenter() {

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
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {

        takeAction = findViewById(R.id.takeAction);
        pickerView = new CityPickerView();
        pickerView.init(this);
        takeAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showAddPick();
//                goTakePhoto();
                startActivity(UserCenterActivity.class);
            }
        });
        initPhoto();
    }

    public void showAddPick() {
        CityConfig cityConfig = new CityConfig.Builder()
                .province("北京市")
                .provinceCyclic(false)
                .build();
    /*CityConfig cityConfig = new CityConfig.Builder()
                .title("选择城市")//标题
                .titleTextSize(18)//标题文字大小
                .titleTextColor("#585858")//标题文字颜  色
                .titleBackgroundColor("#E9E9E9")//标题栏背景色
                .confirTextColor("#585858")//确认按钮文字颜色
                .confirmText("ok")//确认按钮文字
                .confirmTextSize(16)//确认按钮文字大小
                .cancelTextColor("#585858")//取消按钮文字颜色
                .cancelText("cancel")//取消按钮文字
                .cancelTextSize(16)//取消按钮文字大小
                .setCityWheelType(CityConfig.WheelType.PRO_CITY_DIS)//显示类，只显示省份一级，显示省市两级还是显示省市区三级
                .showBackground(true)//是否显示半透明背景
                .visibleItemsCount(7)//显示item的数量
                .province("浙江省")//默认显示的省份
                .city("杭州市")//默认显示省份下面的城市
                .district("滨江区")//默认显示省市下面的区县数据
                .provinceCyclic(true)//省份滚轮是否可以循环滚动
                .cityCyclic(true)//城市滚轮是否可以循环滚动
                .districtCyclic(true)//区县滚轮是否循环滚动
                .setCustomItemLayout(R.layout.item_city)//自定义item的布局
                .setCustomItemTextViewId(R.id.item_city_name_tv)//自定义item布局里面的textViewid
                .drawShadows(false)//滚轮不显示模糊效果
                .setLineColor("#03a9f4")//中间横线的颜色
                .setLineHeigh(5)//中间横线的高度
                .setShowGAT(true)//是否显示港澳台数据，默认不显示
                .build();
*/
        pickerView.setConfig(cityConfig);
        pickerView.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                super.onSelected(province, city, district);

            }
        });
        pickerView.showCityPicker();
    }

    public void goTakePhoto() {
//        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
//        builder.setCorrectImage(true);
//        takePhoto.setTakePhotoOptions(builder.create());
//        CropOptions cropOptions = new CropOptions.Builder().setWithOwnCrop(false).create();
        takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
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
    public void takeSuccess(TResult result) {

    }

    @Override
    public void takeFail(TResult result, String msg) {

    }

    @Override
    public void takeCancel() {

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
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //以下代码为处理Android6.0、7.0动态权限所需
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this);

    }
}