package jp.co.soramitsu.app.root.presentation.tab;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import jp.co.soramitsu.inport.R;

public class LoadingDialog extends Dialog {

    private TextView loadingText;
    private ProgressBar pb;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialog);

        if (context instanceof Activity)
            setOwnerActivity((Activity) context);

        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View rootView = inflater.inflate(R.layout.layout_dialog_loading, null);
        loadingText = rootView.findViewById(R.id.loadingText);
        pb = rootView.findViewById(R.id.pb);
        if (android.os.Build.VERSION.SDK_INT > 22) {
            final Drawable drawable = ContextCompat.getDrawable(getContext().getApplicationContext(),R.drawable.progress_drawable_white_v22);
            pb.setIndeterminateDrawable(drawable);
        }
        setCanceledOnTouchOutside(false);
        setCancelable(true);
        setContentView(rootView);
    }

    public void setLoadingText(String text) {
        if (TextUtils.isEmpty(text)) {
            loadingText.setVisibility(View.GONE);
        } else {
            loadingText.setText(text);
        }
    }
}
