package com.yunhualian.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;

import java.util.List;

public class PicDetailAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public PicDetailAdapter(List<String> data) {
        super(R.layout.pic_detail_img2txt_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
//        helper.setText(R.id.picture_name, item);
    }
}
