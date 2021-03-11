package com.yunhualian.ui.fragment;

import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yunhualian.R;
import com.yunhualian.base.BaseFragment;

public class MainTabFragment extends BaseFragment {

    private BottomNavigationView mBottomNavigationView;
    private FrameLayout container;
    private CoordinatorLayout main_content;
    private HomeFragment homeFragment;
    private PictureSortFragment pictureSortFragment;
    private ShoppingCartFragment shoppingCartFragment;
    private MineFragment mineFragment;
    private CreatorFragment creatorFragment;
    private boolean mIsback;
    private Fragment mCurrentFragment;
    private int mCurrentItemId;

    public static BaseFragment newInstance() {
        MainTabFragment fragment = new MainTabFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main_tab;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        homeFragment = (HomeFragment) getChildFragmentManager().findFragmentByTag("homeFragment");
        pictureSortFragment = (PictureSortFragment) getChildFragmentManager().findFragmentByTag("pictureSortFragment");
        creatorFragment = (CreatorFragment) getChildFragmentManager().findFragmentByTag("creatorFragment");
        shoppingCartFragment = (ShoppingCartFragment) getChildFragmentManager().findFragmentByTag("shoppingCartFragment");
        mineFragment = (MineFragment) getChildFragmentManager().findFragmentByTag("mineFragment");
        if (null == mCurrentFragment) {
            switch (mCurrentItemId) {
                case R.id.navigation_home:
                    mCurrentFragment = homeFragment;
                    break;
                case R.id.navigation_art_sort:
                    mCurrentFragment = pictureSortFragment;
                    break;
                case R.id.navigation_creator:
                    mCurrentFragment = creatorFragment;
                    break;
                case R.id.navigation_shop_cart:
                    mCurrentFragment = shoppingCartFragment;
                    break;
                case R.id.navigation_mine:
                    mCurrentFragment = mineFragment;
                    break;
            }
        }


        container = (FrameLayout) rootView.findViewById(R.id.container);
        main_content = (CoordinatorLayout) rootView.findViewById(R.id.main_content);
        mBottomNavigationView = (BottomNavigationView) rootView.findViewById(R.id.mBottomNavigationView);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (null == getChildFragmentManager()) {
                    return false;
                }

                if (mCurrentItemId == menuItem.getItemId()) {
                    return false;
                }
                mCurrentItemId = menuItem.getItemId();
                FragmentTransaction mFragmentTransaction = getChildFragmentManager().beginTransaction();
                if (null != mCurrentFragment) {
                    mFragmentTransaction.hide(mCurrentFragment);
                }
                switch (mCurrentItemId) {
                    case R.id.navigation_home:
                        if (null == homeFragment) {
                            if (null != getChildFragmentManager().findFragmentByTag("homeFragment")) {
                                homeFragment = (HomeFragment) getChildFragmentManager().findFragmentByTag("homeFragment");
                            } else {
                                homeFragment = (HomeFragment) HomeFragment.newInstance();
                                mFragmentTransaction.add(R.id.container, homeFragment, "homeFragment");
                            }
                        }
                        mCurrentFragment = homeFragment;
                        break;
                    case R.id.navigation_art_sort:
                        if (null == pictureSortFragment) {
                            if (null != getChildFragmentManager().findFragmentByTag("pictureSortFragment")) {
                                pictureSortFragment = (PictureSortFragment) getChildFragmentManager().findFragmentByTag("pictureSortFragment");
                            } else {
                                pictureSortFragment = (PictureSortFragment) PictureSortFragment.newInstance();
                                mFragmentTransaction.add(R.id.container, pictureSortFragment, "pictureSortFragment");
                            }
                        }
                        mCurrentFragment = pictureSortFragment;
                        break;
                    case R.id.navigation_creator:
                        if (null == creatorFragment) {
                            if (null != getChildFragmentManager().findFragmentByTag("creatorFragment")) {
                                creatorFragment = (CreatorFragment) getChildFragmentManager().findFragmentByTag("creatorFragment");
                            } else {
                                creatorFragment = (CreatorFragment) CreatorFragment.newInstance();
                                mFragmentTransaction.add(R.id.container, creatorFragment, "creatorFragment");
                            }
                        }
                        mCurrentFragment = creatorFragment;


                        break;
                    case R.id.navigation_shop_cart:
                        if (null == shoppingCartFragment) {
                            if (null != getChildFragmentManager().findFragmentByTag("shoppingCartFragment")) {
                                shoppingCartFragment = (ShoppingCartFragment) getChildFragmentManager().findFragmentByTag("shoppingCartFragment");
                            } else {
                                shoppingCartFragment = (ShoppingCartFragment) ShoppingCartFragment.newInstance();
                                mFragmentTransaction.add(R.id.container, shoppingCartFragment, "shoppingCartFragment");
                            }
                        }
                        mCurrentFragment = shoppingCartFragment;
                        break;
                    case R.id.navigation_mine:
                        if (null == mineFragment) {
                            if (null != getChildFragmentManager().findFragmentByTag("mineFragment")) {
                                mineFragment = (MineFragment) getChildFragmentManager().findFragmentByTag("mineFragment");
                            } else {
                                mineFragment = (MineFragment) MineFragment.newInstance();
                                mFragmentTransaction.add(R.id.container, mineFragment, "mineFragment");
                            }
                        }
                        mCurrentFragment = mineFragment;
                        break;
                }
//                setAndroidNativeLightStatusBar(mCurrentItemId != R.id.navigation_mine, false, true);
                if (null != mCurrentFragment) {
                    mFragmentTransaction.show(mCurrentFragment);
                }
                mFragmentTransaction.commitAllowingStateLoss();
                return true;
            }
        });

        mBottomNavigationView.setSelectedItemId(0 != mCurrentItemId ? mCurrentItemId : R.id.navigation_home);
        mBottomNavigationView.setItemIconTintList(null);
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}