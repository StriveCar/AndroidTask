package com.example.androidtask.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private List<Fragment> fragmentlist = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Fragment> fragmentlist) {
        super(fragmentManager, lifecycle);
        this.fragmentlist = fragmentlist;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentlist.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentlist.size();
    }
}
