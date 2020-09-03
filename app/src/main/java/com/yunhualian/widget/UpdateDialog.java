package com.yunhualian.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.yunhualian.R;
import com.yunhualian.constant.AppConstant;


public class UpdateDialog extends Dialog {

    private Context mContext;

    private String mVersionName;
    private String mDesc;
    private String mUpdateText;
    private boolean bUpdateFlag;
    private View.OnClickListener mListener;

    private Button btn_aum_update;

    public UpdateDialog(@NonNull Context context, String versionName, String desc, String updateText, boolean updateFlag, View.OnClickListener listener) {
        super(context, R.style.dialog);

        mContext = context;
        mVersionName = versionName;
        mDesc = desc;
        mUpdateText = updateText;
        bUpdateFlag = updateFlag;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init() {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.dialog_update_message, null);
        setContentView(R.layout.dialog_update_message);
        setCancelable(false);

        TextView txt_aum_update_version = findViewById(R.id.txt_aum_update_version);
        TextView txt_aum_update_content = findViewById(R.id.txt_aum_update_content);
        ImageButton imb_aum_close = findViewById(R.id.imb_aum_close);
//        Button btn_browser = findViewById(R.id.btn_browser);
        btn_aum_update = findViewById(R.id.btn_aum_update);

        txt_aum_update_version.setText(getVersionName());
        txt_aum_update_content.setText(getDesc());

        imb_aum_close.setVisibility(bUpdateFlag ? View.GONE : View.VISIBLE);
        imb_aum_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppConstant.DownLoadValues.IsDownLoadDialogShow = false;

                dismiss();

            }
        });

/*        btn_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
*//*
                try {
                    Uri uri = Uri.parse(AppConstant.DownLoadValues.DownLoadBrowserUrl);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    e.getMessage();
                }
*//*

                dismiss();

            }
        });*/

        setUpdateButton(mUpdateText, mListener);
    }

    private String getVersionName() {
        String value;

        if (TextUtils.isEmpty(mVersionName))
            value = "";
        else
            value = mVersionName/*StringUtils.formatUpperCase(mVersionName.replace("Android", "").replace(" ", ""))*/;

        return value;
    }

    private String getDesc() {
        String value;

        if (TextUtils.isEmpty(mDesc))
            value = "";
        else
            value = mDesc/*StringUtils.getArrayValue(mDesc.replace("，", "/").replace(",", "/")
                    .replace(" ", "").split("/"))*/;

        return value;
    }

    public void setUpdateButton(String updateText, View.OnClickListener listener) {
        if (null == btn_aum_update)
            return;

        btn_aum_update.setText(updateText);
        btn_aum_update.setOnClickListener(listener);
    }

}
