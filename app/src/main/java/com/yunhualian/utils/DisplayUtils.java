package com.yunhualian.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ThreadUtils;
import com.yunhualian.R;
import com.yunhualian.ui.activity.art.ArtDetailActivity;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class DisplayUtils {
    /**
     * convert px to its equivalent dp
     * <p>
     * 将px转换为与之相等的dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * convert dp to its equivalent px
     * <p>
     * 将dp转换为与之相等的px
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private void createQrcode(String mUrl, Context context) {
        if (TextUtils.isEmpty(mUrl)) return;
        ThreadUtils.executeByCached(new ThreadUtils.SimpleTask<Bitmap>() {
            @Nullable
            @Override
            public Bitmap doInBackground() throws Throwable {

                return QRCodeEncoder.syncEncodeQRCode(mUrl, 300);
            }

            @Override
            public void onSuccess(@Nullable Bitmap result) {
                if (null != result)
                    DialogManager.showImgDialog(context, result, (dialog, which) -> {

                    });
            }

            @Override
            public void onCancel() {
                super.onCancel();
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
            }
        });
    }

    /**
     * convert px to its equivalent sp
     * <p>
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     * <p>
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
