package com.yunhualian.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sensetime.liveness.motion.util.ToastUtil;
import com.yunhualian.R;
import com.yunhualian.entity.ActivityModule;
import com.yunhualian.entity.Presenter;

import javax.inject.Inject;

public class TestActivity extends AppCompatActivity {
    @Inject
    Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        DaggerActivityComponent.builder()
//                .activityModule(new ActivityModule(this))
//                .build()
//                .inject(this);
    }

    public void showUserName(String name) {
        ToastUtil.show(this, name);
    }
}