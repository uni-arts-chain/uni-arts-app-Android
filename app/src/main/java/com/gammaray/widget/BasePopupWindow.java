package com.gammaray.widget;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by gengda on 2018/4/9.
 */
public class BasePopupWindow extends PopupWindow {

    protected Context mContext;
    private float mShowAlpha = 0.2f;
    private Drawable mBackgroundDrawable;

    public BasePopupWindow(Context context) {
        this.mContext = context;

        iniWindow();
    }

    public void iniWindow() {
        initPopupWindow();
    }

    @Override
    public void setOutsideTouchable(boolean touchable) {
        super.setOutsideTouchable(touchable);

        if (touchable) {
            if (mBackgroundDrawable == null)
                mBackgroundDrawable = new ColorDrawable(0x00000000);

            super.setBackgroundDrawable(mBackgroundDrawable);
        } else {
            super.setBackgroundDrawable(null);
        }
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        mBackgroundDrawable = background;

        setOutsideTouchable(isOutsideTouchable());
    }

    protected void initPopupWindow() {
        setAnimationStyle(android.R.style.Animation_Dialog);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        setFocusable(true);
    }

    @Override
    public void setContentView(View contentView) {
        if (contentView != null) {
            contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            super.setContentView(contentView);
            addKeyListener(contentView);
        }
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);

        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);

        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);

        showAnimator().start();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);

        showAnimator().start();
    }

    @Override
    public void dismiss() {
        super.dismiss();

        dismissAnimator().start();
    }

    protected void setShowAlpha(float v) {
        mShowAlpha = v;
    }

    private ValueAnimator showAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(1.0f, mShowAlpha);
        animator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();

            setWindowBackgroundAlpha(alpha);
        });

        animator.setDuration(100);

        return animator;
    }

    private ValueAnimator dismissAnimator() {
        ValueAnimator animator = ValueAnimator.ofFloat(mShowAlpha, 1.0f);
        animator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();

            setWindowBackgroundAlpha(alpha);
        });

        animator.setDuration(100);

        return animator;
    }


    private void addKeyListener(View contentView) {
        if (contentView != null) {
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);
            contentView.setOnKeyListener((view, keyCode, event) -> {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        dismiss();

                        return true;
                    default:

                        break;
                }

                return false;
            });
        }
    }


    private void setWindowBackgroundAlpha(float alpha) {
        Window window = ((Activity) getContext()).getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = alpha;
        window.setAttributes(layoutParams);
    }

}
