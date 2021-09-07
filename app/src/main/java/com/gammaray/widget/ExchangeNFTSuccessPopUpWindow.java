package com.gammaray.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.gammaray.R;
import com.gammaray.databinding.PopupwindowExchangeSccessBinding;


public class ExchangeNFTSuccessPopUpWindow extends BasePopupWindow {
    private PopupwindowExchangeSccessBinding mBinding;
    private OnBottomTextviewClickListener listener;

    public ExchangeNFTSuccessPopUpWindow(Context context, OnBottomTextviewClickListener listener) {
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popupwindow_exchange_sccess, null, false);

        if (null == mBinding)
            return;

        setContentView(mBinding.getRoot());
        mBinding.relRootView.setOnTouchListener((v, event) -> {
            dismiss();
            return false;
        });
        mBinding.confirm.setOnClickListener(v -> {
            listener.onPerformClick();
        });
    }

    public interface OnPopItemClickListener {
        void onPopItemClick(View view, int position);
    }

    public interface OnBottomTextviewClickListener {

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
