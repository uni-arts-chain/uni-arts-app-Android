package jp.co.soramitsu.app.root.presentation.tab;

import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import javax.inject.Inject;

import jp.co.soramitsu.app.root.di.RootApi;
import jp.co.soramitsu.common.di.FeatureUtils;
import jp.co.soramitsu.inport.R;

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
    @Inject
    MainTabViewModel mainTabViewModel;

    public static MainTabFragment mainTabFragment;

    public static BaseFragment newInstance() {
        MainTabFragment fragment = new MainTabFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    public static MainTabFragment getInstanse() {
        return mainTabFragment;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main_tab;
    }

    @Override
    public void initPresenter() {

    }

    public void goMainPage() {
        mainTabViewModel.goMain();
    }

    @Override
    protected void initView() {
        mainTabFragment = this;
        MainTabInjetKt.inject(getContext(), this);
        if (mainTabViewModel != null) {

        }

        homeFragment = (HomeFragment) getChildFragmentManager().findFragmentByTag("homeFragment");
        pictureSortFragment = (PictureSortFragment) getChildFragmentManager().findFragmentByTag("pictureSortFragment");
        creatorFragment = (CreatorFragment) getChildFragmentManager().findFragmentByTag("creatorFragment");
        shoppingCartFragment = (ShoppingCartFragment) getChildFragmentManager().findFragmentByTag("shoppingCartFragment");
        mineFragment = (MineFragment) getChildFragmentManager().findFragmentByTag("mineFragment");
        if (null == mCurrentFragment) {
            if (mCurrentItemId == R.id.navigation_home) {
                mCurrentFragment = homeFragment;
            } else if (mCurrentItemId == R.id.navigation_art_sort) {
                mCurrentFragment = pictureSortFragment;
            } else if (mCurrentItemId == R.id.navigation_creator) {
                mCurrentFragment = creatorFragment;
            } else if (mCurrentItemId == R.id.navigation_shop_cart) {
                mCurrentFragment = shoppingCartFragment;
            } else if (mCurrentItemId == R.id.navigation_mine) {
                mCurrentFragment = mineFragment;
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
                if (mCurrentItemId == R.id.navigation_home) {
                    if (null == homeFragment) {
                        if (null != getChildFragmentManager().findFragmentByTag("homeFragment")) {
                            homeFragment = (HomeFragment) getChildFragmentManager().findFragmentByTag("homeFragment");
                        } else {
                            homeFragment = (HomeFragment) HomeFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, homeFragment, "homeFragment");
                        }
                    }
                    mCurrentFragment = homeFragment;
                } else if (mCurrentItemId == R.id.navigation_art_sort) {
                    if (null == pictureSortFragment) {
                        if (null != getChildFragmentManager().findFragmentByTag("pictureSortFragment")) {
                            pictureSortFragment = (PictureSortFragment) getChildFragmentManager().findFragmentByTag("pictureSortFragment");
                        } else {
                            pictureSortFragment = (PictureSortFragment) PictureSortFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, pictureSortFragment, "pictureSortFragment");
                        }
                    }
                    mCurrentFragment = pictureSortFragment;
                } else if (mCurrentItemId == R.id.navigation_creator) {
                    if (null == creatorFragment) {
                        if (null != getChildFragmentManager().findFragmentByTag("creatorFragment")) {
                            creatorFragment = (CreatorFragment) getChildFragmentManager().findFragmentByTag("creatorFragment");
                        } else {
                            creatorFragment = (CreatorFragment) CreatorFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, creatorFragment, "creatorFragment");
                        }
                    }
                    mCurrentFragment = creatorFragment;
                } else if (mCurrentItemId == R.id.navigation_shop_cart) {
                    if (null == shoppingCartFragment) {
                        if (null != getChildFragmentManager().findFragmentByTag("shoppingCartFragment")) {
                            shoppingCartFragment = (ShoppingCartFragment) getChildFragmentManager().findFragmentByTag("shoppingCartFragment");
                        } else {
                            shoppingCartFragment = (ShoppingCartFragment) ShoppingCartFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, shoppingCartFragment, "shoppingCartFragment");
                        }
                    }
                    mCurrentFragment = shoppingCartFragment;
                } else if (mCurrentItemId == R.id.navigation_mine) {
                    if (null == mineFragment) {
                        if (null != getChildFragmentManager().findFragmentByTag("mineFragment")) {
                            mineFragment = (MineFragment) getChildFragmentManager().findFragmentByTag("mineFragment");
                        } else {
                            mineFragment = (MineFragment) MineFragment.newInstance();
                            mFragmentTransaction.add(R.id.container, mineFragment, "mineFragment");
                        }
                    }
                    mCurrentFragment = mineFragment;
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