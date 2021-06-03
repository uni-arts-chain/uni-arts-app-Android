package com.yunhualian.ui.activity.art;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

import com.androidyuan.lib.screenshot.Shooter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;
import com.yunhualian.GLRenderer;
import com.yunhualian.JniBridgeJava;
import com.yunhualian.base.YunApplication;

import org.greenrobot.eventbus.EventBus;



public class ShowLiveActivity extends Activity {
    private GLSurfaceView _glSurfaceView;
    public static final String PATH = "path";
    public static final String MODEL_NAME = "model_name";
    private GLRenderer _glRenderer;
    String path;
    String modelName;
    Bitmap bitmap;
    boolean hasScreenShot = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hasScreenShot = true;
            startActivityForResult(createScreenCaptureIntent(), 1);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        path = getIntent().getStringExtra(PATH);
        modelName = getIntent().getStringExtra(MODEL_NAME);
        JniBridgeJava.SetActivityInstance(this);
        JniBridgeJava.SetContext(this);
        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(2);
        _glRenderer = new GLRenderer();
        _glSurfaceView.setRenderer(_glRenderer);
        _glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(_glSurfaceView);

//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN
//                        | (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT
//                        ? View.SYSTEM_UI_FLAG_LOW_PROFILE
//                        : View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
//        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtils.e("onStart");
        JniBridgeJava.nativeOnStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("onResume");
        _glSurfaceView.onResume();
        JniBridgeJava.loadModelFile(path, modelName);
        if (!hasScreenShot)
            handler.sendEmptyMessageDelayed(0, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("onPause");
        if (!hasScreenShot) {
            _glSurfaceView.onPause();
            JniBridgeJava.nativeOnPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.e("onStop");
        JniBridgeJava.nativeOnStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JniBridgeJava.nativeOnDestroy();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Intent createScreenCaptureIntent() {
        //Here using media_projection instead of Context.MEDIA_PROJECTION_SERVICE to  make it successfully build on low api.
        return ((MediaProjectionManager) getSystemService("media_projection")).createScreenCaptureIntent();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1 && data != null) {
            Shooter shooter = new Shooter(ShowLiveActivity.this, resultCode, data);
            String dir = YunApplication.LIVE2D_CACHE_PATH.concat(String.valueOf(System.currentTimeMillis())).concat(".png");
            shooter.startScreenShot(dir, new Shooter.OnShotListener() {
                        @Override
                        public void onFinish(String path) {
                            hasScreenShot = true;
                            EventBus.getDefault().post(new EventBusMessageEvent(EventEntity.EVENT_SCREEN_SHORT, path));
//                            YunApplication.path = path;
                        }

                        @Override
                        public void onError() {
                            ToastUtils.showShort("You got wrong.");
                        }
                    }
            );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                JniBridgeJava.nativeOnTouchesBegan(pointX, pointY);
                break;
            case MotionEvent.ACTION_UP:
                JniBridgeJava.nativeOnTouchesEnded(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                JniBridgeJava.nativeOnTouchesMoved(pointX, pointY);
                break;
        }
        return super.onTouchEvent(event);
    }
}