package com.gammaray.ui.activity.art;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.androidyuan.lib.screenshot.Shooter;
import com.blankj.utilcode.util.BarUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;
import com.gammaray.GLRenderer;
import com.gammaray.JniBridgeJava;
import com.gammaray.base.YunApplication;
import com.gammaray.utils.FileHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;


public class ShowLiveActivity extends Activity {
    private GLSurfaceView _glSurfaceView;
    public static final String PATH = "path";
    public static final String MODEL_NAME = "model_name";
    private GLRenderer _glRenderer;
    String path;
    String modelName;
    Bitmap bitmap;
    boolean isFromDetail;
    boolean hasScreenShot = false;
    public final int shapeThreshold = 200;
    public final int startYpx = 120;
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
        EventBus.getDefault().register(this);
        path = getIntent().getStringExtra(PATH);
        modelName = getIntent().getStringExtra(MODEL_NAME);
        isFromDetail = getIntent().getBooleanExtra("is_from_detail", false);
        JniBridgeJava.SetActivityInstance(this);
        JniBridgeJava.SetContext(this);
        _glSurfaceView = new GLSurfaceView(this);
        _glSurfaceView.setEGLContextClientVersion(2);
        _glRenderer = new GLRenderer();
        _glSurfaceView.setRenderer(_glRenderer);
        _glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        BarUtils.setNotificationBarVisibility(false);
        BarUtils.setNavBarVisibility(this, false);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(com.upbest.arouter.EventBusMessageEvent eventBusMessageEvent) {
        if (eventBusMessageEvent != null) {
            if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_LIVE_CLOSE)) {
                //refresh token
                if(isFromDetail){
                    finish();
                }else{
                    if (!hasScreenShot)
                        handler.sendEmptyMessage(0);
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.e("onResume");
        _glSurfaceView.onResume();
        JniBridgeJava.loadModelFile(path, modelName);

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
        EventBus.getDefault().unregister(this);
        JniBridgeJava.nativeOnDestroy();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private Intent createScreenCaptureIntent() {
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
                            int[] size = ImageUtils.getSize(path);
                            int height = FileHelper.hasNavBar(ShowLiveActivity.this) ? size[BigDecimal.ONE.intValue()] - shapeThreshold : size[BigDecimal.ONE.intValue()];
                            Bitmap bitmap = ImageUtils.clip(ImageUtils.getBitmap(path), BigDecimal.ZERO.intValue(), startYpx, size[BigDecimal.ZERO.intValue()], height);
                            String newPath = YunApplication.LIVE2D_CACHE_PATH.concat(String.valueOf(System.currentTimeMillis())).concat(".png");
                            boolean saveSuc = ImageUtils.save(bitmap, newPath, Bitmap.CompressFormat.PNG, false);
                            if (saveSuc) {
                                FileUtils.delete(path);
                                EventBus.getDefault().post(new EventBusMessageEvent(EventEntity.EVENT_SCREEN_SHORT, newPath));
                            } else ToastUtils.showShort("save fail");
//                            YunApplication.path = path;
                            if (!TextUtils.isEmpty(path))
                                finish();
                        }

                        @Override
                        public void onError() {
                            ToastUtils.showShort("You got wrong.");
                        }
                    }
            );
        }
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;
            decorView.setSystemUiVisibility(uiOptions);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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