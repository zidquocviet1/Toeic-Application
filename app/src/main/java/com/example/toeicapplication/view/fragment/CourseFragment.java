package com.example.toeicapplication.view.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class CourseFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentCourseBinding binding;
    private List<Course> courses;
    private CourseAdapter courseAdapter;
    private HomeActivity context;
    private HomeViewModel homeVM;

    public CourseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCourseBinding.inflate(inflater, container, false);

        context = (HomeActivity) getActivity();

        setupObserve();
        setupRecyclerView();

        return binding.getRoot();
    }

    private void setupObserve(){
        homeVM = new ViewModelProvider(context).get(HomeViewModel.class);

        homeVM.getCourses().observe(getViewLifecycleOwner(), courses -> {
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

        binding.rvCourseInfo.setAdapter(courseAdapter);
        binding.rvCourseInfo.setLayoutManager(new LinearLayoutManager(context,
                RecyclerView.VERTICAL, false));
        binding.rvCourseInfo.setHasFixedSize(true);
    }
}