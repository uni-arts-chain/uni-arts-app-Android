package com.yunhualian.ui.fragment;

import android.Manifest;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.client.android.CaptureActivity;
import com.yunhualian.R;
import com.yunhualian.adapter.HomePagePopularAdapter;
import com.yunhualian.adapter.HomePageThemeAdapter;
import com.yunhualian.base.BaseFragment;
import com.yunhualian.base.YunApplication;
import com.yunhualian.constant.AppConstant;
import com.yunhualian.databinding.FragmentHomeBinding;
import com.yunhualian.utils.ToolbarHelper2;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.Arrays;
import java.util.List;

public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    private HomePagePopularAdapter popularAdapter;
    private HomePageThemeAdapter themeAdapter;
    private List<String> sortList;
    private MZBannerView mzBannerView;

    public static BaseFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void initPresenter() {

    }

    private void setViewFlipperItem() {
        for (int i = 0; i < sortList.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_flipper, null);
            TextView tv_notice = view.findViewById(R.id.tv_notice);
            TextView tv_notice2 = view.findViewById(R.id.tv_notice2);
//            NoticeVo noticeVo = sortList.get(i);
            tv_notice.setText(sortList.get(i));
            tv_notice2.setText(sortList.get(i));
            mBinding.noticeFlipper.addView(view);

//            view.setOnClickListener(v -> {
//                if (noticeVo == null || TextUtils.isEmpty(noticeVo.getNotice_url()))
//                    return;

//                ARouter.getInstance().build(Constants.ARouterUriConst.RFINEXWEBVIEW)
//                        .withString("title", "")
//                        .withString("url", noticeVo.getNotice_url())
//                        .navigation();
//        });
        }

    }

    @Override
    protected void initView() {
        initRefresh();
        sortList = Arrays.asList(getResources().getStringArray(R.array.popular));

        popularAdapter = new HomePagePopularAdapter(sortList);
        themeAdapter = new HomePageThemeAdapter(sortList, YunApplication.getInstance());
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mBinding.hotRecycle.setLayoutManager(layoutManager);
        mBinding.hotRecycle.setAdapter(popularAdapter);
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(YunApplication.getInstance());
        mBinding.theme.setLayoutManager(sortLayoutManager);
        mBinding.theme.setAdapter(themeAdapter);
        setViewFlipperItem();
        mBinding.noticeFlipper.startFlipping();
        mBinding.banner.setPages(Arrays.asList(R.mipmap.shanshui, R.mipmap.shanshui, R.mipmap.shanshui), new MZHolderCreator() {
            @Override
            public MZViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        mBinding.banner.pause();
    }

    private void initRefresh() {
        mBinding.srlShoopingMall.setColorSchemeResources(R.color.colorAccent);
        mBinding.srlShoopingMall.setDistanceToTriggerSync(500);
        mBinding.srlShoopingMall.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBinding.srlShoopingMall.setRefreshing(false);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.banner.start();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void openCamra() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // android 6.0以上需要动态申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 访问手机存储申请权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // android 6.0以上需要动态申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, AppConstant.REQ_PERM_CAMERA);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(getContext(), CaptureActivity.class);
        startActivityForResult(intent, AppConstant.REQ_QR_CODE);

    }

    /**
     * 重载扫描页activity返回函数（接受扫描码）
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstant.REQ_QR_CODE || requestCode == AppConstant.REQUEST_CODE_SCAN_GALLERY) {
                //扫描二维码或者扫描相册成功
                Bundle bundle = data.getExtras();
                ToastUtils.showShort("扫描成功");
                //将扫描出的信息显示出来
                //textView.setText(scanResult);
//                DialogManager.showConfirmDialog("温馨提示",scanResult);
            }
        } else {
            ToastUtils.showShort("取消扫描");
        }
    }

    public static class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            mImageView.setImageResource(data);
        }
    }
}