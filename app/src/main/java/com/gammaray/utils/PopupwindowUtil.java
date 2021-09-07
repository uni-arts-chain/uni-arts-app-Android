package com.gammaray.utils;

/**
 * Synopsis     com.miner.client.utils.PopupwindowUtil
 * Author		Mosr
 * Version		1.0.0
 * Create 	    2020/7/4 14:11
 * Email  		intimatestranger@sina.cn
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Synopsis     ${SYNOPSIS}
 * Author		Mosr
 * Version		${VERSION}
 * Create 	    2017/11/7 16:06
 * Modify 	    2018/10/18 17:32
 * Email  		intimatestranger@sina.cn
 */
public class PopupwindowUtil extends PopupWindow {
    private static PopupwindowUtil mPopupwindowUtil;
    private Context mContext;
    private boolean Overlayerable;
    private View mOverView;
    private WindowManager mWindowManager;


    public static PopupwindowUtil getInstance() {
        if (null == mPopupwindowUtil) {
            synchronized (PopupwindowUtil.class) {
                if (null == mPopupwindowUtil) {
                    mPopupwindowUtil = new PopupwindowUtil();
                }
            }
        }
        //非正常情况处理
        mDismiss();
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mBinding(@NonNull Context mContext) {
        if (null == mContext) {
            throw new IllegalArgumentException("Context can't be null!");
        }
        mBinding(mContext, true);
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mBinding(Context mContext, boolean Overlayerable) {
        this.mContext = mContext;
        this.Overlayerable = Overlayerable;
        return mPopupwindowUtil;
    }

    /**
     * 必须设置，否则pop无法填充满整个屏幕
     *
     * @param background
     * @return
     */
    public PopupwindowUtil mBackground(Drawable background) {
        setBackgroundDrawable(background);
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mAnimationStyle(int animationStyle) {
        setAnimationStyle(animationStyle);
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mLoadView(int layoutID) {
        if (0 == layoutID)
            throw new IllegalArgumentException("This layoutID is invalid!");
        View contentView = null;
        if (null != mContext)
            contentView = LayoutInflater.from(mContext).inflate(layoutID, null);
        if (null == contentView)
            throw new RuntimeException("This layout can't be load!");
        setContentView(contentView);
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mLoadView(int layoutID, String title) {
        if (0 == layoutID)
            throw new IllegalArgumentException("This layoutID is invalid!");
        View contentView = null;
        if (null != mContext)
            contentView = LayoutInflater.from(mContext).inflate(layoutID, null);
        if (null == contentView)
            throw new RuntimeException("This layout can't be load!");
        /*TextView red_title = (TextView) contentView.findViewById(R.id.red_title);
        if (null != red_title && !TextUtils.isEmpty(title))
            red_title.setText(title);*/
        setContentView(contentView);
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mWidthHeight(int width, int height) {
        setWidth(width);
        setHeight(height);
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mFocusable(boolean able) {
        setFocusable(able);
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mTouchable(boolean able) {
        setTouchable(able);
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mOutsideTouchable(final boolean able) {
        setOutsideTouchable(able);
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!isOutsideTouchable()) {
                    View mView = getContentView();
                    if (null != mView)
                        mView.dispatchTouchEvent(event);
                }
                return isFocusable() && !isOutsideTouchable();
            }
        });
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mSoftInputMode(int mode) {
        setSoftInputMode(mode);
        return mPopupwindowUtil;
    }

    public <T extends View> PopupwindowUtil mViewAction(int mViewID, ViewAction<T> mViewAction) {
        View mContentView = getContentView();
        if (null != mContentView) {
            View mView = mContentView.findViewById(mViewID);
            if (null != mView)
                mViewAction.action(mPopupwindowUtil, (T) mView);
        }
        return mPopupwindowUtil;
    }

    public <T extends View> PopupwindowUtil mViewActions(ViewActions<T> mViewActions, Integer... mViewIDs) {
        View mContentView = getContentView();
        if (null != mContentView) {
            for (int mViewID : mViewIDs) {
                View mView = mContentView.findViewById(mViewID);
                mViewActions.action(null != mView ? (T) mView : null, mViewID);
            }
        }
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mCheckAction(int mViewID, final PopupAction mPopupAction) {
        final View mContentView = getContentView();
        if (null != mContentView) {
            View mView = mContentView.findViewById(mViewID);
            if (null != mView && mView instanceof RadioGroup)
                ((RadioGroup) mView).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        mPopupAction.action(PopupwindowUtil.this, mContentView, group, checkedId);
                    }
                });
        }
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mClickAction(int mViewID, final PopupAction mPopupAction) {
        final View mContentView = getContentView();
        if (null != mContentView) {
            View mView = mContentView.findViewById(mViewID);
            if (null != mView)
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupAction.action(PopupwindowUtil.this, mContentView, null, -1);
                    }
                });
        }
        return mPopupwindowUtil;
    }

    public void mShowAtLocation(View parent, int gravity, int x, int y) {
        if (isShowing()) {
            return;
        }
        if (null != parent && null != parent.getContext() && parent.isShown()) {
            if (Overlayerable) {
                addOverlayer(parent.getWindowToken());
            }
            showAtLocation(parent, gravity, x, y);
        }
    }

    public void mshowAsDropDown(View parent) {
        if (isShowing()) {
            return;
        }
        if (null != parent && null != parent.getContext() && parent.isShown()) {
            if (Overlayerable) {
                addOverlayer(parent.getWindowToken());
            }
            showAsDropDown(parent);
        }
    }

    public void mshowAsDropDown(View parent, int xoff, int yoff) {
        if (isShowing()) {
            return;
        }
        if (null != parent && null != parent.getContext() && parent.isShown()) {
            if (Overlayerable) {
                addOverlayer(parent.getWindowToken());
            }
            showAsDropDown(parent, xoff, yoff);
        }
    }

    public void mshowAsDropDown(View parent, int xoff, int yoff, int gravity) {
        if (isShowing()) {
            return;
        }
        if (null != parent && null != parent.getContext() && parent.isShown()) {
            if (Overlayerable) {
                addOverlayer(parent.getWindowToken());
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                showAsDropDown(parent, xoff, yoff, gravity);
            }
        }
    }


    public void mshowAsDropDownWithBottomOverlayer(View parent) {
        if (isShowing()) {
            return;
        }
        if (null != parent && null != parent.getContext() && parent.isShown()) {
            if (Overlayerable) {
                addBottomOverlayer(parent);
            }
            showAsDropDown(parent);
        }
    }

    public PopupwindowUtil mDisMissListener(OnDismissListener mOnDismissListener) {
        setOnDismissListener(mOnDismissListener);
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mDisMissAction(int mViewID) {
        View mContentView = getContentView();
        if (null != mContentView) {
            View mView = mContentView.findViewById(mViewID);
            if (null != mView)
                mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDismiss();
                    }
                });
        }
        return mPopupwindowUtil;
    }

