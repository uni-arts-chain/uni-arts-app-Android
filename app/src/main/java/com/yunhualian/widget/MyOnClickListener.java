package com.yunhualian.widget;

import android.view.View;
import android.view.View.OnClickListener;

public class MyOnClickListener implements OnClickListener {

    private long lastClick;

    @Override
    public void onClick(View v) {
        if (canClick()) {
            onMyClick(v);
        }
    }

    private boolean canClick() {

        if (System.currentTimeMillis() - lastClick <= 500) {
            return false;
        }
        lastClick = System.currentTimeMillis();
        return true;
    }

    public void onMyClick(View v) {

    }


}
