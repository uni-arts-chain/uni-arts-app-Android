package jp.co.soramitsu.app.root.presentation.tab;


import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Arrays;
import java.util.List;

import jp.co.soramitsu.app.App;
import jp.co.soramitsu.inport.R;

public class CreatorParentAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    CreatorChildPictureAdapter homePageThemeAdapter;
    private List<String> sortList;

    public CreatorParentAdapter(List<String> data, Context context) {
        super(R.layout.fragment_creator_parent_item, data);
        sortList = Arrays.asList(context.getResources().getStringArray(R.array.popular));
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int position = helper.getPosition();
        homePageThemeAdapter = new CreatorChildPictureAdapter(getData());
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(mContext);
        sortLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        RecyclerView recyclerView = helper.getView(R.id.picture_list);
        recyclerView.setLayoutManager(sortLayoutManager);
        recyclerView.setAdapter(homePageThemeAdapter);
    }
}
