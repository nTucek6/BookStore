package com.example.bookstore.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerHomeAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragmentTitle = new ArrayList<>();

    public ViewPagerHomeAdapter(@NonNull FragmentActivity fragmentManager) {
        super(fragmentManager);
    }

    public void add (Fragment fragment,String title)
    {
        fragmentList.add(fragment);
        fragmentTitle.add(title);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }

    public CharSequence getPageTitle(int position)
    {
        return fragmentTitle.get(position);
    }

}
