package com.yunhualian.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yunhualian.R;
import com.yunhualian.base.YunApplication;

import java.lang.reflect.Field;

/**
 * Synopsis     ${SYNOPSIS}
 * Author		Mosr
 * Version		${VERSION}
 * Create 	    2018.12.22 13:36
 * Email  		intimatestranger@sina.cn
 */
public class DialogManager {
    public static void showListDialog(Context mContext, String[] items, DialogInterface.OnClickListener mOnClickListener) {
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(mContext);
        listDialog.setTitle("选择");
        listDialog.setItems(items, mOnClickListener);
        listDialog.show();
    }

    public static void showConfirmDialog(Context mContext, String mTitle, String mTMessage, String mCancelTxt, String mConfirmTxt, DialogInterface.OnClickListener mOnClickListener) {
        showConfirmDialog(mContext, mTitle, mTMessage, mCancelTxt, mConfirmTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }, mOnClickListener);
    }

    public static void showConfirmDialog(Context mContext, String mTitle, String mTMessage, String mCancelTxt, String mConfirmTxt, DialogInterface.OnClickListener mNeOnClickListener, DialogInterface.OnClickListener mOnClickListener) {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(mTitle)
                .setMessage(mTMessage)
                .setNegativeButton(TextUtils.isEmpty(mCancelTxt) ? "取消" : mCancelTxt, mNeOnClickListener)
                .setPositiveButton(TextUtils.isEmpty(mConfirmTxt) ? "确定" : mConfirmTxt, mOnClickListener)
                .create();
        if (null != dialog) {
            dialog.setCanceledOnTouchOutside(false);
            if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
                dialog.show();
                setWinWidth(dialog);
            }
        }
        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(dialog);
            Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
            mMessage.setAccessible(true);
            TextView mMessageView = (TextView) mMessage.get(mAlertController);
            mMessageView.setTextColor(0xffB0B0B0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        if (null != dialog) {
            Button BUTTON_POSITIVE = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (null != BUTTON_POSITIVE)
                BUTTON_POSITIVE.setTextColor(YunApplication.getInstance().getResources().getColor(R.color.colorAccent));

            Button BUTTON_NEGATIVE = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (null != BUTTON_NEGATIVE)
                BUTTON_NEGATIVE.setTextColor(YunApplication.getInstance().getResources().getColor(R.color._999999));
        }
    }

    public static void showConfirmDialogWithView(Context mContext, String mTitle, View view, String mCancelTxt, String mConfirmTxt, DialogInterface.OnClickListener mOnClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setView(view)
                .setNegativeButton(TextUtils.isEmpty(mCancelTxt) ? "取消" : mCancelTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(TextUtils.isEmpty(mConfirmTxt) ? "确定" : mConfirmTxt, mOnClickListener);
        if (!TextUtils.isEmpty(mTitle)) {
            builder.setTitle(mTitle);
        }
        AlertDialog dialog = builder.create();
        if (null != dialog) {
            dialog.setCanceledOnTouchOutside(false);
            if (null != mContext && mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
                dialog.show();
                setWinWidth(dialog);
            }
        }
        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(dialog);
            Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
            mMessage.setAccessible(true);
            TextView mMessageView = (TextView) mMessage.get(mAlertController);
            mMessageView.setTextColor(0xffB0B0B0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (null != dialog) {
            Button BUTTON_POSITIVE = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
            if (null != BUTTON_POSITIVE)
                BUTTON_POSITIVE.setTextColor(YunApplication.getInstance().getResources().getColor(R.color.colorAccent));

            Button BUTTON_NEGATIVE = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (null != BUTTON_NEGATIVE)
                BUTTON_NEGATIVE.setTextColor(YunApplication.getInstance().getResources().getColor(R.color._999999));
        }
    }

    public static void showConfirmDialog(Context mContext, String mTitle, String mTMessage, String mCancelTxt, DialogInterface.OnClickListener mOnClickListener) {
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(mTitle)
                .setMessage(mTMessage)
                .setNegativeButton(TextUtils.isEmpty(mCancelTxt) ? "取消" : mCancelTxt, mOnClickListener)
                .create();
        if (null != dialog) {
            dialog.setCanceledOnTouchOutside(false);
            if (null != mContext && mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
                dialog.show();
                setWinWidth(dialog);
            }
        }
        try {
            Field mAlert = AlertDialog.class.getDeclaredField("mAlert");
            mAlert.setAccessible(true);
            Object mAlertController = mAlert.get(dialog);
            Field mMessage = mAlertController.getClass().getDeclaredField("mMessageView");
            mMessage.setAccessible(true);
            TextView mMessageView = (TextView) mMessage.get(mAlertController);
            mMessageView.setTextColor(0xffB0B0B0);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        if (null != dialog) {
            Button BUTTON_NEGATIVE = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (null != BUTTON_NEGATIVE)
                BUTTON_NEGATIVE.setTextColor(YunApplication.getInstance().getResources().getColor(R.color._999999));
        }
    }

    private static void setWinWidth(Dialog mDialog) {
        if (null == mDialog) return;
        DisplayMetrics metric = new DisplayMetrics();
        if (null == mDialog.getWindow()) return;
        if (null == mDialog.getWindow().getWindowManager()) return;
        mDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(metric);
        WindowManager.LayoutParams p = mDialog.getWindow().getAttributes();
        p.width = (int) (metric.widthPixels * 0.95);
        mDialog.getWindow().setAttributes(p);
    }

    public static void showImgDialog(Context mContext, Bitmap img, DialogInterface.OnClickListener mOnClickListener) {
        AlertDialog.Builder imgDialog =
                new AlertDialog.Builder(mContext);
//        listDialog.setTitle("选择");
//        listDialog.setItems(items, mOnClickListener);
        RelativeLayout relativeLayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.alert_img, null);
        ImageView imageView = relativeLayout.findViewById(R.id.code_img);
        imageView.setImageBitmap(img);
        imgDialog.setView(R.layout.alert_img);
        imgDialog.show();
    }
}
