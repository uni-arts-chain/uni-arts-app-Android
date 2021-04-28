package jp.co.soramitsu.app.root.presentation;


import org.jetbrains.annotations.NotNull;

import jp.co.soramitsu.common.base.BaseActivity;
import jp.co.soramitsu.inport.R;

public class VpActivity extends BaseActivity<RootViewModel> {

    @Override
    public void inject() {
        InjetsKt.injets(this);
    }

    @Override
    public int layoutResource() {
        return R.layout.activity_vp;
    }

    @Override
    public void initViews() {

    }


    @Override
    public void subscribe(@NotNull RootViewModel viewModel) {
        viewModel.getAssets();
    }
}