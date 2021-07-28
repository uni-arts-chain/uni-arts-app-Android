package com.yunhualian.adapter;

import android.view.View;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.entity.ArtTypeVo;

import java.util.HashMap;
import java.util.List;

public class SortAdapter extends BaseQuickAdapter<ArtTypeVo, BaseViewHolder> {
    public onSelectedListener onSelectedListener;
    int clickPosition = 999;
    private boolean isInit = true;

    public SortAdapter(List<ArtTypeVo> data) {
        super(R.layout.fragment_sort_item, data);

    }

    public void addSelectedListener(onSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, ArtTypeVo item) {
        final TextView textView = helper.getView(R.id.name);
        textView.setText(item.getTitle());
        textView.setOnClickListener(view -> {

            if (clickPosition == helper.getPosition()) {
                clickPosition = 999;
                if (null != onSelectedListener){
                    isInit = true;
                    onSelectedListener.onUnSelected(true,helper.getPosition());
                }
            } else {
                clickPosition = helper.getPosition();
                if (null != onSelectedListener){
                    isInit = false;
                    onSelectedListener.onSelected(false,helper.getPosition());
                }
            }
        });

        if(isInit){
            if (0 == helper.getPosition()) {
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
                textView.setBackground(mContext.getDrawable(R.drawable.shape_sort_type_without_stroke));

            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color._101010));
                textView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }else{
            if (clickPosition == helper.getPosition()) {
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
                textView.setBackground(mContext.getDrawable(R.drawable.shape_sort_type_without_stroke));

            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color._101010));
                textView.setBackgroundColor(mContext.getResources().getColor(R.color.white));

            }
        }
    }

    public interface onSelectedListener {
        void onSelected(boolean isInit,int selectPosition);

        void onUnSelected(boolean isInit,int selectPosition);
    }
}
