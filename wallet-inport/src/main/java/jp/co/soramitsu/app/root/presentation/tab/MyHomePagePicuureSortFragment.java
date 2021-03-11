package jp.co.soramitsu.app.root.presentation.tab;

import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.Arrays;
import java.util.List;

import jp.co.soramitsu.inport.R;
import jp.co.soramitsu.inport.databinding.FragmentMyPagePictureSortBinding;

public class MyHomePagePicuureSortFragment extends BaseFragment<FragmentMyPagePictureSortBinding> {
    MyPicturesAdapter picturesAdapter;

    public static BaseFragment newInstance() {
        MyHomePagePicuureSortFragment fragment = new MyHomePagePicuureSortFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
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
        List<String> list = Arrays.asList(getResources().getStringArray(R.array.sorts));
        picturesAdapter = new MyPicturesAdapter(list);
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(getContext());
        mBinding.pictures.setLayoutManager(sortLayoutManager);
        mBinding.pictures.setAdapter(picturesAdapter);
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