package com.yunhualian.adapter;


import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.base.YunApplication;
import com.yunhualian.entity.BlindBoxVo;
import com.yunhualian.entity.SellingArtVo;

import java.util.List;

public class BlindBoxDetailCardAdapter extends BaseQuickAdapter<BlindBoxVo.CardGroupsBean, BaseViewHolder> {

    public BlindBoxDetailCardAdapter(List<BlindBoxVo.CardGroupsBean> data) {
        super(R.layout.blindbox_card_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BlindBoxVo.CardGroupsBean item) {
        ImageView ivImage = helper.getView(R.id.hot_picture);
        Glide.with(mContext).load(item.getArt().getImg_main_file1().getUrl()).into(ivImage);

        if (TextUtils.isEmpty(item.getArt().getLive2d_file())) {
            helper.setVisible(R.id.live2d, false);
        } else helper.setVisible(R.id.live2d, true);

        if (TextUtils.isEmpty(item.getSpecial_attr())) {
            helper.setVisible(R.id.seldom, false);
        } else helper.setVisible(R.id.seldom, true);

        if (item.getArt().isIs_owner()) {
            helper.setVisible(R.id.already_have, true);
        } else helper.setVisible(R.id.already_have, false);
    }
}
