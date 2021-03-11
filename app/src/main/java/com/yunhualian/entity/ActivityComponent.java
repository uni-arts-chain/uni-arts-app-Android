package com.yunhualian.entity;

import com.yunhualian.ui.activity.TestActivity;

import dagger.Component;

@Component(modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(TestActivity daggerActivity);
}
