package com.example.toeicapplication.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.view.fragment.OverviewFragment;
import com.example.toeicapplication.view.fragment.ReviewFragment;

import org.jetbrains.annotations.NotNull;

public class CourseDetailPagerAdapter extends FragmentStateAdapter {
    private final int NUM_PAGES;
    private final Course course;

    public CourseDetailPagerAdapter(FragmentManager fm, Lifecycle lifecycle, int NUM_PAGES, Course course) {
        super(fm, lifecycle);
        this.NUM_PAGES = NUM_PAGES;
        this.course = course;
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return OverviewFragment.newInstance(this.course.getDescription());
            case 1:
                return ReviewFragment.newInstance(this.course.getComment());
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
