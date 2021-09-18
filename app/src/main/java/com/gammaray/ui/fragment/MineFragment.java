package com.gammaray.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ClickUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gammaray.ui.activity.WalletLinksActivity;
import com.google.gson.Gson;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.upbest.arouter.Extras;
import com.gammaray.R;
import com.gammaray.adapter.MineActionAdapter;
import com.gammaray.base.BaseFragment;
import com.gammaray.base.YunApplication;
import com.gammaray.constant.AppConstant;
import com.gammaray.databinding.FragmentMineBinding;
import com.gammaray.entity.AccountVo;
import com.gammaray.entity.AuctionArtVo;
import com.gammaray.entity.BaseResponseVo;
import com.gammaray.entity.StdoutLogger;
import com.gammaray.entity.UserVo;
import com.gammaray.net.MinerCallback;
import com.gammaray.net.RequestManager;
import com.gammaray.ui.activity.AuctionRecordsActivity;
import com.gammaray.ui.activity.CashAccountActivity;
import com.gammaray.ui.activity.ExchangeNFTActivity;
import com.gammaray.ui.activity.order.SellAndBuyActivity;
import com.gammaray.ui.activity.user.FollowAndFansActivity;
import com.gammaray.ui.activity.user.MessagesActivity;
import com.gammaray.ui.activity.user.MyCollectActivity;
import com.gammaray.ui.activity.user.MyHomePageActivity;
import com.gammaray.ui.activity.user.SettingsActivity;
import com.gammaray.ui.activity.user.UploadArtActivity;
import com.gammaray.ui.activity.wallet.AcountActivity;
import com.gammaray.utils.SharedPreUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;

import jp.co.soramitsu.fearless_utils.wsrpc.SocketService;
import retrofit2.Call;
import retrofit2.Response;

public class
MineFragment extends BaseFragment<FragmentMineBinding> implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    Button button;
    RecyclerView actionList;
    MineActionAdapter mineActionAdapter;
    public static final int MINE_PAGE = 0;
    public static final int UPGRADE_ARTS = 1;
    public static final int BUY_IN = 2;
    public static final int SELL_OUT = 3;
    public static final int AUCTIONS = 4;
    public static final int COLLECT_ARTS = 5;
    public static final int ABOUT_US = 6;
    public static final int NEWS = 7;
    public static final int CASH_ACCOUNT = 8;

    SocketService socketService;
    long lastClickTime = 0;
    private long time_space = 1000 * 1;
    private String accountRemain = "0";

    private boolean hasWinNft = false;
    private int page_index;

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
        View[] views = {mBinding.setting, mBinding.fans, mBinding.follow, mBinding.layoutMainWallet, mBinding.layoutSubWallet, mBinding.mineTitleImg};
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

            case R.id.layout_main_wallet:
                startActivity(AcountActivity.class);

                break;
            case R.id.layout_sub_wallet:
                startActivity(WalletLinksActivity.class);
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
            case AUCTIONS:
                Intent intent = new Intent(requireActivity(), AuctionRecordsActivity.class);
                intent.putExtra("page_index", page_index);
                startActivity(intent);
                break;
            case MINE_PAGE:
                startActivity(MyHomePageActivity.class);
                break;
            case COLLECT_ARTS:
                startActivity(MyCollectActivity.class);
                break;
            case UPGRADE_ARTS:
                startActivity(UploadArtActivity.class);
                break;
            case ABOUT_US:
                if (YunApplication.getmUserVo() != null)
                    if (!TextUtils.isEmpty(YunApplication.getmUserVo().getPhone_number()))
                        startActivity(ExchangeNFTActivity.class);
                    else {
                        ToastUtils.showShort("请先绑定手机号");
                    }
                break;
            case CASH_ACCOUNT:
                Intent cashIntent = new Intent(requireContext(), CashAccountActivity.class);
                cashIntent.putExtra("account_remain", accountRemain);
                startActivity(cashIntent);
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
                    queryAccountInfo();
                    queryWinAuctions();
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

    private void queryAccountInfo() {
        RequestManager.instance().queryAccount(new MinerCallback<BaseResponseVo<List<AccountVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AccountVo>>> call, Response<BaseResponseVo<List<AccountVo>>> response) {
                if (response.isSuccessful()) {
                    List<AccountVo> accounts = response.body().getBody();
                    if (accounts != null && accounts.size() > 0) {
                        for (int i = 0; i < accounts.size(); i++) {
                            if (accounts.get(i).getCurrency_code().equals("rmb")) {
                                accountRemain = accounts.get(i).getBalance();
                                return;
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<AccountVo>>> call, Response<BaseResponseVo<List<AccountVo>>> response) {

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
//            mBinding.mineCount.setText(getString(R.string.mine_acount, s));
        }
    }

    private void queryWinAuctions() {
        RequestManager.instance().queryWinAuctions(1, 20, new MinerCallback<BaseResponseVo<List<AuctionArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<AuctionArtVo> list = response.body().getBody();
                        if (list != null && list.size() > 0) {
                            hasWinNft = true;
                            page_index = 2;
                        } else {
                            hasWinNft = false;
                            page_index = 0;
                        }
                        mineActionAdapter.setWinTag(hasWinNft);
                    }
                }
            }

            @Override
            public void onError(Call<BaseResponseVo<List<AuctionArtVo>>> call, Response<BaseResponseVo<List<AuctionArtVo>>> response) {
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
            }
        });
    }
}