package com.yunhualian.adapter;


import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.ArtTopicVo;
import com.yunhualian.entity.SellingArtVo;

import java.util.List;

public class CommonPictureAdapter extends BaseQuickAdapter<SellingArtVo, BaseViewHolder> {

    public CommonPictureAdapter(List<SellingArtVo> data) {
        super(R.layout.fragment_home_theme_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SellingArtVo item) {
        Glide.with(mContext).load(item.getImg_main_file1().getUrl()) //图片地址
                .into((ImageView) helper.getView(R.id.picture));
        helper.setText(R.id.picture_name, item.getName());
//        helper.setText(R.id.picture_type, item.getMaterial_desc());
//        helper.setText(R.id.certify_add, mContext.getString(R.string.centify_addr, item.getItem_hash()));
        helper.setText(R.id.picture_prize, YunApplication.PAY_CURRENCY.concat(" " + item.getPrice()));

    }
}
