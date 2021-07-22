package com.example.toeicapplication.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toeicapplication.adapters.RecordExpandableListAdapter;
import com.example.toeicapplication.databinding.FragmentUserRecordBinding;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.Result;
import com.example.toeicapplication.viewmodels.UserInfoViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class UserRecordFragment extends BaseFragment<UserInfoViewModel, FragmentUserRecordBinding> {
    private List<Course> courses;
    private List<Result> results;

    public UserRecordFragment() {
        // Required empty public constructor
    }

    @Override
    public FragmentUserRecordBinding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return FragmentUserRecordBinding.inflate(inflater, container, false);
    }

    @Override
    public Class<UserInfoViewModel> getViewModel() {
        return UserInfoViewModel.class;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return requireActivity();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObserver();
//        if (courses != null && !courses.isEmpty()){
//            Map<Course, List<Result>> listDetail = new HashMap<>();
//
//            courses.forEach(c -> {
//                if (c != null) listDetail.put(c, null);
//            });
//
//            RecordExpandableListAdapter adapter = new RecordExpandableListAdapter(courses, listDetail, getActivity());
//            mBinding.expandableListView.setAdapter(adapter);
//        }
    }

    private void setupObserver(){
        if (mVM != null){
            mVM.getCourseListLiveData().observe(getViewLifecycleOwner(), courses ->{
                if (courses != null && !courses.isEmpty()){
                    this.courses = new ArrayList<>(courses);

                    Map<Course, List<Result>> listDetail = new HashMap<>();

                    courses.forEach(c -> {
                        if (c != null) listDetail.put(c, null);
                    });

                    RecordExpandableListAdapter adapter = new RecordExpandableListAdapter(courses, listDetail, getActivity());
                    mBinding.expandableListView.setAdapter(adapter);
                }
            });

            mVM.getResultListLiveData().observe(getViewLifecycleOwner(), results -> {
                if (results != null && !results.isEmpty()){
                    this.results = new ArrayList<>(results);
                }
            });
        }
    }
}