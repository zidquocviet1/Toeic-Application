package com.example.toeicapplication.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.toeicapplication.databinding.TabOverviewFragmentBinding;

import org.jetbrains.annotations.NotNull;

public class OverviewFragment extends Fragment {
    private TabOverviewFragmentBinding binding;
    private String description;

    // require no arguments constructor
    public OverviewFragment(){}

    public static OverviewFragment newInstance(String description){
        OverviewFragment overviewFragment = new OverviewFragment();

        Bundle args = new Bundle();
        args.putString("course_description", description);

        overviewFragment.setArguments(args);
        return overviewFragment;
    }

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            description = getArguments().getString("course_description");
        }
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        binding = TabOverviewFragmentBinding.inflate(inflater, container, false);

        if (!description.equals("")){
            binding.txtDes.setText(description);
        }
        return binding.getRoot();
    }
}
