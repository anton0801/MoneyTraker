package priler.com.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import priler.com.R;
import priler.com.fragments.BalanceFragment;
import priler.com.fragments.ItemsFragment;
import priler.com.models.Item;

public class MainPagesAdapter extends FragmentPagerAdapter {

    public static final int PAGE_INCOMES = 0;
    public static final int PAGE_EXPENSES = 1;
    public static final int PAGE_BALANCE = 2;

    private String[] titles;

    public MainPagesAdapter(@NonNull FragmentManager fm, Context context) { // FragmentManager нужен чтоб добовлять или удалять фрагменты (взаимодействовать)
        super(fm);
        titles = context.getResources().getStringArray(R.array.tab_title);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case PAGE_INCOMES:
                return ItemsFragment.createItemsFragment(Item.TYPE_INCOMES);
            case PAGE_EXPENSES:
                return ItemsFragment.createItemsFragment(Item.TYPE_EXPENSES);
            case PAGE_BALANCE:
                return new BalanceFragment();
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return 3;
    }

}
