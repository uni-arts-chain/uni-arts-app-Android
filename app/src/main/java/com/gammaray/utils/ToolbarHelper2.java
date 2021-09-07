package com.gammaray.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.gammaray.R;


public class ToolbarHelper2 {

    public interface OnSingleMenuItemClickListener {
        void onSingleMenuItemClick();
    }

    private AppCompatActivity mActivity;
    private RelativeLayout mToolbar;
    private LinearLayout mLayoutBack;
    private LinearLayout mCenterToolbarView;
    private LinearLayout mLayoutMenu;
    private ImageView img_back;
    private TextView tv_menu;
    private ImageView img_menu;
    private View mUserView;
    private boolean bUserViewIsTitle = false;

    public ToolbarHelper2(AppCompatActivity activity, View view) {
        mActivity = activity;
        mToolbar = view.findViewById(R.id.toolbar);
        img_back = view.findViewById(R.id.img_back);
        mLayoutBack = view.findViewById(R.id.layout_back);
        mLayoutMenu = view.findViewById(R.id.layout_menu);
        img_menu = view.findViewById(R.id.img_menu);
    }

    public ToolbarHelper2(AppCompatActivity activity) {
        mActivity = activity;
        mToolbar = mActivity.findViewById(R.id.toolbar);
        img_back = mActivity.findViewById(R.id.img_back);
        mLayoutBack = mActivity.findViewById(R.id.layout_back);
        mLayoutMenu = mActivity.findViewById(R.id.layout_menu);
        img_menu = mActivity.findViewById(R.id.img_menu);
    }

    public void initToolbar(View view) {
        mCenterToolbarView = mToolbar.findViewById(R.id.centerToolbarView);
        mCenterToolbarView.removeAllViews();
        mCenterToolbarView.addView(view);
        mUserView = view;
    }

    public void initToolbar(String title) {
        initToolbar(createTitleView());
        setTitle(title);
    }

    public void initToolbar(String title, @ColorInt int resId) {
        initToolbar(createTitleView());
        setTitle(title, resId);
    }

    public void initToolbar(String title, @ColorInt int resId, @ColorInt int layoutColorId, int mTitlegravity) {
        initToolbar(createTitleView());
        setTitle(title, resId, layoutColorId, mTitlegravity);
    }

    public void initToolbar(String[] title) {
        initToolbar(createTitleViewWithSure());
        setTitle(title);
    }

    public void removeView(View view) {
        mCenterToolbarView.removeView(view);
    }

    private View createTitleView() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_title, null);
        bUserViewIsTitle = true;
        return view;
    }

    private View createTitleViewWithSure() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_title_with_sure, null);
        bUserViewIsTitle = true;
        return view;
    }

    public void setTitle(String title) {
        setTitle(title, 0);
    }

    public void setTitle(String title, @ColorInt int resId) {
        setTitle(title, 0, 0, -1001);
    }

    public void setTitle(String title, @ColorInt int resId, @ColorInt int layoutColorId, int mTitlegravity) {
        if (bUserViewIsTitle) {
            TextView tv_title = mUserView.findViewById(R.id.toolbar_title);
            tv_title.setText(title);

            if (0 != resId)
                tv_title.setTextColor(resId);
            if (0 != layoutColorId)
                mToolbar.setBackgroundColor(layoutColorId);
            if (mTitlegravity != -1001) {
                LinearLayout.LayoutParams mLayoutParams = tv_title.getLayoutParams() instanceof LinearLayout.LayoutParams ? (LinearLayout.LayoutParams) tv_title.getLayoutParams() : null;
                if (null != mLayoutParams) {
                    mLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    tv_title.setGravity(mTitlegravity);
                    tv_title.setLayoutParams(mLayoutParams);
                }
            }
        }
    }

    public void setTitle(String[] title) {
        if (bUserViewIsTitle) {
            TextView tv_title = mUserView.findViewById(R.id.toolbar_title);
            TextView tv_title_with_sure = mUserView.findViewById(R.id.toolbar_title_with_sure);
            tv_title.setText(title[0]);
            tv_title_with_sure.setText(title[1]);
        }
    }

    public TextView getTitleView() {
        return bUserViewIsTitle && null != mUserView ? mUserView.findViewById(R.id.toolbar_title) : null;
    }

    public void setBackNavigation(boolean hasBack, @DrawableRes int drawableResId, View.OnClickListener listener) {
        if (hasBack) {
            mLayoutBack.setVisibility(View.VISIBLE);
            img_back.setImageResource(drawableResId);
            mLayoutBack.setOnClickListener(listener);
        } else {
            mLayoutBack.setOnClickListener(null);
        }
    }

    public void setBackNavigation(boolean hasBack, View.OnClickListener listener) {
        if (hasBack) {
            mLayoutBack.setVisibility(View.VISIBLE);
            img_back.setImageResource(R.mipmap.icon_back_dark);
            mLayoutBack.setOnClickListener(listener);
        } else {
            mLayoutBack.setOnClickListener(null);
        }
    }

    public void setBackNavigationIcon(int resId, View.OnClickListener listener) {
        img_back.setImageResource(resId);
        mLayoutBack.setOnClickListener(listener);
    }

    public void setBackNavigationIcon(int resId) {
        img_back.setImageResource(resId);
    }

    public void setBackNavigationGone() {
        mLayoutBack.setVisibility(View.GONE);
    }

    public void setMenuGone(int mPostion) {
        (mPostion == 0 ? tv_menu : img_menu).setVisibility(View.GONE);
    }

    public void setSingleMenu(String title, @DrawableRes int resId, final OnSingleMenuItemClickListener listener) {
        if (resId == 0) {
            tv_menu.setVisibility(View.VISIBLE);
            tv_menu.setText(title);
            if (null != listener)
                tv_menu.setOnClickListener(v -> listener.onSingleMenuItemClick());
        } else {
            img_menu.setVisibility(View.VISIBLE);
            img_menu.setImageResource(resId);
            if (null != listener)
                img_menu.setOnClickListener(v -> listener.onSingleMenuItemClick());
        }

    }

    public void setSingleMenu(String title, @DrawableRes int resId, @ColorInt int colorResId, final OnSingleMenuItemClickListener listener) {
        if (resId == 0) {
            tv_menu.setVisibility(View.VISIBLE);
            tv_menu.setText(title);
            tv_menu.setTextColor(colorResId);
            if (null != listener)
                tv_menu.setOnClickListener(v -> listener.onSingleMenuItemClick());
        } else {
            img_menu.setVisibility(View.VISIBLE);
            img_menu.setImageResource(resId);
            if (null != listener)
                img_menu.setOnClickListener(v -> listener.onSingleMenuItemClick());
        }

    }

}
