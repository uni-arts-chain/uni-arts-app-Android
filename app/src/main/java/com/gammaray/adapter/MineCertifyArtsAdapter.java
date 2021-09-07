package com.gammaray.adapter;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;

import java.util.List;

public class MineCertifyArtsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MineCertifyArtsAdapter(List<String> data) {
        super(R.layout.my_certify_arts_item, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.picture_name, item);
    }
}
