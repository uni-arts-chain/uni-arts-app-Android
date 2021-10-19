package jp.co.soramitsu.feature_wallet_impl.presentation.balance.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import jp.co.soramitsu.feature_wallet_impl.R;

public class PersonalAssertFragment extends Fragment {

    private List<WalletTokenBean> walletTokenBeans = new ArrayList<>();

    private RecyclerView mRecyclerView;

    private View mView;

    private WalletTokenAdapter mAdapter;

    public static Fragment newInstance(List<WalletTokenBean> walletLinkBeans) {
        return new PersonalAssertFragment(walletLinkBeans);
    }


    public PersonalAssertFragment(List<WalletTokenBean> walletLinkBeans) {
        this.walletTokenBeans = walletLinkBeans;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_personal_assert_layout, container, false);
        initData();
        return mView;
    }

    private void initData() {
        mRecyclerView = mView.findViewById(R.id.rv_asserts);
        mAdapter = new WalletTokenAdapter(walletTokenBeans);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
