package com.yunhualian.adapter;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;

import com.blankj.utilcode.util.Utils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.utils.DisplayUtils;

import java.util.List;

public class PicturesAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Context context;

    public PicturesAdapter(List<String> data, Context context) {
        super(R.layout.fragment_sort_pictures_item, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int position = helper.getPosition();
        CardView cardView = helper.getView(R.id.picture_layout);
        ImageView ivImage = helper.getView(R.id.hot_picture);
        if (position % 2 == 0) {
            ivImage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(context, 120)))
            ;
        } else {
            ivImage.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtils.dp2px(context, 165)));
        }
        ivImage.setImageResource(R.mipmap.shanshui);
    }
}
