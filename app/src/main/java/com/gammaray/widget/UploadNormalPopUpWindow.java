package com.gammaray.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gammaray.R;
import com.gammaray.databinding.PopupwindowAssetsOperatingBinding;

import java.util.List;


public class UploadNormalPopUpWindow extends BasePopupWindow {
    private PopupwindowAssetsOperatingBinding mBinding;
    private List<String> lists;
    private OnPopItemClickListener listener;
    PopWindowListAdapter adapter;

    public UploadNormalPopUpWindow(Context context, List<String> lists, OnPopItemClickListener listener) {
        super(context);
        this.lists = lists;
        this.listener = listener;
    }


    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_assets_operating, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
        adapter = new PopWindowListAdapter(lists);
        mBinding.setHandlers(new ClickHandlers());
        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            listener.onPopItemClick(view, position);
        });
        mBinding.lists.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.lists.setAdapter(adapter);// 可循环滚动
        mBinding.relRootView.setOnTouchListener((v, event) -> {
            dismiss();
            return false;
        });

    }

    public interface OnPopItemClickListener {
        void onPopItemClick(View view, int position);
    }

    public interface OnBottomTextviewClickListener {
        void onBottomClick();
    }

    public class ClickHandlers {

        public void onClickDeposit() {
            dismiss();

        }

        public void onClickWithdraw() {
            dismiss();

        }

        public void onClickTransfer() {
            dismiss();
        }
    }

    public void setLists(List<String> lists) {
        if (adapter != null) {
            adapter.setNewData(lists);
        }
    }

    public class PopWindowListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public PopWindowListAdapter(@Nullable List<String> data) {
            super(R.layout.pop_list_item, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.itemText, item).addOnClickListener(R.id.itemText);
        }

    }
}
