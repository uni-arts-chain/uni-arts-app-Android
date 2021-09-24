package jp.co.soramitsu.feature_wallet_impl.presentation.balance;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class MyHomePageAdapter extends FragmentPagerAdapter {
    private final int PAGE_COUNT;
    private List<String> list;
    private Context context;
    private TabPagerListener listener;

    public interface TabPagerListener {
        Fragment getFragment(int position);
    }

    public MyHomePageAdapter(FragmentManager fm, int count, List<String> list, Context context) {
        super(fm);
        if (list == null || list.isEmpty()) {
            throw new ExceptionInInitializerError("list can't be null or empty");
        }
        if (count <= 0) {
            throw new ExceptionInInitializerError("count value error");
        }
        this.PAGE_COUNT = count;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return listener.getFragment(position);
    }

    public void setListener(TabPagerListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}
