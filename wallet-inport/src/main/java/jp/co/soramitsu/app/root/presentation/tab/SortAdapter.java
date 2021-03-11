package jp.co.soramitsu.app.root.presentation.tab;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;

import jp.co.soramitsu.inport.R;

public class SortAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public HashMap<Integer, Integer> hashMap;
    public onSelectedListener onSelectedListener;

    public SortAdapter(List<String> data) {
        super(R.layout.fragment_sort_item, data);
        hashMap = new HashMap<>();

    }

    public void addSelectedListener(onSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected void convert(final BaseViewHolder helper, String item) {
//        helper.setText(R.id.name, item);
        final TextView textView = helper.getView(R.id.name);
        hashMap.put(helper.getPosition(), 0);
        textView.setText(item);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hashMap.get(helper.getPosition()) == 0) {
                    hashMap.put(helper.getPosition(), 1);
                    textView.setTextColor(mContext.getResources().getColor(R.color.white));
                    textView.setBackgroundColor(mContext.getResources().getColor(R.color.picture_name_color));
                    if (null != onSelectedListener)
                        onSelectedListener.onSelected(helper.getPosition());
                } else {
                    hashMap.put(helper.getPosition(), 0);
                    textView.setTextColor(mContext.getResources().getColor(R.color.picture_name_color));
                    textView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    if (null != onSelectedListener)
                        onSelectedListener.onUnSelected(helper.getPosition());
                }
            }
        });

    }

    public interface onSelectedListener {
        void onSelected(int selectPosition);

        void onUnSelected(int selectPosition);
    }
}
