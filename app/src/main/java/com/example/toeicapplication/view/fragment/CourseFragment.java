package com.example.toeicapplication.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toeicapplication.HomeActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.adapters.CourseAdapter;
import com.example.toeicapplication.databinding.FragmentCourseBinding;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.viewmodels.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseFragment extends BaseFragment<HomeViewModel, FragmentCourseBinding> {
    private CourseAdapter courseAdapter;
    private HomeActivity context;

    private List<Course> courses;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CourseFragment() {
        // Required empty public constructor
    }

    public static CourseFragment newInstance(String param1, String param2) {
        CourseFragment fragment = new CourseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public FragmentCourseBinding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return FragmentCourseBinding.inflate(inflater, container, attachToParent);
    }

    @Override
    public Class<HomeViewModel> getViewModel() {
        return HomeViewModel.class;
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return requireActivity();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupObserve();
        setupRecyclerView();
    }

    private void setupObserve(){
        mVM.getCourses().observe(getViewLifecycleOwner(), courses -> {
            if (courses != null){
                courseAdapter.setData(courses);
            }
        });
    }

    private void setupRecyclerView(){
        courses = new ArrayList<>();

        courseAdapter = new CourseAdapter(context,
                courses,
                R.layout.info_course_fragment,
                CourseAdapter.Owner.COURSE_FRAGMENT);

        mBinding.rvCourseInfo.setAdapter(courseAdapter);
        mBinding.rvCourseInfo.setLayoutManager(new LinearLayoutManager(context,
                RecyclerView.VERTICAL, false));
        mBinding.rvCourseInfo.setHasFixedSize(true);
    }
}