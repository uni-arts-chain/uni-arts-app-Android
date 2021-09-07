package com.gammaray.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.gammaray.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class ShowNetBigImgActivity extends Activity implements ViewPager.OnPageChangeListener {

    /**
     * 用于管理图片的滑动
     */
    private ViewPager viewPager;
    private int position;
    /**
     * 显示当前图片的页数
     */
    private TextView pageText;
    private List<String> list = new ArrayList<>();
    private int currentIndex;
    private boolean isNeedShowClose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_img);
        position = getIntent().getIntExtra("index", BigDecimal.ONE.intValue());
        list = getIntent().getStringArrayListExtra("url");
        isNeedShowClose = getIntent().getBooleanExtra("isNeedClose",true);
        pageText = findViewById(R.id.page_text);
        viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(ShowNetBigImgActivity.this, list);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position - 1);
        viewPager.setOnPageChangeListener(this);
        viewPager.setEnabled(false);
        // 设定当前的页数和总页数
        pageText.setText(position + "/" + list.size());
        ImageView imageView = findViewById(R.id.close);
        imageView.setOnClickListener(v -> finish());
        if(isNeedShowClose){
            imageView.setVisibility(View.VISIBLE);
        }else{
            imageView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        // 每当页数发生改变时重新设定一遍当前的页数和总页数
        pageText.setText((arg0 + 1) + "/" + list.size());
    }

    /**
     * ViewPager的适配器
     *
     * @author guolin
     */
    class ViewPagerAdapter extends PagerAdapter {
        List<String> list;
        private Context context;

        public ViewPagerAdapter(Context context, List<String> lists) {
            this.context = context;
            this.list = lists;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(ShowNetBigImgActivity.this).inflate(
                    R.layout.zoom_image_layout, null);
            PhotoView zoomImageView = view
                    .findViewById(R.id.photo_view);
            Glide.with(context).load(list.get(position)).into(zoomImageView);
            zoomImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isNeedShowClose){
                        finish();
                    }
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }
}