package com.gammaray.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;

import java.util.List;

public class MyPicturesAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MyPicturesAdapter(List<String> data) {
        super(R.layout.fragment_my_pictures_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int position = helper.getPosition();
        ImageView ivImage = helper.getView(R.id.picture);
    }
}
