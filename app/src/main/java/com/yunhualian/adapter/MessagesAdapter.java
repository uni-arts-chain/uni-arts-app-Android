package com.yunhualian.adapter;


import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.utils.DisplayUtils;

import java.util.List;

public class MessagesAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    Context context;

    public MessagesAdapter(List<String> data, Context context) {
        super(R.layout.activity_messages_item, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
