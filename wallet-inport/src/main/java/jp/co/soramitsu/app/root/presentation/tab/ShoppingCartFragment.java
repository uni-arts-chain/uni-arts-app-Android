package jp.co.soramitsu.app.root.presentation.tab;


import jp.co.soramitsu.inport.R;

public class ShoppingCartFragment extends BaseFragment {

    public static BaseFragment newInstance() {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_shopping_cart;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {

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