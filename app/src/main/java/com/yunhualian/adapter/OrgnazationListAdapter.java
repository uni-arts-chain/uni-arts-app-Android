package com.yunhualian.adapter;


import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;

import java.util.List;

public class OrgnazationListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Context context;

    public OrgnazationListAdapter(List<String> data, Context context) {
        super(R.layout.activity_orgnazation_list_tiem, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        Glide.with(mContext).load(R.mipmap.shanshui) //图片地址
                .into((ImageView) helper.getView(R.id.hot_picture));
        helper.addOnClickListener(R.id.pass);
        helper.addOnClickListener(R.id.refuse);
    }
}
