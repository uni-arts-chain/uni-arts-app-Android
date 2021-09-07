package com.gammaray.adapter;


import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.entity.FollowerVO;

import java.util.List;

public class FollowAdapter extends BaseQuickAdapter<FollowerVO, BaseViewHolder> {

    public FollowAdapter(List<FollowerVO> data) {
        super(R.layout.follow_fans_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FollowerVO item) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.mipmap.icon_default_head);
        if (!TextUtils.isEmpty(item.getAvatar().getUrl()))
            Glide.with(mContext).load(item.getAvatar().getUrl()).apply(requestOptions)
                    .into((ImageView) helper.getView(R.id.mine_title_img));
        if (TextUtils.isEmpty(item.getDisplay_name()))
            helper.setText(R.id.nick_name, mContext.getString(R.string.no_display_name));
        else
            helper.setText(R.id.nick_name, item.getDisplay_name());
        helper.setText(R.id.follow, mContext.getString(R.string.arts_num, String.valueOf(item.getArt_size())));
        if (item.isFollow_by_me()) {
            helper.setBackgroundRes(R.id.focus, R.drawable.shape_bg_black);
            helper.setTextColor(R.id.focus, mContext.getResources().getColor(R.color._101010));
            helper.setText(R.id.focus, mContext.getString(R.string.canel_follow));
        } else {
            helper.setBackgroundRes(R.id.focus, R.drawable.shape_btn_bgcolor_black);
            helper.setTextColor(R.id.focus, mContext.getResources().getColor(R.color.white));
            helper.setText(R.id.focus, mContext.getString(R.string.text_follow));
        }
        helper.addOnClickListener(R.id.focus);
    }
}
