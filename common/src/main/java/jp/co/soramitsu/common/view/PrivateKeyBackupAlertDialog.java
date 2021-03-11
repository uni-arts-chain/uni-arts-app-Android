package jp.co.soramitsu.common.view;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.w3c.dom.Text;

import jp.co.soramitsu.common.R;


public class PrivateKeyBackupAlertDialog extends Dialog implements View.OnClickListener {

    private View.OnClickListener listener;
    private String privateKey;
    private TextView tv;
    private RelativeLayout closeLayout;

    public PrivateKeyBackupAlertDialog(Context context) {
        super(context);
    }

    public PrivateKeyBackupAlertDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public PrivateKeyBackupAlertDialog(Context context, String privateKey, View.OnClickListener listener) {
        super(context);
        this.listener = listener;
        this.privateKey = privateKey;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_private_key_backup_alert);
        setCanceledOnTouchOutside(false);
        tv = findViewById(R.id.privateKey);
        tv.setText(privateKey);
        //初始化界面控件的事件
        initEvent();
    }

    private void initEvent() {
        findViewById(R.id.btn_confirm).setOnClickListener(this);
        findViewById(R.id.close).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_confirm) {// 确定
            ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = ClipData.newPlainText("", privateKey);
            cm.setPrimaryClip(mClipData);
            listener.onClick(v);
        } else {
            dismiss();
//            listener.onClick(v);
        }
    }

}
