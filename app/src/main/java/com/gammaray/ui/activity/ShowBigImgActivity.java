package com.gammaray.ui.activity;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.gammaray.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class ShowBigImgActivity extends Activity {

    /**
     * 用于管理图片的滑动
     */
    private ViewPager viewPager;
    private LocalPicPagerAdapter picViewPager;
    private int position;
    /**
     * 显示当前图片的页数
     */
    private TextView pageText;
    private int[] image = {R.mipmap.img_eg1, R.mipmap.img_eg2, R.mipmap.img_eg3};
    private List<Map<String, Integer>> egPicList = new ArrayList<Map<String, Integer>>();
    private ArrayList<String> picPathList = new ArrayList<>();
    private boolean isArts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_img);
        if(getIntent() != null){
            position = getIntent().getIntExtra("index", 0);
            isArts = getIntent().getBooleanExtra("is_arts",false);
            picPathList = getIntent().getStringArrayListExtra("pic_paths");
        }
        pageText = (TextView) findViewById(R.id.page_text);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = null;
        if(!isArts){
            for (int i = 0; i < image.length; i++) {
                Map<String, Integer> map = new HashMap<String, Integer>();
                map.put("image", image[i]);
                egPicList.add(map);
            }
            adapter = new ViewPagerAdapter(ShowBigImgActivity.this, egPicList);
            viewPager.setAdapter(adapter);
            viewPager.setCurrentItem(position - 1);
            viewPager.setEnabled(false);
            pageText.setText(position + "/" + image.length);
        }else{
            picViewPager = new LocalPicPagerAdapter(this,picPathList);
            viewPager.setAdapter(picViewPager);
            viewPager.setCurrentItem(position - 1);
            viewPager.setEnabled(false);
            pageText.setText(position + "/" + picPathList.size());
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 每当页数发生改变时重新设定一遍当前的页数和总页数
                pageText.setText((position + 1) + "/" + image.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 设定当前的页数和总页数

        ImageView imageView = findViewById(R.id.close);
        imageView.setOnClickListener(v -> finish());
    }

    /**
     * ViewPager的适配器
     *
     * @author guolin
     */
    class ViewPagerAdapter extends PagerAdapter {
        List<Map<String, Integer>> list;
        private Context context;

        public ViewPagerAdapter(Context context, List<Map<String, Integer>> lists) {
            this.context = context;
            this.list = lists;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), list.get(position).get("image"));
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.icon_empty);
            }
            View view = LayoutInflater.from(ShowBigImgActivity.this).inflate(
                    R.layout.zoom_image_layout, null);
            PhotoView zoomImageView = view
                    .findViewById(R.id.photo_view);
            zoomImageView.setImageBitmap(bitmap);
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

    class LocalPicPagerAdapter extends PagerAdapter {
        List<String> list;
        private Context context;

        public LocalPicPagerAdapter(Context context, List<String> lists) {
            this.context = context;
            this.list = lists;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Bitmap bitmap=BitmapFactory.decodeFile(list.get(position));
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(getResources(),
                        R.mipmap.icon_empty);
            }
            View view = LayoutInflater.from(ShowBigImgActivity.this).inflate(
                    R.layout.zoom_image_layout, null);
            PhotoView zoomImageView = view.findViewById(R.id.photo_view);
            zoomImageView.setImageBitmap(bitmap);
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