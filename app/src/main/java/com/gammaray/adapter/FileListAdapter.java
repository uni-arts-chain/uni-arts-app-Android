package com.gammaray.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.zp.z_file.content.ZFileBean;

import java.util.List;

public class FileListAdapter extends BaseQuickAdapter<ZFileBean, BaseViewHolder> {

    public FileListAdapter(List<ZFileBean> data) {
        super(R.layout.file_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ZFileBean item) {
        helper.setText(R.id.item_super_nameTxt, item.getFileName());
        helper.setText(R.id.item_super_dateTxt, item.getDate());
        helper.setText(R.id.item_super_sizeTxt, item.getSize());
    }
}
