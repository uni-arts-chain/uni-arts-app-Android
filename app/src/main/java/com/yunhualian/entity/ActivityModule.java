package com.yunhualian.entity;

import com.yunhualian.ui.activity.ArtDetailActivity;
import com.yunhualian.ui.activity.TestActivity;

import org.bouncycastle.jcajce.provider.symmetric.TEA;
import org.bouncycastle.util.test.Test;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private TestActivity activity;

    public ActivityModule(TestActivity activity) {
        this.activity = activity;
    }

    @Provides
    public TestActivity provideActivity() {
        return activity;
    }

    @Provides
    public User provideUser() {
        return new User("User from ActivityModule");
    }

    @Provides
    public Presenter providePresenter(TestActivity activity, User user) {
        return new Presenter(activity, user);
    }
}

