package com.yunhualian.ui.activity.user;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yunhualian.R;
import com.yunhualian.adapter.MessagesAdapter;
import com.yunhualian.base.BaseActivity;
import com.yunhualian.base.ToolBarOptions;
import com.yunhualian.databinding.ActivityMessagesBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.MessagesVo;
import com.yunhualian.entity.NoticeVo;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.blindbox.BlindBoxDetailActivity;
import com.yunhualian.ui.activity.order.SellAndBuyActivity;
import com.yunhualian.ui.activity.user.MyHomePageActivity;
import com.yunhualian.utils.SharedPreUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MessagesActivity extends BaseActivity<ActivityMessagesBinding> {
    public final String ART = "art";
    public final String ARTTRADE = "arttrade";
    public final String BLINDBOX = "blindboxdrawgroup";
    MessagesAdapter messagesAdapter;
    List<MessagesVo> messagesVoList;
    int page = 1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_messages;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {
        messagesVoList = new ArrayList<>();
        ToolBarOptions mToolBarOptions = new ToolBarOptions();
        mToolBarOptions.titleId = R.string.message;
        mToolBarOptions.rightTextString = R.string.all_read_message;
        mDataBinding.mAppBarLayoutAv.mToolbar.findViewById(R.id.txt_right).setOnClickListener(v ->
        {
            if (messagesVoList.size() > BigDecimal.ZERO.intValue())
                allRead();
        });

        setToolBar(mDataBinding.mAppBarLayoutAv.mToolbar, mToolBarOptions);

        messagesAdapter = new MessagesAdapter(messagesVoList);
        mDataBinding.messageList.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter.setEmptyView(R.layout.layout_entrust_empty, mDataBinding.messageList);
        mDataBinding.messageList.setAdapter(messagesAdapter);
        messagesAdapter.setOnItemClickListener((adapter, view, position) -> {
            readMessage(messagesVoList.get(position).getId());

            if (messagesVoList.get(position).getResource_type().equalsIgnoreCase(ART)) {
                startActivity(MyHomePageActivity.class);
            } else if (messagesVoList.get(position).getResource_type().equalsIgnoreCase(ARTTRADE)) {
                Bundle sell = new Bundle();
                sell.putString("from", SellAndBuyActivity.SELL);
                startActivity(SellAndBuyActivity.class, sell);
            } else if (messagesVoList.get(position).getResource_type().equalsIgnoreCase(BLINDBOX)) {
                if (TextUtils.isEmpty(messagesVoList.get(position).getAction_str())) return;
                String[] parame = messagesVoList.get(position).getAction_str().split("#");
                if (parame.length < 2) return;
                Bundle bundle = new Bundle();
                bundle.putString("id", parame[1]);
                startActivity(BlindBoxDetailActivity.class, bundle);
            }
        });
        messagesAdapter.setOnLoadMoreListener(this::queryMessage, mDataBinding.messageList);
        mDataBinding.swipeRefresh.setOnRefreshListener(() -> {
            page = 1;
            queryMessage();
            mDataBinding.swipeRefresh.setRefreshing(false);
        });
    }

    private void allRead() {
        RequestManager.instance().readAllMessage(new MinerCallback<BaseResponseVo>() {
            @Override
            public void onSuccess(Call<BaseResponseVo> call, Response<BaseResponseVo> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    page = 1;
                    queryMessage();
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo> call, Response<BaseResponseVo> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        queryMessage();
    }

    private void queryMessage() {
        showLoading(getString(R.string.progress_loading));
        RequestManager.instance().queryNotices(page, new MinerCallback<BaseResponseVo<List<MessagesVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<MessagesVo>>> call, Response<BaseResponseVo<List<MessagesVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<MessagesVo> messageList = response.body().getBody();
                        if (messageList.size() > 0) {
                            if (page == 1) {
                                messagesVoList.clear();
                                messagesVoList = messageList;
                            } else {
                                messagesVoList.addAll(messageList);
                            }
                            page++;
                        }
                        if (messagesVoList.size() > 0) {
                            messagesAdapter.setNewData(messagesVoList);
                        }
                        if (page > 1)
                            messagesAdapter.loadMoreEnd();
                    }

                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<MessagesVo>>> call, Response<BaseResponseVo<List<MessagesVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

    private void readMessage(int id) {


        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));

        RequestManager.instance().readMessage(params, new MinerCallback<BaseResponseVo>() {
            @Override
            public void onSuccess(Call<BaseResponseVo> call, Response<BaseResponseVo> response) {
                dismissLoading();
                if (response.isSuccessful()) {
//                    queryMessage();

//                    startActivity(MyHomePageActivity.class);
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo> call, Response<BaseResponseVo> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }

}