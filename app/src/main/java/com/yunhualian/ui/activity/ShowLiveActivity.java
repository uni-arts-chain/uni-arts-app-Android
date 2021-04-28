package com.yunhualian.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;

import com.yunhualian.GLRenderer;
import com.yunhualian.JniBridgeJava;

import java.io.File;

public class ShowLiveActivity extends AppCompatActivity {
    private GLSurfaceView _glSurfaceView;
    public static final String PATH = "path";
    public static final String MODEL_NAME = "model_name";
    private GLRenderer _glRenderer;
    String path;
    String modelName;

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

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT
                        ? View.SYSTEM_UI_FLAG_LOW_PROFILE
                        : View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        JniBridgeJava.nativeOnStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        _glSurfaceView.onResume();
        String dir = Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator.concat("Yunhualian/live2d/97/");
        JniBridgeJava.loadModelFile(path, modelName);
    }

    @Override
    protected void onPause() {
        super.onPause();
        _glSurfaceView.onPause();
        JniBridgeJava.nativeOnPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        JniBridgeJava.nativeOnStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JniBridgeJava.nativeOnDestroy();
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