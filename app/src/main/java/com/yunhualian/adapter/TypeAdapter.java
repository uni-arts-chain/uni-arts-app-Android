package com.yunhualian.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.entity.ArtMaterialVo;
import com.yunhualian.entity.ArtTypeVo;

import java.util.List;

public class TypeAdapter extends BaseQuickAdapter<ArtTypeVo, BaseViewHolder> {
    //    public HashMap<Integer, Integer> hashMap;
    public onSelectedListener onSelectedListener;
    int clickPosition = 999;

    public TypeAdapter(List<ArtTypeVo> data) {
        super(R.layout.fragment_sort_item, data);
//        hashMap = new HashMap<>();

    }

    public void addSelectedListener(onSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, ArtTypeVo item) {
//        helper.setText(R.id.name, item);
        final TextView textView = helper.getView(R.id.name);
//        hashMap.put(helper.getPosition(), 0);
        textView.setText(item.getTitle());
        textView.setOnClickListener(view -> {

            if (clickPosition == helper.getPosition()) {
                clickPosition = 999;
                if (null != onSelectedListener)
                    onSelectedListener.onUnSelected(helper.getPosition());
            } else {
                clickPosition = helper.getPosition();
                if (null != onSelectedListener)
                    onSelectedListener.onSelected(helper.getPosition());
            }


//                if (hashMap.get(helper.getPosition()) == 0) {
//                    hashMap.put(helper.getPosition(), 1);
//                    textView.setTextColor(mContext.getResources().getColor(R.color.white));
//                    textView.setBackgroundColor(mContext.getResources().getColor(R.color.picture_name_color));
//                    if (null != onSelectedListener)
//                        onSelectedListener.onSelected(helper.getPosition());
//                } else {
//                    hashMap.put(helper.getPosition(), 0);
//                    textView.setTextColor(mContext.getResources().getColor(R.color.picture_name_color));
//                    textView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
//                    if (null != onSelectedListener)
//                        onSelectedListener.onUnSelected(helper.getPosition());
//                }
        });


        if (clickPosition == helper.getPosition()) {
            textView.setTextColor(mContext.getResources().getColor(R.color.white));
            textView.setBackgroundColor(mContext.getResources().getColor(R.color.picture_name_color));

        } else {
            textView.setTextColor(mContext.getResources().getColor(R.color.picture_name_color));
            textView.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }

    }

    public interface onSelectedListener {
        void onSelected(int selectPosition);

        void onUnSelected(int selectPosition);
    }
}
