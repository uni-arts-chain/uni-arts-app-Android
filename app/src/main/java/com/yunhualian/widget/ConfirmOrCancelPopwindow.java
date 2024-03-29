package com.yunhualian.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.yunhualian.R;
import com.yunhualian.databinding.PopConfirmOrCancelLayoutBinding;


public class ConfirmOrCancelPopwindow extends BasePopupWindow {
    private PopConfirmOrCancelLayoutBinding mBinding;
    private OnBottomTextviewClickListener listener;

    public ConfirmOrCancelPopwindow(Context context, OnBottomTextviewClickListener listener) {
        super(context);
        this.listener = listener;
    }


    @Override
    protected void initPopupWindow() {
        super.initPopupWindow();

//        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
//        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void iniWindow() {
        super.iniWindow();

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.pop_confirm_or_cancel_layout, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
        mBinding.setHandlers(new ClickHandlers());
        mBinding.cancle.setOnClickListener(v -> {
            listener.onCancleClick();
        });
        mBinding.confirm.setOnClickListener(v -> {
            listener.onPerformClick();
        });
        mBinding.imgClose.setOnClickListener(view -> dismiss());
    }

    public void setOneKey(boolean status) {
        if (status) {
            mBinding.cancle.setVisibility(View.GONE);
        }
    }

    public interface OnPopItemClickListener {
        void onPopItemClick(View view, int position);
    }

    public interface OnBottomTextviewClickListener {
        void onCancleClick();

        void onPerformClick();
    }

    public class ClickHandlers {

        public void onClickDeposit() {
            dismiss();

        }

        public void onClickWithdraw() {
            dismiss();
        }

    }

    public void setContent(String str) {
        mBinding.content.setText(str);
    }

    public void setCancleText(String string) {
        if (!TextUtils.isEmpty(string))
            mBinding.cancle.setText(string);
    }

    public void setConfirmText(String string) {
        if (!TextUtils.isEmpty(string))
            mBinding.confirm.setText(string);
    }
}
