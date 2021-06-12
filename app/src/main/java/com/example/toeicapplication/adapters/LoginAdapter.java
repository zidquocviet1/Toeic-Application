package com.example.toeicapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.toeicapplication.view.fragment.LoginFragment;
import com.example.toeicapplication.view.fragment.SignUpFragment;

import org.jetbrains.annotations.NotNull;

public class LoginAdapter extends FragmentStateAdapter {
    private final int NUM_PAGES;

    public LoginAdapter(FragmentManager fm, Lifecycle lifecycle, int NUM_PAGES){
        super(fm, lifecycle);
        this.NUM_PAGES = NUM_PAGES;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new LoginFragment();
            case 1:
                return new SignUpFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
