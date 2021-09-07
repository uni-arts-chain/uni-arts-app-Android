package com.gammaray.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;

import com.gammaray.R;
import com.gammaray.databinding.PopupwindowLoadingBinding;

import java.util.List;


public class LoadingPop extends BasePopupWindow {


    private PopupwindowLoadingBinding mBinding;
    private List<String> lists;

    public LoadingPop(Context context) {
        super(context);
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

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.popupwindow_loading, null, false);

        if (null == mBinding)
            return;
        setContentView(mBinding.getRoot());

    }

    public void setProgress(int rate) {
        mBinding.progress.setSecondaryProgress(rate);
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

    }

}
