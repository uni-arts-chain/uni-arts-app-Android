package com.yunhualian.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import com.yunhualian.R;
import com.yunhualian.base.YunApplication;

import java.nio.ByteBuffer;

import static com.blankj.utilcode.util.ScreenUtils.getScreenHeight;
import static com.blankj.utilcode.util.ScreenUtils.getScreenWidth;

public class ScreenShotActivity extends AppCompatActivity {
    ImageView imageView;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot2);
        imageView = findViewById(R.id.image);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            startActivityForResult(
                    ((MediaProjectionManager) getSystemService("media_projection")).createScreenCaptureIntent(), 1);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        parseData(data);
    }

    private void parseData(Intent data) {
        MediaProjection mMediaProjection = ((MediaProjectionManager) getSystemService(
                Context.MEDIA_PROJECTION_SERVICE)).getMediaProjection(Activity.RESULT_OK, data);
        ImageReader mImageReader = ImageReader.newInstance(
                getScreenWidth(),
                getScreenHeight(),
                PixelFormat.RGBA_8888, 1);

        VirtualDisplay mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                getScreenWidth(),
                getScreenHeight(),
                Resources.getSystem().getDisplayMetrics().densityDpi,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Image image = mImageReader.acquireLatestImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
            }
        }, 300);

        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }
}