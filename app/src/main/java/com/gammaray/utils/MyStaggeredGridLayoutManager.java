package com.gammaray.utils;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.ScreenUtils;


/**
 * @author Created by yyj on 2018/8/8
 * @descride 解决Scrollview中嵌套RecyclerView实现瀑布流时无法显示的问题，同时修复了子View显示时底部多出空白区域的问题
 */
public class MyStaggeredGridLayoutManager extends StaggeredGridLayoutManager {


    public MyStaggeredGridLayoutManager(int spanCount, int orientation) {
        super(spanCount, orientation);
        mHeightArray = new int[spanCount];
        for (int i = 0; i < spanCount; i++)
            mHeightArray[i] = 0;
    }

    private int[] mMeasuredDimension = new int[2];
    private int[] mHeightArray;

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);

        int width = 0;
        int height = 0;
        int count = getItemCount();
        int span = getSpanCount();
        for (int i = 0; i < span; i++)//防止多次调用onMeasure方法造成数据叠加
            mHeightArray[i] = 0;

        for (int i = 0; i < count; i++) {
            measureScrapChild(recycler, i,
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    mMeasuredDimension);
            if (getOrientation() == HORIZONTAL)
                calculatorStaggeredHeight(mMeasuredDimension[0]);
            else
                calculatorStaggeredHeight(mMeasuredDimension[1]);
        }
        if (getOrientation() == HORIZONTAL) {
            width = sort(mHeightArray);
            height = ScreenUtils.getScreenHeight();//获取屏幕高度
        } else {
            height = sort(mHeightArray);
            width = ScreenUtils.getScreenHeight();//获取屏幕宽度
        }
        switch (widthMode) {
            case View.MeasureSpec.EXACTLY:
                width = widthSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }

        switch (heightMode) {
            case View.MeasureSpec.EXACTLY:
                height = heightSize;
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.UNSPECIFIED:
        }
        setMeasuredDimension(width, height);
    }

    /**
     * 冒泡排序返回数组最大值
     *
     * @param unsorted
     * @return
     */
    private int sort(int[] unsorted) {
        for (int i = 0; i < unsorted.length; i++) {
            for (int j = i; j < unsorted.length; j++) {
                if (unsorted[i] < unsorted[j]) {
                    int temp = unsorted[i];
                    unsorted[i] = unsorted[j];
                    unsorted[j] = temp;
                }
            }
        }
        return unsorted[0];
    }

    /**
     * 将传入的item高度值赋给当前数组中最小的元素
     *
     * @param singleViewHeight 传入的item高度
     */
    private void calculatorStaggeredHeight(int singleViewHeight) {
        int index = 0;
        int minValue = mHeightArray[0];
        for (int i = 1; i < mHeightArray.length; i++) {
            if (minValue > mHeightArray[i]) {
                minValue = mHeightArray[i];
                index = i;
            }
        }
        mHeightArray[index] += singleViewHeight;
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
                                   int heightSpec, int[] measuredDimension) {
        if (position < getItemCount()) {
            try {
                View view = recycler.getViewForPosition(position);//fix 动态添加时报IndexOutOfBoundsException
                if (view != null) {
                    RecyclerView.LayoutParams p = (RecyclerView.LayoutParams) view.getLayoutParams();
                    int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec,
                            getPaddingLeft() + getPaddingRight(), p.width);
                    int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec,
                            getPaddingTop() + getPaddingBottom(), p.height);
                    view.measure(childWidthSpec, childHeightSpec);
                    measuredDimension[0] = view.getMeasuredWidth() + p.leftMargin + p.rightMargin;
                    measuredDimension[1] = view.getMeasuredHeight() + p.bottomMargin + p.topMargin;

                    Log.v("p.height", p.height + "");
                    Log.v("measuredDimension[1]", measuredDimension[1] + "");

                    recycler.recycleView(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
