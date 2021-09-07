package com.gammaray.ui.activity.wallet;

import android.content.Intent;
import android.text.TextUtils;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.igexin.sdk.PushManager;
import com.upbest.arouter.EventBusMessageEvent;
import com.upbest.arouter.EventEntity;
import com.upbest.arouter.Extras;
import com.gammaray.R;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.base.YunApplication;
import com.gammaray.databinding.ActivityAcountBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.UserVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.PinCodeKtActivity;
import com.gammaray.utils.SharedPreUtils;

import org.bouncycastle.util.encoders.Hex;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;

import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType;
import jp.co.soramitsu.fearless_utils.encrypt.SignatureWrapper;
import jp.co.soramitsu.fearless_utils.encrypt.Signer;
import jp.co.soramitsu.fearless_utils.encrypt.model.Keypair;
import jp.co.soramitsu.feature_account_impl.presentation.importing.ImportAccountFragment;
import retrofit2.Call;
import retrofit2.Response;

import static com.gammaray.ui.activity.wallet.WalletEditActivity.RESUME_CER;
import static com.gammaray.ui.activity.wallet.WalletEditActivity.SET_CER;

public class ImportWalletActivity extends BaseActivity<ActivityAcountBinding> {
    private ArrayList<String> mnemonicList;

    @Override
    public int getLayoutId() {
        return R.layout.activity_acount;
    }

    @Override
    public void initPresenter() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusMessageEvent eventBusMessageEvent) {
        if (eventBusMessageEvent != null) {
            if (eventBusMessageEvent.getmMessage().equals(EventEntity.EVENT_IMPORT_SUCCESS)) {
                saveData();
            }
        }
    }

    private void saveData() {

        if (!TextUtils.isEmpty(Extras.Address)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_ADDRESS, Extras.Address);
        }
        if (!TextUtils.isEmpty(Extras.publicKey)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_PUBLICKEY, Extras.publicKey);
        }
        if (!TextUtils.isEmpty(Extras.seed)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_SEED, Extras.seed);
        }
        if (!TextUtils.isEmpty(Extras.nonce)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_NONCE, Extras.nonce);
        }
        if (!TextUtils.isEmpty(Extras.privateKey)) {
            SharedPreUtils.setString(this, SharedPreUtils.KEY_PRIVATE, Extras.privateKey);
        }

        loginByAddress();
    }

    public void loginByAddress() {
        showLoading("加载中...");
        String privateKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PRIVATE);
        String publicKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PUBLICKEY);
        String nonce = SharedPreUtils.getString(this, SharedPreUtils.KEY_NONCE);
        String Address = SharedPreUtils.getString(this, SharedPreUtils.KEY_ADDRESS);
        LogUtils.e(privateKey + "|" + publicKey + "|" + nonce);
        Keypair keypair = new Keypair(Hex.decode(privateKey), Hex.decode(publicKey), Hex.decode(nonce.substring(2)));
        Signer signer = new Signer();
        SignatureWrapper signatureWrapper = signer.sign(EncryptionType.SR25519, Address.getBytes(), keypair);
        String singStr2 = Hex.toHexString(signatureWrapper.getSignature());
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("address", Address);
        hashMap.put("message", Address);
        hashMap.put("signature", singStr2);
        hashMap.put("cid", PushManager.getInstance().getClientid(this));
        hashMap.put("os", "android");
        RequestManager.instance().addressLogin(hashMap, new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null)
                        if (response.body().getBody() != null) {
                            UserVo userVo = response.body().getBody();
                            YunApplication.setmUserVo(userVo);
                            YunApplication.setToken(userVo.getToken());
                            ToastUtils.showShort("导入成功");
                            Intent intent = new Intent(ImportWalletActivity.this, PinCodeKtActivity.class);
                            intent.putExtra(RESUME_CER, true);
                            intent.putExtra(SET_CER, true);
                            startActivity(intent);
                            EventBus.getDefault().post(new EventBusMessageEvent(EventEntity.EVENT_IMPORT_COMPLETE, null));
                            finish();
                        }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    @Override
    public void initView() {

        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.import_wallet;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        EventBus.getDefault().register(this);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        ImportAccountFragment otcFragment = new ImportAccountFragment();
        mFragmentTransaction.add(R.id.container, otcFragment, "");
        mFragmentTransaction.show(otcFragment);
        mFragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}