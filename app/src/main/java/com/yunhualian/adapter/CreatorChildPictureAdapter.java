package com.yunhualian.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;

import java.util.List;

public class CreatorChildPictureAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CreatorChildPictureAdapter(List<String> data) {
        super(R.layout.creator_child_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
//        helper.setText(R.id.picture_name, item);
    }
}
