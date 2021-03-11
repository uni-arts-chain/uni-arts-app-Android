package jp.co.soramitsu.common.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import jp.co.soramitsu.common.R;


public class MnemonicBackupAlertDialog extends Dialog implements View.OnClickListener {

    private View.OnClickListener listener;

    public MnemonicBackupAlertDialog(Context context) {
        super(context);
    }

    public MnemonicBackupAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public MnemonicBackupAlertDialog(Context context, View.OnClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_mnemonic_backup_alert);
        setCanceledOnTouchOutside(false);
        //初始化界面控件的事件
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {// 确定
            dismiss();
            listener.onClick(v);
        }
    }

}
