package com.yunhualian.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.entity.ArtPriceVo;

import java.util.List;

public class PrizeAdapter extends BaseQuickAdapter<ArtPriceVo, BaseViewHolder> {
    //    public HashMap<Integer, Integer> hashMap;
    public onSelectedListener onSelectedListener;
    int clickPosition = 999;
    boolean isInit = true;
    public PrizeAdapter(List<ArtPriceVo> data) {
        super(R.layout.fragment_sort_item, data);
//        hashMap = new HashMap<>();

    }

    public void addSelectedListener(onSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, ArtPriceVo item) {
//        helper.setText(R.id.name, item);
        final TextView textView = helper.getView(R.id.name);
//        hashMap.put(helper.getPosition(), 0);
        textView.setText(item.getTitle());
        textView.setOnClickListener(view -> {

            if (clickPosition == helper.getPosition()) {
                clickPosition = 999;
                if (null != onSelectedListener){
                    isInit = true;
                    onSelectedListener.onUnSelected(isInit,helper.getPosition());
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
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_sort_type_without_stroke));
            } else {
                textView.setTextColor(mContext.getResources().getColor(R.color._101010));
                textView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }
        }else{
            if (clickPosition == helper.getPosition()) {
                textView.setTextColor(mContext.getResources().getColor(R.color.white));
                textView.setBackground(mContext.getResources().getDrawable(R.drawable.shape_sort_type_without_stroke));
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

    public void selectTag(int clickPosition, boolean isInit) {
        this.clickPosition = clickPosition;
        this.isInit = isInit;
    }
}
