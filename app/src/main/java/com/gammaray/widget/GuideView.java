package com.gammaray.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.gammaray.R;
import com.gammaray.utils.DensityUtils;

import java.util.ArrayList;

/**
 * @Description: 引导页控件
 * @Author: Houbc
 * @CreateDate: 2020/7/14 11:11
 * @UpdateUser:
 * @UpdateDate:
 * @UpdateRemark:
 * @VersionLog: 1.0
 */

public class GuideView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private Context mContext;
    private ViewPager mViewPager;
    private LinearLayout mPointContent;
    private FrameLayout mEnterContent;
    private int pageSize;
    private ArrayList<View> mPageViews;
    private ArrayList<ImageView> mPointView;
    private Bitmap selectPoint, unselectPoint;
    private GuideCallBack guideCallBack;

    public GuideView(@NonNull Context context) {
        this(context, null);
    }

    public GuideView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GuideView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mPageViews = new ArrayList<View>();
        mPointView = new ArrayList<ImageView>();
        View rootView = LayoutInflater.from(context).inflate(R.layout.guilde_view_layout, this, true);
        mViewPager = rootView.findViewById(R.id.guide_viewpager);
        mPointContent = rootView.findViewById(R.id.point_content);
        mEnterContent = rootView.findViewById(R.id.rl_enter);
    }

    public void setData(int[] pageImages, int[] guidePoint, int res, final GuideCallBack guideCallBack) {
        this.guideCallBack = guideCallBack;
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        pageSize = pageImages.length;

        for (int i = 0; i < pageSize; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource((pageImages[i]));
            mPageViews.add(iv);
        }

        mViewPager.setAdapter(new GuideAdapter(mPageViews));
        mViewPager.addOnPageChangeListener(this);

        if (null != guidePoint)
            initPointViews(guidePoint);

        View view = View.inflate(mContext, res, null);
        if (null != view) {
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != guideCallBack) {
                        guideCallBack.onEnterClickListener();
                    }
                }
            });
            mEnterContent.addView(view);
        }
    }

    private void initPointViews(int[] guidePoint) {
        selectPoint = BitmapFactory.decodeResource(getResources(), guidePoint[0]);
        unselectPoint = BitmapFactory.decodeResource(getResources(), guidePoint[1]);
        mPointView = new ArrayList<ImageView>();
        mPointContent.removeAllViews();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mParams.setMargins(DensityUtils.dp2px(getContext(), 7), 0, 0, 0);
        for (int i = 0; i < pageSize; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setLayoutParams(mParams);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageBitmap(unselectPoint);
            mPointView.add(iv);
            mPointContent.addView(iv);
        }
        switchHighlightPoint(0);
    }

    private void switchHighlightPoint(int index) {
        if (index < 0 || index > pageSize - 1) {
            return;
        }
        int size = mPointView.size();
        for (int i = 0; i < size; i++) {
            if (index == i) {
                mPointView.get(i).setImageBitmap(selectPoint);
            } else {
                mPointView.get(i).setImageBitmap(unselectPoint);
            }
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        switchHighlightPoint(i);
        if (null != guideCallBack) {
            guideCallBack.slidingPosition(i);
            if (i == pageSize - 1) {
                mPageViews.get(pageSize - 1).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != guideCallBack)
                            guideCallBack.onEndClickListener();
                    }
                });
                guideCallBack.slidingEnd();
            }
        }

        if (null != mEnterContent)
            mEnterContent.setVisibility(i == pageSize - 1 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    public void clear() {
        pageSize = 0;
        clearPageViews();
        clearPointView();
        clearBitmap();
    }

    private void clearPageViews() {
        if (null != mPageViews) {
            int size = mPageViews.size();
            for (int i = 0; i < size; i++) {
                mPageViews.get(i).setBackgroundResource(0);
            }
            mPageViews.clear();
        }
        mPageViews = null;
    }

    private void clearPointView() {
        if (null != mPointView) {
            int size = mPointView.size();
            for (int i = 0; i < size; i++) {
                mPointView.get(i).setBackgroundResource(0);
            }
            mPointView.clear();
        }
        mPointView = null;
    }

    private void clearBitmap() {
        if (!selectPoint.isRecycled()) {
            selectPoint.recycle();
            selectPoint = null;
        }
        if (!unselectPoint.isRecycled()) {
            unselectPoint.recycle();
            unselectPoint = null;
        }
    }

    public static class GuideAdapter extends PagerAdapter {
        private ArrayList<View> views;

        GuideAdapter(ArrayList<View> views) {
            this.views = views;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(views.get(position));
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(views.get(position));
            return views.get(position);
        }
    }

    public interface GuideCallBack {

        void slidingPosition(int position);

        void slidingEnd();

        void onEndClickListener();

        void onEnterClickListener();
    }
}