package com.yunhualian.ui.fragment;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.upbest.arouter.Extras;
import com.yunhualian.R;
import com.yunhualian.adapter.MineActionAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.databinding.FragmentMineBinding;
import com.yunhualian.entity.ArtBean;
import com.yunhualian.entity.ArtTypeVo;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.entity.StdoutLogger;
import com.yunhualian.entity.UserVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.ExchangeNFTActivity;
import com.yunhualian.ui.activity.video.VideoPlayerActivity;
import com.yunhualian.ui.activity.wallet.AcountActivity;
import com.yunhualian.ui.activity.CustomerServiceActivity;
import com.yunhualian.ui.activity.user.UploadArtActivity;
import com.yunhualian.ui.activity.user.FollowAndFansActivity;
import com.yunhualian.ui.activity.user.MessagesActivity;
import com.yunhualian.ui.activity.user.MyCollectActivity;
import com.yunhualian.ui.activity.user.MyHomePageActivity;
import com.yunhualian.ui.activity.order.SellAndBuyActivity;
import com.yunhualian.ui.activity.user.SettingsActivity;
import com.yunhualian.utils.SharedPreUtils;


import org.bouncycastle.math.ec.rfc8032.Ed25519;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import jp.co.soramitsu.fearless_utils.encrypt.EncryptionType;
import jp.co.soramitsu.fearless_utils.encrypt.json.JsonSeedEncoder;
import jp.co.soramitsu.fearless_utils.encrypt.model.Keypair;
import jp.co.soramitsu.fearless_utils.ss58.SS58Encoder;
import jp.co.soramitsu.fearless_utils.wsrpc.SocketService;
import retrofit2.Call;
import retrofit2.Response;

public class MineFragment extends BaseFragment<FragmentMineBinding> implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    Button button;
    RecyclerView actionList;
    MineActionAdapter mineActionAdapter;
    public static final int MINE_PAGE = 0;
    public static final int UPGRADE_ARTS = 1;
    public static final int BUY_IN = 2;
    public static final int SELL_OUT = 3;
    public static final int COLLECT_ARTS = 4;
    public static final int ABOUT_US = 5;
    public static final int NEWS = 6;
    public static final int SERVICE = 7;
    SocketService socketService;
    long lastClickTime = 0;
    private long time_space = 1000 * 1;

    public static BaseFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        initList();
        View[] views = {mBinding.setting, mBinding.fans, mBinding.follow, mBinding.walletLayout, mBinding.mineTitleImg};
        ClickUtils.applyGlobalDebouncing(views, this);
        socketService = new SocketService(new Gson(), new StdoutLogger(), new WebSocketFactory(), i -> 0);
        socketService.start(AppConstant.RPC);
    }

    public void initList() {
        List<String> namelist = Arrays.asList(getResources().getStringArray(R.array.mine_actions));
        mineActionAdapter = new MineActionAdapter(namelist);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
        mBinding.myActionList.setLayoutManager(layoutManager);
        mBinding.myActionList.setAdapter(mineActionAdapter);
        mineActionAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();
        getBalance();
    }

    @SuppressLint("CheckResult")
    private void initPageData() {
        UserVo userVo = YunApplication.getmUserVo();
        if (userVo == null) return;
        Glide.with(YunApplication.getInstance())
                .load(userVo.getAvatar().getUrl())
                .apply(new RequestOptions().placeholder(R.mipmap.icon_default_head)).into(mBinding.mineTitleImg);
        mBinding.follow.setText(
                getString(R.string.follow_num,
                        String.valueOf(userVo.getFollowing_user_size())));
        mBinding.fans.setText(getString(R.string.fans_num,
                String.valueOf(userVo.getFollow_user_size())));
        if (TextUtils.isEmpty(userVo.getDisplay_name()))
            mBinding.nickName.setText(R.string.set_display_name_tips);
        else
            mBinding.nickName.setText(userVo.getDisplay_name());
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setting:
                startActivity(SettingsActivity.class);
                break;
            case R.id.follow:
                Bundle bundle = new Bundle();
                bundle.putString("from", FollowAndFansActivity.FOLLOW);
                startActivity(FollowAndFansActivity.class, bundle);
                break;
            case R.id.fans:
                Bundle bundle2 = new Bundle();
                bundle2.putString("from", FollowAndFansActivity.FANS);
                startActivity(FollowAndFansActivity.class, bundle2);
                break;
            case R.id.walletLayout:
                startActivity(AcountActivity.class);
                break;
            case R.id.mine_title_img:
                startActivity(MyHomePageActivity.class);
                break;
        }
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        long currentTime = System.currentTimeMillis();
        if ((System.currentTimeMillis() - lastClickTime) < time_space) {
            return;
        }
        lastClickTime = currentTime;
        switch (position) {
            case BUY_IN:
                Bundle buy = new Bundle();
                buy.putString("from", SellAndBuyActivity.BUY);
                startActivity(SellAndBuyActivity.class, buy);
                break;
            case SELL_OUT:
                Bundle sell = new Bundle();
                sell.putString("from", SellAndBuyActivity.SELL);
                startActivity(SellAndBuyActivity.class, sell);
                break;
            case NEWS:
                startActivity(MessagesActivity.class);
                break;
            case SERVICE:
                startActivity(CustomerServiceActivity.class);
                break;
            case MINE_PAGE:
                startActivity(MyHomePageActivity.class);
                break;
            case COLLECT_ARTS:
                startActivity(MyCollectActivity.class);
                break;
            case UPGRADE_ARTS:
                startActivity(UploadArtActivity.class);
//                startActivity(VideoPlayerActivity.class);
                break;
            case ABOUT_US:
                if (YunApplication.getmUserVo() != null)
                    if (!TextUtils.isEmpty(YunApplication.getmUserVo().getPhone_number()))
                        startActivity(ExchangeNFTActivity.class);
                    else {
                        ToastUtils.showShort("请先绑定手机号");
                    }
                break;
        }
    }

    private void getUserInfo() {
        RequestManager.instance().queryUser(new MinerCallback<BaseResponseVo<UserVo>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {
                if (response.isSuccessful()) {
                    YunApplication.setmUserVo(response.body().getBody());
                    initPageData();
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<UserVo>> call, Response<BaseResponseVo<UserVo>> response) {

            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {

            }
        });
    }

    private void getBalance() {
        new getDesc().execute(socketService);
    }


    public class getDesc extends AsyncTask<SocketService, Integer, String> {
        @Override
        protected String doInBackground(SocketService... socketServices) {
            SendIntegrationTest sendIntegrationTest = new SendIntegrationTest();
            String balance = sendIntegrationTest.getBalance(socketServices[0], SharedPreUtils.getString(mActivity, SharedPreUtils.KEY_ADDRESS));
            String balanceStr;
            if (TextUtils.isEmpty(balance)) {
                balanceStr = "0";
            } else {
                balanceStr = new BigDecimal(balance).setScale(4, RoundingMode.DOWN).stripTrailingZeros().toPlainString();
            }
            return balanceStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Extras.balance = s;
            mBinding.mineCount.setText(getString(R.string.mine_acount, s));
        }
    }
}