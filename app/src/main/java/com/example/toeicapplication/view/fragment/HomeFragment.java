package com.example.toeicapplication.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.toeicapplication.CourseDetailActivity;
import com.example.toeicapplication.HomeActivity;
import com.example.toeicapplication.R;
import com.example.toeicapplication.adapters.CourseAdapter;
import com.example.toeicapplication.adapters.WordAdapter;
import com.example.toeicapplication.databinding.FragmentHomeBinding;
import com.example.toeicapplication.model.entity.Course;
import com.example.toeicapplication.model.entity.User;
import com.example.toeicapplication.model.entity.Word;
import com.example.toeicapplication.listeners.ItemClickListener;
import com.example.toeicapplication.utilities.AppConstants;
import com.example.toeicapplication.viewmodels.HomeViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends BaseFragment<HomeViewModel, FragmentHomeBinding>
        implements ItemClickListener, View.OnClickListener {
    private HomeActivity context;
    private CourseAdapter courseAdapter;
    private WordAdapter wordsAdapter;

    private List<Course> courses;
    private List<Word> words;

    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
    public FragmentHomeBinding bindingInflater(LayoutInflater inflater, ViewGroup container, boolean attachToParent) {
        return FragmentHomeBinding.inflate(inflater, container, attachToParent);
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = (HomeActivity) getActivity();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.btnViewAll.setOnClickListener(this);
        observeViewModel();
        initRecyclerView();
    }

    @Override
    public void onItemClick(Object object, int position) {
        if (object instanceof CourseAdapter) {
            Course course = courseAdapter.getItem(position);
            User user = mVM.getCacheUser().getValue();

            Intent intent = new Intent(context, CourseDetailActivity.class);
            intent.putExtra("course", course);
            intent.putExtra("user", user);

            startActivity(intent);
        } else if (object instanceof WordAdapter) {
            // open WordInfoActivity
            Log.d(AppConstants.TAG, "Open WordInfo");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnViewAll){
            context.openFragment(VocabularyFragment.class, getString(R.string.vocabulary), R.id.mnVocab);
        }
    }

    private void observeViewModel() {
        mVM.getCacheUser().observe(getViewLifecycleOwner(), user -> {
            if (user != null)
                mBinding.txtHello.setText(getString(R.string.hello, user.getDisplayName()));
            else
                mBinding.txtHello.setText(getString(R.string.hello, "Guys"));
        });

        mVM.getTop30Words().observe(getViewLifecycleOwner(), words -> {
            if (words != null){
                wordsAdapter.setData(words);
            }
        });

        mVM.getCourses().observe(getViewLifecycleOwner(), courses -> {
            if (courses != null){
                courseAdapter.setData(courses);
            }
        });
    }

    private void initRecyclerView() {
        courses = new ArrayList<>();
        words = new ArrayList<>();

        courseAdapter = new CourseAdapter(context, courses, R.layout.info_course, CourseAdapter.Owner.HOME_FRAGMENT);
        wordsAdapter = new WordAdapter(context, words);

        courseAdapter.setOnItemClickListener(this);
        wordsAdapter.setOnItemClickListener(this);

        mBinding.rvCourse.setAdapter(courseAdapter);
        mBinding.rvCourse.setHasFixedSize(false);
        mBinding.rvCourse.setLayoutManager(new GridLayoutManager(context, 1,
                RecyclerView.HORIZONTAL, false));

        mBinding.rvVocab.setAdapter(wordsAdapter);
        mBinding.rvVocab.setHasFixedSize(false);
        mBinding.rvVocab.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mBinding.rvVocab.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
    }
}