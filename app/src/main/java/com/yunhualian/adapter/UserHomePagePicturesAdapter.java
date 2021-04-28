package com.yunhualian.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.ArtBean;
import com.yunhualian.entity.SellingArtVo;

import java.util.List;

public class UserHomePagePicturesAdapter extends BaseQuickAdapter<SellingArtVo, BaseViewHolder> {

    public UserHomePagePicturesAdapter(List<SellingArtVo> data) {
        super(R.layout.fragment_sort_pictures_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, SellingArtVo item) {
        int position = helper.getPosition();
        ImageView ivImage = helper.getView(R.id.hot_picture);
        Glide.with(mContext).clear(ivImage);
        Glide.with(mContext).load(item.getImg_main_file1().getUrl()).into(ivImage);
        helper.setText(R.id.picture_name, item.getName());

        helper.setText(R.id.picture_prize, YunApplication.PAY_CURRENCY.concat(" " + item.getPrice()));

    }
}
