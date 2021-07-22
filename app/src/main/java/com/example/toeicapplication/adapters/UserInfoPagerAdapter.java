package com.example.toeicapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.toeicapplication.view.fragment.UserRecordFragment;

import org.jetbrains.annotations.NotNull;

public class UserInfoPagerAdapter extends FragmentStateAdapter {
    private final int NUM_PAGES;

    public UserInfoPagerAdapter(FragmentManager fm, Lifecycle lifecycle, int NUM_PAGES) {
        super(fm, lifecycle);
        this.NUM_PAGES = NUM_PAGES;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return new UserRecordFragment();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
