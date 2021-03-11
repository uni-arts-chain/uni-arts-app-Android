package jp.co.soramitsu.app.root.presentation.tab;

import androidx.recyclerview.widget.LinearLayoutManager;


import java.util.Arrays;
import java.util.List;

import jp.co.soramitsu.inport.R;
import jp.co.soramitsu.inport.databinding.FragmentCreatorKBinding;

public class CreatorFragment extends BaseFragment<FragmentCreatorKBinding> {

    CreatorParentAdapter creatorParentAdapter;
    List<String> lists;

    public static BaseFragment newInstance() {
        CreatorFragment fragment = new CreatorFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_creator_k;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    protected void initView() {
        lists = Arrays.asList(getResources().getStringArray(R.array.popular));
        creatorParentAdapter = new CreatorParentAdapter(lists, getContext());
        LinearLayoutManager sortLayoutManager = new LinearLayoutManager(getContext());
        mBinding.artistList.setLayoutManager(sortLayoutManager);
        mBinding.artistList.setAdapter(creatorParentAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}