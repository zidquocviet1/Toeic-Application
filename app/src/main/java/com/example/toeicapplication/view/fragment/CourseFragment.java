package com.example.toeicapplication.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toeicapplication.CommentActivity;
import com.example.toeicapplication.HomeActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.adapters.CourseAdapter;
import com.example.toeicapplication.databinding.FragmentCourseBinding;
import com.example.toeicapplication.model.Comment;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.viewmodels.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CourseFragment extends BaseFragment<HomeViewModel, FragmentCourseBinding> {
    private CourseAdapter courseAdapter;
    private HomeActivity context;

    private List<Course> courses;
    private List<Comment> comments = new ArrayList<>();
    private User user;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ENTRY = "entries";
    public static final String USER_KEY = "user";
    public static final String COURSE_KEY = "course";

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
        setupRecyclerView();
        setupObserve();
    }

    @Override
    public void onStart() {
        super.onStart();
        mVM.getAllComment();
    }

    private void setupObserve(){
        mVM.getCourses().observe(getViewLifecycleOwner(), courses -> {
            if (courses != null){
                this.courses.addAll(courses);
                courseAdapter.setData(courses);
            }
        });

        mVM.getCommentLiveData().observe(getViewLifecycleOwner(), comments -> {
            if (comments != null){
                this.comments = new ArrayList<>(comments);

                Map<Long, Integer> result = comments.stream()
                        .collect(Collectors
                                .toConcurrentMap(
                                        Comment::getCourseId, // return key
                                        comment -> 1, // return count value
                                        Integer::sum));

                result.entrySet().forEach(entry ->
                        courses.stream()
                        .filter(c -> c.getId().equals(entry.getKey()))
                        .findFirst()
                        .ifPresent(course1 -> {
                            int position = courses.indexOf(course1);
                            courseAdapter.notifyItemChanged(position, entry.getValue());
                        }));
            }
        });

        mVM.getLoginUserLiveData().observe(getViewLifecycleOwner(), user -> {
            this.user = user;
        });
    }

    private void setupRecyclerView(){
        courses = new ArrayList<>();

        courseAdapter = new CourseAdapter(context,
                courses,
                R.layout.info_course_fragment,
                CourseAdapter.Owner.COURSE_FRAGMENT);

        courseAdapter.setOnItemClickListener((object, position) -> {
            Course course = courses.get(position);
            ArrayList<Comment> entries = comments.stream()
                    .filter(c -> c.getCourseId().equals(course.getId()))
                    .collect(Collectors.toCollection(ArrayList::new));

            Intent intent = new Intent(context, CommentActivity.class);
            intent.putExtra(USER_KEY, user);
            intent.putExtra(COURSE_KEY, course);
            intent.putParcelableArrayListExtra(ENTRY, entries);
            startActivity(intent);
        });

        mBinding.rvCourseInfo.setAdapter(courseAdapter);
        mBinding.rvCourseInfo.setLayoutManager(new LinearLayoutManager(context,
                RecyclerView.VERTICAL, false));
        mBinding.rvCourseInfo.setHasFixedSize(true);
    }
}