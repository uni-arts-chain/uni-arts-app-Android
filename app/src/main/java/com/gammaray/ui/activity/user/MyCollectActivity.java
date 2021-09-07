package com.gammaray.ui.activity.user;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.blankj.utilcode.util.LogUtils;
import com.igexin.sdk.PushManager;
import com.gammaray.R;
import com.gammaray.adapter.CollectionAdapter;
import com.gammaray.base.BaseActivity;
import com.gammaray.base.ToolBarOptions;
import com.gammaray.databinding.ActivityMyCollectBinding;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.CollectArtVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.art.ArtDetailActivity;
import com.gammaray.utils.SharedPreUtils;

import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType;
import jp.co.soramitsu.fearless_utils.encrypt.SignatureWrapper;
import jp.co.soramitsu.fearless_utils.encrypt.Signer;
import jp.co.soramitsu.fearless_utils.encrypt.model.Keypair;
import retrofit2.Call;
import retrofit2.Response;


/*
 * 我的收藏
 * */
public class MyCollectActivity extends BaseActivity<ActivityMyCollectBinding> {

    private CollectionAdapter popularAdapter;
    private int page = 1;
    private List<CollectArtVo> collectList;

    private String PREFS_SECURITY_SOURCE_MASK = "security_source_%s";

    private String SHARED_PREFERENCES_FILE = "fearless_prefs";


    @Override
    public int getLayoutId() {
        return R.layout.activity_my_collect;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.my_collect_title;
        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);
        collectList = new ArrayList<>();
        popularAdapter = new CollectionAdapter(collectList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mDataBinding.messageList.setLayoutManager(layoutManager);
        mDataBinding.messageList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                layoutManager.invalidateSpanAssignments();
            }
        });
        popularAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.messageList);
        mDataBinding.messageList.setAdapter(popularAdapter);
        popularAdapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
//            bundle.putSerializable(ArtDetailActivity.ART_KEY, collectList.get(position));
            bundle.putInt(ArtDetailActivity.ART_ID, collectList.get(position).getFavoritable().getId());
            startActivity(ArtDetailActivity.class, bundle);
        });
        mDataBinding.swipeRefresh.setOnRefreshListener(() -> {
            getPopular();
            mDataBinding.swipeRefresh.setRefreshing(false);
        });
        getPopular();
//        getRaw();
    }

    public void getPopular() {
        RequestManager.instance().queryCollect(page, 20, new MinerCallback<BaseResponseVo<List<CollectArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<CollectArtVo>>> call, Response<BaseResponseVo<List<CollectArtVo>>> response) {
                if (response.isSuccessful()) {
                    collectList = response.body().getBody();
                    if (collectList.size() > 0) {
                        popularAdapter.setNewData(collectList);
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<CollectArtVo>>> call, Response<BaseResponseVo<List<CollectArtVo>>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });

    }

    private void getRaw() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);


        String address = SharedPreUtils.getString(this, SharedPreUtils.KEY_ADDRESS);
        String seed = SharedPreUtils.getString(this, SharedPreUtils.KEY_SEED);
        String publicKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PUBLICKEY);
        String privateKey = SharedPreUtils.getString(this, SharedPreUtils.KEY_PRIVATE);
        String seedst = seed.substring(2);
        String nonce = "4092de3e07c259de3904634415177bc07135446ed7d84e43ff246025780c0af1";
        Keypair keypair = new Keypair(Hex.decode(privateKey), Hex.decode(publicKey), Hex.decode(nonce));
        Signer signer = new Signer();
        SignatureWrapper signatureWrapper = signer.sign(EncryptionType.SR25519, address.getBytes(), keypair);
        String singStr2 = Hex.toHexString(signatureWrapper.getSignature());
        LogUtils.e("sinStr22 == " + singStr2);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("address", SharedPreUtils.getString(this, SharedPreUtils.KEY_ADDRESS));
        hashMap.put("message", SharedPreUtils.getString(this, SharedPreUtils.KEY_ADDRESS));
        hashMap.put("signature", singStr2);
        hashMap.put("cid", PushManager.getInstance().getClientid(this));
        hashMap.put("os", "android");

    }

    private View mEmptyView;

    private View getEmptyView() {
        if (null == mEmptyView)
            mEmptyView = LayoutInflater.from(this).inflate(R.layout.layout_entrust_empty, null);
        return mEmptyView;
    }
}