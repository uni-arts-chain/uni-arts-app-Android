package com.yunhualian.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;

import java.util.List;

public class SignCompanysAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public SignCompanysAdapter(List<String> data) {
        super(R.layout.sign_companys_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
//        helper.setText(R.id.picture_name, item);
    }
}