    public PopupwindowUtil mDisMissAction(View mView) {
        if (null != mView)
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDismiss();
                }
            });
        return mPopupwindowUtil;
    }

    private static void mDismiss() {
        if (null != mPopupwindowUtil && mPopupwindowUtil.isShowing())
            mPopupwindowUtil.dismiss();
    }

    public interface PopupAction {
        void action(PopupWindow mPopupWindow, @Nullable View mContentView, @Nullable RadioGroup group, int checkedId);
    }

    public interface ViewAction<T extends View> {
        void action(PopupwindowUtil mPopupwindowUtil, T mView);
    }

    public interface ViewActions<T extends View> {
        void action(T mView, int mViewId);
    }

    private void addBottomOverlayer(View mView) {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        int windowHeight = mWindowManager.getDefaultDisplay().getHeight();

        int[] location = new int[2];
        mView.getLocationInWindow(location);
        int height = mView.getMeasuredHeight();

        WindowManager.LayoutParams wl = new WindowManager.LayoutParams();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = windowHeight - location[1] - height;
        wl.format = PixelFormat.TRANSLUCENT;
        wl.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        wl.token = mView.getWindowToken();
        wl.gravity = Gravity.BOTTOM;

        mOverView = new View(mContext);
        mOverView.setBackgroundColor(0x99000000);
        mOverView.setFitsSystemWindows(true);
        mOverView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    removeMask();
                    return true;
                }
                return false;
            }
        });
        mWindowManager.addView(mOverView, wl);
    }

    private void addOverlayer(IBinder token) {
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        WindowManager.LayoutParams wl = new WindowManager.LayoutParams();
        wl.width = WindowManager.LayoutParams.MATCH_PARENT;
        wl.height = WindowManager.LayoutParams.MATCH_PARENT;
        wl.format = PixelFormat.TRANSLUCENT;
        wl.type = WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        wl.token = token;

        mOverView = new View(mContext);
        mOverView.setBackgroundColor(0x99000000);
        mOverView.setFitsSystemWindows(true);
        mOverView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    removeMask();
                    return true;
                }
                return false;
            }
        });
        mWindowManager.addView(mOverView, wl);
    }

    private void removeMask() {
        if (null != mOverView) {
            mWindowManager.removeViewImmediate(mOverView);
            mWindowManager = null;
            mOverView = null;
        }
    }

    public PopupwindowUtil setmSoftInputMode(int mode) {
        setSoftInputMode(mode);
        return mPopupwindowUtil;
    }

    @Override
    public void dismiss() {
        if (this.isShowing()) {
            if (mContext instanceof Activity) {
                if (!((Activity) mContext).isFinishing() && (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 || !((Activity) mContext).isDestroyed())) {
                    if (Overlayerable) {
                        removeMask();
                    }
                    super.dismiss();
                    mPopupwindowUtil = null;
                }
            }
        }
    }

    public void destory() {
        dismiss();
        mPopupwindowUtil = null;
    }
}


