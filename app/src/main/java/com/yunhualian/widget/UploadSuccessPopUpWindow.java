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
import com.yunhualian.databinding.PopupwindowUploadSccessBinding;

import java.util.List;


public class UploadSuccessPopUpWindow extends BasePopupWindow {
    private PopupwindowUploadSccessBinding mBinding;
    private OnBottomTextviewClickListener listener;

    public UploadSuccessPopUpWindow(Context context, OnBottomTextviewClickListener listener) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_upload_sccess, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
        mBinding.setHandlers(new ClickHandlers());
        mBinding.relRootView.setOnTouchListener((v, event) -> {
            dismiss();
            return false;
        });
        mBinding.cancle.setOnClickListener(v -> {
            listener.onCancleClick();
        });
        mBinding.confirm.setOnClickListener(v -> {
            listener.onPerformClick();
        });
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

}
