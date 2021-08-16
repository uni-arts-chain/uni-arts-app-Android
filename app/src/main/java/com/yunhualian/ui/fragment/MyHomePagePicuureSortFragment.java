package com.yunhualian.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.yunhualian.R;
import com.yunhualian.adapter.MyHomePageArtAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.constant.ExtraConstant;
import com.yunhualian.databinding.FragmentMyPagePictureSortBinding;
import com.yunhualian.entity.BaseResponseVo;
import com.yunhualian.entity.EventBusMessageEvent;
import com.yunhualian.entity.SellingArtVo;
import com.yunhualian.net.MinerCallback;
import com.yunhualian.net.RequestManager;
import com.yunhualian.ui.activity.CustomerServiceActivity;
import com.yunhualian.ui.activity.GoAuctionActivity;
import com.yunhualian.ui.activity.TransferActivity;
import com.yunhualian.ui.activity.art.ArtDetailActivity;
import com.yunhualian.ui.activity.user.SellArtActivity;
import com.yunhualian.ui.activity.user.SellArtUnCutActivity;
import com.yunhualian.widget.UploadSuccessPopUpWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MyHomePagePicuureSortFragment extends BaseFragment<FragmentMyPagePictureSortBinding> {
    //    MyPicturesAdapter picturesAdapter;
    public static String STATE_ONLINE = "online";
    public static String STATE_AUCTION = "bidding";
    public static String STATE = "state";
    public static int CUT_MODE = 3;
    MyHomePageArtAdapter picturesAdapter;

    UploadSuccessPopUpWindow uploadSuccessPopUpWindow;
    List<SellingArtVo> artVoList;
    private String state;
    private boolean hasRefresh = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            queryArts();
            hasRefresh = true;
        }
    };

    public static BaseFragment newInstance(String state) {
        MyHomePagePicuureSortFragment fragment = new MyHomePagePicuureSortFragment();
        Bundle args = new Bundle();
        args.putString(STATE, state);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_my_page_picture_sort;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        state = getArguments().getString(STATE);
        picturesAdapter = new MyHomePageArtAdapter(artVoList);
        StaggeredGridLayoutManager sortLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mBinding.pictures.setLayoutManager(sortLayoutManager);
        picturesAdapter.setEmptyView(R.layout.layout_entrust_empty_for_homepage, mBinding.pictures);
        mBinding.pictures.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                sortLayoutManager.invalidateSpanAssignments();
            }
        });
        mBinding.pictures.setAdapter(picturesAdapter);
        picturesAdapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ArtDetailActivity.ART_KEY, artVoList.get(position));
            bundle.putInt(ArtDetailActivity.ART_ID, artVoList.get(position).getId());
            startActivity(ArtDetailActivity.class, bundle);
        });
        picturesAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            //去出售
            SellingArtVo sellingArtVo = null;
            if (artVoList.size() > 0) {
                sellingArtVo = artVoList.get(position);
            }
            if (view.getId() == R.id.sellAction) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(SellArtActivity.ARTINFO, sellingArtVo);
                bundle.putBoolean("is_from_detail", false);
                if (sellingArtVo.getCollection_mode() == CUT_MODE) {
                    startActivity(SellArtActivity.class, bundle);
                } else {
                    startActivity(SellArtUnCutActivity.class, bundle);
                }
            } else if (view.getId() == R.id.transferAction) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(SellArtActivity.ARTINFO, sellingArtVo);
                startActivity(TransferActivity.class, bundle);
            } else if (view.getId() == R.id.auctionAction) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(SellArtActivity.ARTINFO, sellingArtVo);
                startActivity(GoAuctionActivity.class, bundle);
            }
        });
        mBinding.swipeRefresh.setOnRefreshListener(this::queryArts);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(EventBusMessageEvent mEventBusMessageEvent) {
        if (null != mEventBusMessageEvent && !TextUtils.isEmpty(mEventBusMessageEvent.getmMessage())) {
            if (TextUtils.equals(ExtraConstant.EVENT_SELL_SUCCESS, mEventBusMessageEvent.getmMessage())) {
                showPopWindow();
            }
        }
    }

    private void showPopWindow() {
        if (mActivity != null)
            uploadSuccessPopUpWindow = new UploadSuccessPopUpWindow(mActivity, new UploadSuccessPopUpWindow.OnBottomTextviewClickListener() {
                @Override
                public void onCancleClick() {
                    uploadSuccessPopUpWindow.dismiss();
                }

                @Override
                public void onPerformClick() {
                    uploadSuccessPopUpWindow.dismiss();
                    startActivity(CustomerServiceActivity.class);
                }
            });
        uploadSuccessPopUpWindow.setConfirmText(getString(R.string.text_call_service));
        uploadSuccessPopUpWindow.setContent(getString(R.string.text_sell_tips));
        uploadSuccessPopUpWindow.showAtLocation(mBinding.swipeRefresh, Gravity.CENTER, 0, 0);
        EventBus.getDefault().removeAllStickyEvents();
    }

    @Override
    public void onResume() {
        super.onResume();
        queryArts();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }


    private void queryArts() {
        if (mBinding.swipeRefresh.isRefreshing()) {
            mBinding.swipeRefresh.setRefreshing(false);
        }
        showLoading("");
        HashMap<String, String> param = new HashMap<>();
        param.put("aasm_state", state);
        param.put("page", "1");
        param.put("per_page", "100");
        RequestManager.instance().queryMine(param, new MinerCallback<BaseResponseVo<List<SellingArtVo>>>() {
            @Override
            public void onSuccess(Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
                if (response.isSuccessful()) {
                    if (artVoList != null && artVoList.size() > 0)
                        artVoList.clear();
                    artVoList = response.body().getBody();
                    if (artVoList.size() > 0) {
                        picturesAdapter.setNewData(artVoList);
                        dismissLoading();
                    }
                }
            }

            @Override
            public void onError
                    (Call<BaseResponseVo<List<SellingArtVo>>> call, Response<BaseResponseVo<List<SellingArtVo>>> response) {
                dismissLoading();
            }

            @Override
            public void onFailure(Call<?> call, Throwable t) {
                dismissLoading();
            }
        });
    }
}