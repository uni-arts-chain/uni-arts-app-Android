package com.yunhualian.entity;

import com.yunhualian.ui.activity.ArtDetailActivity;
import com.yunhualian.ui.activity.TestActivity;

public class Presenter {
    TestActivity activity;
    User user;

    public Presenter(TestActivity activity, User user) {
        this.user = user;
        this.activity = activity;
    }

    public void showUserName() {
        activity.showUserName(user.name);
    }
}
