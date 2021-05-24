package com.yunhualian.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.databinding.PopupwindowAssetsOperatingBinding;
import com.yunhualian.databinding.PopupwindowSelectorOperatingBinding;
import com.zyyoona7.wheel.WheelView;

import java.util.List;


public class UploadSelectorPopUpWindow extends BasePopupWindow {
    private PopupwindowSelectorOperatingBinding mBinding;
    private List<String> lists;
    private OnPopItemClickListener listener;
    private int index;

    public UploadSelectorPopUpWindow(Context context, List<String> lists, OnPopItemClickListener listener) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_selector_operating, null, false);

        if (null == mBinding)
            return;
        setContentView(mBinding.getRoot());
//        mBinding.wheelview.setData(lists);
        mBinding.wheelview.setOnItemSelectedListener(new WheelView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelView wheelView, Object data, int position) {
                index = position;
            }
        });
        mBinding.canclle.setOnClickListener(v -> dismiss());
        mBinding.confirm.setOnClickListener(v -> listener.onPopItemClick(index));
    }

    public interface OnPopItemClickListener {
        void onPopItemClick(int position);
    }

    public interface OnBottomTextviewClickListener {
        void onBottomClick();
    }

    public void setLists(List<String> lists) {
        if (mBinding.wheelview != null) {
            mBinding.wheelview.setData(lists);
        }
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
