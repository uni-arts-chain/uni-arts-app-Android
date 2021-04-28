package com.yunhualian.adapter;


import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.entity.MessagesVo;
import com.yunhualian.utils.DateUtil;

import java.util.List;

public class MessagesAdapter extends BaseQuickAdapter<MessagesVo, BaseViewHolder> {

    public MessagesAdapter(List<MessagesVo> data) {
        super(R.layout.activity_messages_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessagesVo item) {
        helper.setText(R.id.title, item.getBody());
        helper.setText(R.id.time, DateUtil.dateToStringWith(item.getCreated_at() * 1000));
        if (item.isRead()) {
            helper.setVisible(R.id.noRead, false);
        } else {
            helper.setVisible(R.id.noRead, true);
        }
    }
}
