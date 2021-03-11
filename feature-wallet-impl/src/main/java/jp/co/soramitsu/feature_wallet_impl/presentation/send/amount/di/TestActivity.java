package jp.co.soramitsu.feature_wallet_impl.presentation.send.amount.di;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.upbest.arouter.ArouterModelPath;

import javax.inject.Inject;

import jp.co.soramitsu.common.di.FeatureUtils;
import jp.co.soramitsu.feature_wallet_api.di.WalletFeatureApi2;
import jp.co.soramitsu.feature_wallet_api.domain.model.Fee;
import jp.co.soramitsu.feature_wallet_impl.R;
import jp.co.soramitsu.feature_wallet_impl.di.WalletFeatureComponent2;
import jp.co.soramitsu.feature_wallet_impl.presentation.send.amount.ChooseAmountViewModel2;

@Route(path = ArouterModelPath.Test_Activity)
public class TestActivity extends AppCompatActivity {
    @Inject
    ChooseAmountViewModel2 viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WalletFeatureComponent2 chooseAmountComponent2 = FeatureUtils.getFeature1(this, WalletFeatureApi2.class);
        chooseAmountComponent2.chooseAmountComponentFactory().create(this, "").inject(this);

        if (viewModel != null) {
            viewModel.getFeeLiveData().observe(this, this::onSuccess);
        }

    }

    private void onSuccess(Fee fee) {
//        ToastUtil.show(this, fee.getFeeAmount().toPlainString());
    }

}