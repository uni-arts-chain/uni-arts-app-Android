package com.yunhualian.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.blankj.utilcode.constant.PermissionConstants;
import com.blankj.utilcode.util.PermissionUtils;
import com.yunhualian.R;


public class PermissionDialog extends Dialog {
    private static final String PACKAGE_URL_SCHEME = "package:"; // 方案
    private Context context;
    TextView dialog_msg;
    String hintmsg;
    Activity activity;

    public PermissionDialog(final Context context, String... permissions) {
        super(context, R.style.dialog);
        this.setContentView(R.layout.dialog_permission);
        this.context = context;
        activity = (Activity) context;
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        LayoutInflater inflaterDl = LayoutInflater.from(context);
        View layout = inflaterDl.inflate(R.layout.dialog_permission, null);
        TextView conner_sure = (TextView) layout.findViewById(R.id.conner_sure);
        TextView conner_cancel = (TextView) layout.findViewById(R.id.conner_cancel);
        dialog_msg = (TextView) layout.findViewById(R.id.dialog_msg);
        initpermissionhint(permissions);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) (height / 4);
        params.width = (int) (width * 0.7);
        getWindow().setAttributes(params);
        getWindow().setContentView(layout);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        conner_sure.setOnClickListener(onClick);
        conner_cancel.setOnClickListener(onClick);
    }

    private void initpermissionhint(String... permissions) {
        StringBuilder sb = new StringBuilder();
        for (String per : permissions) {
            if (!PermissionUtils.isGranted(PermissionConstants.LOCATION)) {//定位
                sb.append(",定位");
            } else if (!PermissionUtils.isGranted(PermissionConstants.STORAGE)) {//读写文件存储
                sb.append(",读写文件存储");
            } else if (!PermissionUtils.isGranted(PermissionConstants.CAMERA)) {//相机
                sb.append(",相机");
            } else if (!PermissionUtils.isGranted(PermissionConstants.PHONE)) {
                sb.append(",电话");
            } else {
                sb.append("");
            }
        }
        hintmsg = String.format(context.getResources().getString(R.string.permissions_alert), sb.toString());
        dialog_msg.setText(hintmsg);
    }

    MyOnClickListener onClick = new MyOnClickListener() {

        @Override
        public void onMyClick(View v) {
            int id = v.getId();
            if (id == R.id.conner_sure) {
                dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse(PACKAGE_URL_SCHEME + context.getPackageName()));
                activity.startActivityForResult(intent, 112);
            } else if (id == R.id.conner_cancel) {
                System.exit(0);
            }
        }

    };

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
