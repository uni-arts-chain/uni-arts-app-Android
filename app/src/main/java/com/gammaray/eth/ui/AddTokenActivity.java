package com.gammaray.eth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.constant.ExtraConstant;
import com.gammaray.entity.EventBusMessageEvent;
import com.gammaray.eth.entity.AddTokenBean;
import com.gammaray.eth.entity.ErrorEnvelope;
import com.gammaray.eth.entity.NetworkInfo;
import com.gammaray.eth.entity.RegiseResponse;
import com.gammaray.eth.entity.TokenInfo;
import com.gammaray.eth.repository.EthereumNetworkRepository;
import com.gammaray.eth.viewmodel.AddTokenViewModel;
import com.gammaray.eth.viewmodel.AddTokenViewModelFactory;
import com.gammaray.eth.viewmodel.TokensViewModel;
import com.gammaray.eth.viewmodel.TokensViewModelFactory;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.gammaray.eth.base.C.Key.WALLET;

/**
 * Created by Tiny熊
 * 微信: xlbxiong
 */

public class AddTokenActivity extends BaseActivity {

    TokensViewModelFactory tokensViewModelFactory;
    private TokensViewModel tokensViewModel;

    protected AddTokenViewModelFactory addTokenViewModelFactory;
    private AddTokenViewModel addTokenViewModel;

    private static final int SEARCH_ICO_TOKEN_REQUEST = 1000;


    List<AddTokenBean> mItems = new ArrayList<AddTokenBean>();

//    private AddTokenListAdapter mAdapter;

    List<String> tokensSelect;
    List<String> symbles;
    boolean isQts = false;
    public String walletAddre;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_new_property;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

    }

    public static class TokenItem {
        public final TokenInfo tokenInfo;
        public boolean added;
        public int iconId;

        public TokenItem(TokenInfo tokenInfo, boolean added, int id) {
            this.tokenInfo = tokenInfo;
            this.added = added;
            this.iconId = id;
        }
    }

    public void initDatas() {
        tokensSelect = getIntent().getExtras().getStringArrayList("tokens");
        symbles = getIntent().getExtras().getStringArrayList("symbles");
        isQts = getIntent().getExtras().getBoolean("is");
        walletAddre = getIntent().getExtras().getString(WALLET);
//        if (isMutipleWallet)
//            mItems = Arrays.asList(QtsTokenInforRepository.init().getTokenItems());
//        else if (isQts) {
//            mItems = QtsTokenInforRepository.init().getQtsTokenItems();
//        } else
//            mItems = QtsTokenInforRepository.init().getEthTokenItems();

        tokensViewModelFactory = new TokensViewModelFactory();
        tokensViewModel = ViewModelProviders.of(this, tokensViewModelFactory)
                .get(TokensViewModel.class);

        tokensViewModel.prepare();
        tokensViewModel.getAddTokens(null, isQts ? "QRC20" : "ERC20");
        tokensViewModel.addTokens().observe(this, this::onAddTokens);
        tokensViewModel.codes().observe(this, this::onCodes);
        tokensViewModel.error().observe(this, this::onError);
        tokensViewModel.registerResult().observe(this, this::registerResult);
        addTokenViewModelFactory = new AddTokenViewModelFactory();
        addTokenViewModel = ViewModelProviders.of(this, addTokenViewModelFactory)
                .get(AddTokenViewModel.class);
        showLoading(getString(R.string.progress_loading));
    }

    private void registerResult(RegiseResponse re) {
        LogUtils.e(re.getToken().toString());
    }

    private void onError(ErrorEnvelope errorEnvelope) {
        ToastUtils.showShort("请求失败");
        dismissLoading();
    }

    private void onCodes(String code) {
        ToastUtils.showLong(code);
    }

    private void onAddTokens(List<AddTokenBean> list) {
        mItems = list;
        for (AddTokenBean item : mItems) {
            for (String token : tokensSelect) {
                if (item.getAddress().equalsIgnoreCase(token)) {
                    item.setAdded(true);
                    break;
                } else item.setAdded(false);
            }
        }
//        mAdapter.setNewData(mItems);
        dismissLoading();
    }

    private boolean verqts(List<String> symbles) {

        for (String symble : symbles) {
            if (symble.equalsIgnoreCase("QTS")) {
                return true;
            }
        }
        return false;
    }


    public void onCheckedChanged(CompoundButton btn, boolean checked) {
        AddTokenBean info = (AddTokenBean) btn.getTag();
        info.setAdded(checked);
        LogUtils.d(info.toString() + ", checked:" + checked);
        NetworkInfo networkInfo = info.getSymbol().equalsIgnoreCase("qts") ? EthereumNetworkRepository.sSelf.getDefaultNetwork() : EthereumNetworkRepository.sSelf.getEthNetWork();
        if (checked) {
            addTokenViewModel.save(info.getName(), info.getAddress(), info.getSymbol(), info.getDecimals(), networkInfo, info.getIcon());
        } else
            addTokenViewModel.del(info.getAddress(), info.getSymbol(), info.getDecimals(), info.getIcon());
//            addTokenViewModel.update(info.getName(), info.getAddress(), info.getSymbol(), info.getDecimals(), networkInfo, info.getIcon(), checked);

        EventBus.getDefault().post(new EventBusMessageEvent(ExtraConstant.EVENT_SHOW_LODING, null));
    }


//    @Override
//    public void configViews() {
//        tokenList.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new AddTokenListAdapter(this, mItems, R.layout.list_item_add_ico_property);
//        mAdapter.setCurrentSyble(isQts);
//        tokenList.setAdapter(mAdapter);
//    }
//
//    @OnClick({R.id.rl_btn, R.id.layout_back})
//    public void onClick(View view) {
//        if (view.getId() == R.id.rl_btn) {
//            Intent intent = new Intent(this, AddCustomTokenActivity.class);
//            startActivityForResult(intent, SEARCH_ICO_TOKEN_REQUEST);
//        } else if (view.getId() == R.id.layout_back) {
//            finish();
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
