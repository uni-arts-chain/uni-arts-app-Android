package com.yunhualian.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yunhualian.R;
import com.yunhualian.databinding.PopupwindowDateSelectorOperatingBinding;
import com.yunhualian.databinding.PopupwindowSelectorOperatingBinding;
import com.zyyoona7.picker.base.BaseDatePickerView;
import com.zyyoona7.picker.listener.OnDateSelectedListener;
import com.zyyoona7.wheel.WheelView;

import java.util.Date;
import java.util.List;


public class UploadDateSelectPopUpWindow extends BasePopupWindow {
    private PopupwindowDateSelectorOperatingBinding mBinding;
    private OnPopItemClickListener listener;
    private long date;

    public UploadDateSelectPopUpWindow(Context context, OnPopItemClickListener listener) {
        super(context);
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_date_selector_operating, null, false);

        if (null == mBinding)
            return;
        setContentView(mBinding.getRoot());
//        mBinding.wheelview.setData(lists);
        mBinding.dpvDefault.setOnDateSelectedListener((datePickerView, year, month, day, date) -> {
            this.date = date.getTime();
        });
        mBinding.dpvDefault.setTextSize(24, true);
        mBinding.dpvDefault.setLabelTextSize(20);
        mBinding.canclle.setOnClickListener(v -> dismiss());
        mBinding.confirm.setOnClickListener(v -> {
            if (date < System.currentTimeMillis()) {
                ToastUtils.showShort("版税期限不应小于当前日期");
                return;
            }
            listener.onPopItemClick(date);
            dismiss();
        });
    }

    public interface OnPopItemClickListener {
        void onPopItemClick(long date);
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
